package org.matvey.freelancebackend.security.dto.request;

import lombok.Data;

@Data
public class RefreshRequestDto {
    private String accessToken;
}
