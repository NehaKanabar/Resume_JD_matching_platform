package com.resume.resumematching.plan.mapper;

import com.resume.resumematching.plan.dto.CreatePlanRequest;
import com.resume.resumematching.plan.dto.PlanResponse;
import com.resume.resumematching.plan.dto.UpdatePlanRequest;
import com.resume.resumematching.plan.entity.Plan;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    PlanResponse toResponse(Plan plan);

    List<PlanResponse> toResponseList(List<Plan> plans);

    Plan toEntity(CreatePlanRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePlanFromRequest(UpdatePlanRequest request,
                               @MappingTarget Plan plan);
}
