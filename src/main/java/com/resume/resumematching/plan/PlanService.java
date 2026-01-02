package com.resume.resumematching.plan;

import com.resume.resumematching.enums.PlanStatus;
import com.resume.resumematching.plan.dto.CreatePlanRequest;
import com.resume.resumematching.plan.dto.PlanResponse;
import com.resume.resumematching.plan.dto.UpdatePlanRequest;
import com.resume.resumematching.plan.entity.Plan;
import com.resume.resumematching.plan.mapper.PlanMapper;
import com.resume.resumematching.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    // SUPERUSER
    @Transactional
    public PlanResponse createPlan(CreatePlanRequest request) {

        // Normalize name
        String normalizedName = request.getName().trim().toUpperCase();

        // Find currently ACTIVE plan (same name)
        Optional<Plan> activePlanOpt =
                planRepository.findTopByNameIgnoreCaseAndStatusOrderByVersionDesc(
                        normalizedName,
                        PlanStatus.ACTIVE
                );

        // Pause old active plan
        activePlanOpt.ifPresent(activePlan -> {
            activePlan.setStatus(PlanStatus.PAUSED);
            planRepository.save(activePlan);
        });

        // Calculate next version
        int nextVersion = planRepository
                .findMaxVersionByName(normalizedName)
                .orElse(0) + 1;

        // Create new plan as DRAFT
        Plan newPlan = planMapper.toEntity(request);
        newPlan.setName(normalizedName);
        newPlan.setVersion(nextVersion);
        newPlan.setStatus(PlanStatus.DRAFT);

        Plan saved = planRepository.save(newPlan);

        return planMapper.toResponse(saved);
    }

    // ADMIN → ACTIVE only
    // SUPERUSER → all
    public List<PlanResponse> getPlansForCurrentUser() {

        if (SecurityUtils.isSuperUser()) {
            return planMapper.toResponseList(planRepository.findAll());
        }

        return planMapper.toResponseList(
                planRepository.findByStatus(PlanStatus.ACTIVE)
        );
    }

    // SUPERUSER → ONLY DRAFT editable
    public PlanResponse updatePlan(Long id, UpdatePlanRequest request) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        if (plan.getStatus() != PlanStatus.DRAFT) {
            throw new IllegalStateException("Only draft plans can be edited");
        }

        planMapper.updatePlanFromRequest(request, plan);

        return planMapper.toResponse(planRepository.save(plan));
    }

    // SUPERUSER
    public void publishPlan(Long id) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        if (plan.getStatus() != PlanStatus.DRAFT) {
            throw new RuntimeException("Only draft plans can be published");
        }

        plan.setStatus(PlanStatus.ACTIVE);
        planRepository.save(plan);
    }

    // SUPERUSER
    public void pausePlan(Long id) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        if (plan.getStatus() != PlanStatus.ACTIVE) {
            throw new RuntimeException("Only active plans can be paused");
        }

        plan.setStatus(PlanStatus.PAUSED);
        planRepository.save(plan);
    }
}
