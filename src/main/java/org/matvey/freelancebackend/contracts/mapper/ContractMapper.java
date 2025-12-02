package org.matvey.freelancebackend.contracts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    
    @Mapping(target = "proposal.id", source = "proposal.id")
    @Mapping(target = "proposal.price", source = "proposal.price")
    @Mapping(target = "proposal.message", source = "proposal.message")
    @Mapping(target = "proposal.ad.id", source = "proposal.ad.id")
    @Mapping(target = "proposal.ad.title", source = "proposal.ad.title")
    @Mapping(target = "proposal.ad.description", source = "proposal.ad.description")
    @Mapping(target = "freelancer.id", source = "freelancer.id")
    @Mapping(target = "freelancer.name", source = "freelancer.name")
    @Mapping(target = "freelancer.username", source = "freelancer.username")
    @Mapping(target = "buyer.id", source = "buyer.id")
    @Mapping(target = "buyer.name", source = "buyer.name")
    @Mapping(target = "buyer.username", source = "buyer.username")
    ContractResponseDto toDto(Contract contract);

    default Page<ContractResponseDto> toDto(Page<Contract> contracts) {
        return contracts.map(this::toDto);
    }
}
