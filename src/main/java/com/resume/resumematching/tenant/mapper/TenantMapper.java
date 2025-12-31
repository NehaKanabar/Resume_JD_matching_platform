package com.resume.resumematching.tenant.mapper;

import com.resume.resumematching.tenant.dto.TenantResponse;
import com.resume.resumematching.tenant.entity.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(source = "name", target = "tenantName")
    TenantResponse toResponse(Tenant tenant);

    List<TenantResponse> toResponseList(List<Tenant> tenants);
}

