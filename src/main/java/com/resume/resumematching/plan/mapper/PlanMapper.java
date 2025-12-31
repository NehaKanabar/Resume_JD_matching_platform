package com.resume.resumematching.plan.mapper;

import com.resume.resumematching.plan.dto.PlanResponse;
import com.resume.resumematching.plan.entity.Plan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    PlanResponse toResponse(Plan plan);

    List<PlanResponse> toResponseList(List<Plan> plans);
}
