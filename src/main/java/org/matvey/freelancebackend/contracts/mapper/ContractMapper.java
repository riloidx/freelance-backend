package org.matvey.freelancebackend.contracts.mapper;

import org.mapstruct.Mapper;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = ProposalMapper.class)
public interface ContractMapper {
    ContractResponseDto toDto(Contract contract);

    default Page<ContractResponseDto> toDtoPage(Page<Contract> contracts) {
        return contracts.map(this::toDto);
    }
}
