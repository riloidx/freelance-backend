package org.matvey.freelancebackend.ads.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.dto.request.AdCreateDto;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.service.api.AdCommandService;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdCommandService adCommandService;
    private final AdQueryService adQueryService;

    @GetMapping("/me")
    public ResponseEntity<List<AdResponseDto>> getMyAds(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            Authentication authentication) {
        List<AdResponseDto> ads = adQueryService.findAdsByUser(authentication, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ads);
    }

    @GetMapping
    public ResponseEntity<Page<AdResponseDto>> findAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<AdResponseDto> ads = adQueryService.findAllByOrderByCreatedDesc(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdResponseDto> findById(@PathVariable long id) {
        AdResponseDto ad = adQueryService.findDtoById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ad);
    }

    @PostMapping
    public ResponseEntity<AdResponseDto> create(@Valid @RequestBody AdCreateDto adCreateDto,
                                                Authentication authentication) {
        AdResponseDto created = adCommandService.create(adCreateDto, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdResponseDto> update(@PathVariable long id,
                                                @Valid @RequestBody AdUpdateDto adUpdateDto,
                                                Authentication authentication) {
        adUpdateDto.setId(id);
        AdResponseDto updated = adCommandService.update(adUpdateDto, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, Authentication authentication) {
        adCommandService.delete(id, authentication);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
