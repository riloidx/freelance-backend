package org.matvey.freelancebackend.proposal.mapper;

import org.mapstruct.Mapper;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {AdMapper.class, UserMapper.class})
public interface ProposalMapper {

    @org.mapstruct.Mapping(source = "freelancer", target = "user")
    ProposalResponseDto toDto(Proposal proposal);

    default Page<ProposalResponseDto> toDto(Page<Proposal> proposals) {
        return proposals.map(this::toDto);
    }

    Proposal toEntity(ProposalCreateDto proposalCreateDto);
}
