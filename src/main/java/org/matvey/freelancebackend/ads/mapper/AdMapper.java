package org.matvey.freelancebackend.ads.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.matvey.freelancebackend.ads.dto.request.AdCreateDto;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.category.mapper.CategoryMapper;
import org.matvey.freelancebackend.roles.mapper.RoleMapper;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * MapStruct mapper interface for Advertisement entity transformations.
 * 
 * Handles conversion between Ad entities and various DTOs including
 * response DTOs, create DTOs, and update DTOs. Supports both single
 * entity mapping and collections with pagination.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class, CategoryMapper.class})
public interface AdMapper {
    /**
     * Converts an Ad entity to an AdResponseDto.
     * 
     * @param ad the advertisement entity to convert
     * @return the corresponding advertisement response DTO
     */
    AdResponseDto toDto(Ad ad);

    /**
     * Converts a list of Ad entities to AdResponseDtos.
     * 
     * @param ads the list of advertisement entities
     * @return list of advertisement response DTOs
     */
    List<AdResponseDto> toDto(List<Ad> ads);

    /**
     * Converts a paginated list of Ad entities to AdResponseDtos.
     * 
     * @param ads the paginated advertisement entities
     * @return paginated advertisement response DTOs
     */
    default Page<AdResponseDto> toDto(Page<Ad> ads) {
        return ads.map(this::toDto);
    }

    /**
     * Converts an AdCreateDto to an Ad entity.
     * 
     * @param adCreateDto the create DTO
     * @return the corresponding advertisement entity
     */
    Ad toEntity(AdCreateDto adCreateDto);

    /**
     * Updates an existing Ad entity with data from AdUpdateDto.
     * 
     * @param dto the update DTO containing new values
     * @param user the target advertisement entity to update
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AdUpdateDto dto, @MappingTarget Ad user);
}
