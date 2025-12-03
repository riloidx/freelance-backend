package org.matvey.freelancebackend.contracts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDto {
    private long id;
    private BigDecimal price;
    private String contractStatus;
    private Instant createdAt;
    private String deliveryUrl;
    private ProposalSummaryDto proposal;
    private UserSummaryDto freelancer;
    private UserSummaryDto buyer;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProposalSummaryDto {
        private Long id;
        private BigDecimal price;
        private String message;
        private AdSummaryDto ad;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdSummaryDto {
        private Long id;
        private String titleEn;
        private String titleRu;
        private String descriptionEn;
        private String descriptionRu;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummaryDto {
        private Long id;
        private String name;
        private String username;
    }
}
