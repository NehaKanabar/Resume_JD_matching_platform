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

        Plan plan = Plan.builder()
                .name(request.getName())
                .resumeLimit(request.getResumeLimit())
                .jdLimit(request.getJdLimit())
                .matchLimit(request.getMatchLimit())
                .priceMonthly(request.getPriceMonthly())
                .priceYearly(request.getPriceYearly())
                .build();

        Plan saved = planRepository.save(plan);

        // ✅ MapStruct here
        return planMapper.toResponse(saved);
    }

    public List<PlanResponse> getAllPlans() {
        return planMapper.toResponseList(planRepository.findAll());
    }

    // SUPERUSER only
    public PlanResponse updatePlan(Long id, UpdatePlanRequest request) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan.setResumeLimit(request.getResumeLimit());
        plan.setJdLimit(request.getJdLimit());
        plan.setMatchLimit(request.getMatchLimit());
        plan.setPriceMonthly(request.getPriceMonthly());
        plan.setPriceYearly(request.getPriceYearly());

        Plan updated = planRepository.save(plan);

        // ✅ MapStruct here
        return planMapper.toResponse(updated);
    }
}
