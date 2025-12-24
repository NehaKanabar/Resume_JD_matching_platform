package com.resume.resumematching.plan;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.plan.dto.CreatePlanRequest;
import com.resume.resumematching.plan.dto.PlanResponse;
import com.resume.resumematching.plan.dto.UpdatePlanRequest;
import com.resume.resumematching.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    // SUPERUSER → Create plan
    @PreAuthorize("hasRole('SUPERUSER')")
    @PostMapping
    public ResponseEntity<ApiResponse<PlanResponse>> createPlan(
            @Valid @RequestBody CreatePlanRequest request
    ) {

        PlanResponse plan = planService.createPlan(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Plan created successfully",
                        plan
                )
        );
    }

    // ADMIN + SUPERUSER → View plans
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PlanResponse>>> getAllPlans() {

        List<PlanResponse> plans = planService.getAllPlans();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Plans fetched successfully",
                        plans
                )
        );
    }

    // SUPERUSER → Update plan
    @PreAuthorize("hasRole('SUPERUSER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanResponse>> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlanRequest request
    ) {

        PlanResponse plan = planService.updatePlan(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Plan updated successfully",
                        plan
                )
        );
    }
}
