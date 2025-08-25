package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.AccountRequest;
import com.example.clinic_skin_be.dto.AuthResponse;
import com.example.clinic_skin_be.model.Account;
import com.example.clinic_skin_be.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    // Map từ DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avtPath", ignore = true)   // xử lý upload riêng
    @Mapping(target = "roles", ignore = true)     // set trong service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(AccountRequest dto);

    // Map từ Entity -> DTO Response
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "avatarUrl", source = "avtPath") // map chuẩn sang DTO
    @Mapping(target = "roles", expression = "java(mapRoles(account.getRoles()))")
    @Mapping(target = "gender", expression = "java(account.getGender() != null ? account.getGender().name() : null)")
    @Mapping(target = "status", expression = "java(account.getStatus() != null ? account.getStatus().name() : null)")
    AuthResponse toAuthResponse(Account account);

    // Map role entity -> String
    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
