package com.resume.resumematching.controller;

import com.resume.resumematching.dto.plan.CreatePlanRequest;
import com.resume.resumematching.dto.plan.PlanResponse;
import com.resume.resumematching.dto.plan.UpdatePlanRequest;
import com.resume.resumematching.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    // SUPERUSER
    @PreAuthorize("hasRole('SUPERUSER')")
    @PostMapping
    public PlanResponse createPlan(
            @Valid @RequestBody CreatePlanRequest request
    ) {
        return planService.createPlan(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    @GetMapping
    public List<PlanResponse> getAllPlans() {
        return planService.getAllPlans();
    }

    // SUPERUSER
    @PreAuthorize("hasRole('SUPERUSER')")
    @PutMapping("/{id}")
    public PlanResponse updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlanRequest request
    ) {
        return planService.updatePlan(id, request);
    }
}
