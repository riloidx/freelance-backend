package org.matvey.freelancebackend.ads.controller.admin;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/admin/ads")
@RequiredArgsConstructor
public class AdminAdController {
    private final AdQueryService adQueryService;
    private final AdCommandService adCommandService;

    @GetMapping
    public ResponseEntity<Page<AdResponseDto>> findAllAds(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            Authentication auth) {
        Page<AdResponseDto> ads = adQueryService.findAllByOrderByCreatedDesc(pageable, auth);

        return ResponseEntity.status(HttpStatus.OK).body(ads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdResponseDto> findById(@PathVariable long id) {
        AdResponseDto ad = adQueryService.findDtoById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        adCommandService.delete(id, null);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
