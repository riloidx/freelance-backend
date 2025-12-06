package org.matvey.freelancebackend.users.mapper;

import org.mapstruct.*;
import org.matvey.freelancebackend.roles.mapper.RoleMapper;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UserUpdateDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserProfileResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.data.domain.Page;

/**
 * MapStruct mapper interface for User entity transformations.
 * 
 * Handles conversion between User entities and various DTOs including
 * response DTOs, profile DTOs, and registration DTOs. Supports both
 * single entity mapping and paginated collections.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    /**
     * Converts a User entity to a UserResponseDto.
     * 
     * @param user the user entity to convert
     * @return the corresponding user response DTO
     */
    UserResponseDto toDto(User user);

    /**
     * Converts a User entity to a UserProfileResponseDto.
     * 
     * @param user the user entity to convert
     * @return the corresponding user profile DTO
     */
    UserProfileResponseDto toProfileDto(User user);

    /**
     * Converts a paginated list of User entities to UserResponseDtos.
     * 
     * @param users the paginated user entities
     * @return paginated user response DTOs
     */
    default Page<UserResponseDto> toDto(Page<User> users) {
        return users.map(this::toDto);
    }

    /**
     * Converts a RegistrationDto to a User entity.
     * 
     * @param dto the registration DTO
     * @return the corresponding user entity
     */
    @Mapping(target = "roles", ignore = true)
    User toEntity(RegistrationDto dto);

    /**
     * Updates an existing User entity with data from UserUpdateDto.
     * 
     * @param dto the update DTO containing new values
     * @param user the target user entity to update
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User user);
}
