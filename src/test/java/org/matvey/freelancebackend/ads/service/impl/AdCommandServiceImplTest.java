package org.matvey.freelancebackend.ads.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.ads.dto.request.AdCreateDto;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.matvey.freelancebackend.ads.entity.AdType;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.ads.service.util.AdUpdater;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdCommandServiceImplTest {

    @Mock
    private AdRepository adRepo;
    
    @Mock
    private AdMapper adMapper;
    
    @Mock
    private CategoryQueryService categoryQueryService;
    
    @Mock
    private AdSecurityUtil adSecurityUtil;
    
    @Mock
    private AdUpdater adUpdater;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private CustomUserDetails userDetails;
    
    @InjectMocks
    private AdCommandServiceImpl adCommandService;
    
    private User user;
    private Category category;
    private Ad ad;
    private AdCreateDto adCreateDto;
    private AdUpdateDto adUpdateDto;
    private AdResponseDto adResponseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        category = new Category();
        category.setId(1L);
        
        ad = new Ad();
        ad.setId(1L);
        ad.setTitleEn("Test Ad");
        ad.setTitleRu("Тестовое объявление");
        ad.setDescriptionEn("Test Description");
        ad.setDescriptionRu("Тестовое описание");
        ad.setBudget(BigDecimal.valueOf(1000));
        ad.setStatus(AdStatus.ACTIVE);
        ad.setAdType(AdType.OFFER);
        ad.setUser(user);
        ad.setCategory(category);
        
        adCreateDto = new AdCreateDto();
        adCreateDto.setTitleEn("Test Ad Title");
        adCreateDto.setTitleRu("Тестовое объявление");
        adCreateDto.setDescriptionEn("Test Description");
        adCreateDto.setDescriptionRu("Тестовое описание");
        adCreateDto.setBudget(BigDecimal.valueOf(1000));
        adCreateDto.setCategoryId(1L);
        
        adUpdateDto = new AdUpdateDto();
        adUpdateDto.setId(1L);
        adUpdateDto.setTitleEn("Updated Ad");
        
        adResponseDto = new AdResponseDto();
        adResponseDto.setId(1L);
        adResponseDto.setTitleEn("Test Ad");
    }

    @Test
    void CreateShouldReturnAdResponseDto() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(categoryQueryService.findCategoryById(1L)).thenReturn(category);
        when(adMapper.toEntity(adCreateDto)).thenReturn(ad);
        when(adRepo.save(any(Ad.class))).thenReturn(ad);
        when(adMapper.toDto(ad)).thenReturn(adResponseDto);

        AdResponseDto result = adCommandService.create(adCreateDto, authentication);

        assertNotNull(result);
        assertEquals(adResponseDto.getId(), result.getId());
        verify(adRepo).save(any(Ad.class));
        verify(adMapper).toDto(ad);
    }

    @Test
    void UpdateShouldReturnUpdatedAdResponseDto() {
        when(adSecurityUtil.checkAdOwnerPermissionAndReturn(1L, authentication)).thenReturn(ad);
        when(adRepo.save(ad)).thenReturn(ad);
        when(adMapper.toDto(ad)).thenReturn(adResponseDto);

        AdResponseDto result = adCommandService.update(adUpdateDto, authentication);

        assertNotNull(result);
        verify(adUpdater).updateAdFromDto(ad, adUpdateDto);
        verify(adRepo).save(ad);
        verify(adMapper).toDto(ad);
    }

    @Test
    void DeleteShouldRemoveAd() {
        adCommandService.delete(1L, authentication);

        verify(adRepo).deleteById(1L);
    }
}