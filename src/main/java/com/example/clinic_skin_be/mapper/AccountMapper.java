package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.user.AccountResponse;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "roles", expression = "java(mapRoles(account.getRoles()))")
    @Mapping(target = "gender", expression = "java(account.getGender() != null ? account.getGender().name() : null)")
    @Mapping(target = "status", expression = "java(account.getStatus() != null ? account.getStatus().name() : null)")
    @Mapping(target = "createdAt", expression = "java(account.getCreatedAt() != null ? account.getCreatedAt().format(dtf) : null)")
    @Mapping(target = "updatedAt", expression = "java(account.getUpdatedAt() != null ? account.getUpdatedAt().format(dtf) : null)")
    AccountResponse toResponse(Account account);

    default Set<String> mapRoles(Set<Role> roles) {
        return roles != null ? roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet()) : null;
    }
}
