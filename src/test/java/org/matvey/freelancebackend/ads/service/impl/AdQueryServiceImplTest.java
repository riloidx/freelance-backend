package org.matvey.freelancebackend.ads.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.exception.AdNotFoundException;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdQueryServiceImplTest {

    @Mock
    private AdRepository adRepo;
    
    @Mock
    private AdMapper adMapper;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private CustomUserDetails userDetails;
    
    @InjectMocks
    private AdQueryServiceImpl adQueryService;
    
    private Ad ad;
    private AdResponseDto adResponseDto;
    private User user;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        ad = new Ad();
        ad.setId(1L);
        ad.setTitle("Test Ad");
        
        adResponseDto = new AdResponseDto();
        adResponseDto.setId(1L);
        adResponseDto.setTitle("Test Ad");
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void FindAllByOrderByCreatedDescShouldReturnPageOfAds() {
        Page<Ad> adPage = new PageImpl<>(List.of(ad));
        Page<AdResponseDto> expectedPage = new PageImpl<>(List.of(adResponseDto));
        
        when(adRepo.findAll(any(Pageable.class))).thenReturn(adPage);
        when(adMapper.toDto(adPage)).thenReturn(expectedPage);

        Page<AdResponseDto> result = adQueryService.findAllByOrderByCreatedDesc(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(adRepo).findAll(any(Pageable.class));
    }

    @Test
    void FindAllByUserIdShouldReturnListOfAds() {
        List<Ad> ads = List.of(ad);
        List<AdResponseDto> expectedDtos = List.of(adResponseDto);
        
        when(adRepo.findAllByUserId(1L)).thenReturn(ads);
        when(adMapper.toDto(ads)).thenReturn(expectedDtos);

        List<AdResponseDto> result = adQueryService.findAllByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(adRepo).findAllByUserId(1L);
    }

    @Test
    void FindAdByIdShouldReturnAd() {
        when(adRepo.findById(1L)).thenReturn(Optional.of(ad));

        Ad result = adQueryService.findAdById(1L);

        assertNotNull(result);
        assertEquals(ad.getId(), result.getId());
        verify(adRepo).findById(1L);
    }

    @Test
    void FindAdByIdShouldThrowExceptionWhenNotFound() {
        when(adRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AdNotFoundException.class, () -> adQueryService.findAdById(1L));
        verify(adRepo).findById(1L);
    }

    @Test
    void FindDtoByIdShouldReturnAdResponseDto() {
        when(adRepo.findById(1L)).thenReturn(Optional.of(ad));
        when(adMapper.toDto(ad)).thenReturn(adResponseDto);

        AdResponseDto result = adQueryService.findDtoById(1L);

        assertNotNull(result);
        assertEquals(adResponseDto.getId(), result.getId());
        verify(adRepo).findById(1L);
        verify(adMapper).toDto(ad);
    }

    @Test
    void FindAdsByUserShouldReturnUserAds() {
        List<Ad> ads = List.of(ad);
        List<AdResponseDto> expectedDtos = List.of(adResponseDto);
        
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(adRepo.findAllByUserId(1L)).thenReturn(ads);
        when(adMapper.toDto(ads)).thenReturn(expectedDtos);

        List<AdResponseDto> result = adQueryService.findAdsByUser(authentication, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(adRepo).findAllByUserId(1L);
    }
}