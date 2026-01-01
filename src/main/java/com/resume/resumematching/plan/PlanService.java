package com.resume.resumematching.plan;

import com.resume.resumematching.plan.dto.CreatePlanRequest;
import com.resume.resumematching.plan.dto.PlanResponse;
import com.resume.resumematching.plan.dto.UpdatePlanRequest;
import com.resume.resumematching.plan.entity.Plan;
import com.resume.resumematching.plan.mapper.PlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    // SUPERUSER only
    public PlanResponse createPlan(CreatePlanRequest request) {

        if (planRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Plan with same name already exists");
        }

        Plan plan = planMapper.toEntity(request);

        Plan saved = planRepository.save(plan);

        return planMapper.toResponse(saved);
    }

    public List<PlanResponse> getAllPlans() {
        return planMapper.toResponseList(planRepository.findAll());
    }

    // SUPERUSER only
    public PlanResponse updatePlan(Long id, UpdatePlanRequest request) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        planMapper.updatePlanFromRequest(request, plan);

        Plan updated = planRepository.save(plan);

        return planMapper.toResponse(updated);
    }
}
