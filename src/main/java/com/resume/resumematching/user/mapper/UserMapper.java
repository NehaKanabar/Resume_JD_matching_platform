package com.resume.resumematching.user.mapper;

import com.resume.resumematching.user.dto.CreateHrUserRequest;
import com.resume.resumematching.user.dto.UserResponse;
import com.resume.resumematching.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "tenant.id", target = "tenantId")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
