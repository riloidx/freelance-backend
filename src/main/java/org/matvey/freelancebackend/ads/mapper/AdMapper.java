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

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class, CategoryMapper.class})
public interface AdMapper {
    AdResponseDto toDto(Ad ad);

    List<AdResponseDto> toDto(List<Ad> ads);

    Page<AdResponseDto> toDto(Page<Ad> ads);

    Ad toEntity(AdCreateDto adCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AdUpdateDto dto, @MappingTarget Ad user);
}
