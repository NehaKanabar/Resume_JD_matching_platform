package com.resume.resumematching.plan;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.plan.dto.CreatePlanRequest;
import com.resume.resumematching.plan.dto.PlanResponse;
import com.resume.resumematching.plan.dto.UpdatePlanRequest;
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

    // SUPERUSER → Create plan (DRAFT)
    @PreAuthorize("hasRole('SUPERUSER')")
    @PostMapping
    public ResponseEntity<ApiResponse<PlanResponse>> createPlan(
            @Valid @RequestBody CreatePlanRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Plan created in draft state",
                        planService.createPlan(request)
                )
        );
    }

    // SUPERUSER → View ALL plans
    // ADMIN → View ONLY ACTIVE plans
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PlanResponse>>> getAllPlans() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Plans fetched successfully",
                        planService.getPlansForCurrentUser()
                )
        );
    }

    // SUPERUSER → Update ONLY DRAFT plan
    @PreAuthorize("hasRole('SUPERUSER')")
    @PutMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanResponse>> updatePlan(
            @PathVariable Long planId,
            @Valid @RequestBody UpdatePlanRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Plan updated successfully",
                        planService.updatePlan(planId, request)
                )
        );
    }

    // SUPERUSER → Publish plan (DRAFT → ACTIVE)
    @PreAuthorize("hasRole('SUPERUSER')")
    @PatchMapping("/{planId}/publish")
    public ResponseEntity<ApiResponse<Void>> publishPlan(@PathVariable Long planId) {
        planService.publishPlan(planId);
        return ResponseEntity.ok(
                ApiResponse.success("Plan published successfully", null)
        );
    }

    // SUPERUSER → Pause plan (ACTIVE → PAUSED)
    @PreAuthorize("hasRole('SUPERUSER')")
    @PatchMapping("/{planId}/pause")
    public ResponseEntity<ApiResponse<Void>> pausePlan(@PathVariable Long planId) {
        planService.pausePlan(planId);
        return ResponseEntity.ok(
                ApiResponse.success("Plan paused successfully", null)
        );
    }
}
