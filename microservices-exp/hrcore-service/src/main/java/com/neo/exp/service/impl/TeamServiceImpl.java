package com.neo.exp.service.impl;

import com.neo.exp.domain.TeamEntity;
import com.neo.exp.dto.TeamDto;
import com.neo.exp.repository.*;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.TeamService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class TeamServiceImpl implements TeamService {
    @Inject
    TeamRepository teamRepository;
    @Inject
    OrganizationRepository organizationRepository;

    public static TeamDto mapToDto(TeamEntity team) {
        return new TeamDto(
                team.getId(),
                team.getCode(),
                team.getLabel(),
                team.getTenant(),
                team.getOrganization().getId()
        );
    }

    @Override
    public MessageResponse create(TeamDto teamDto) {
        log.debug("Request to create team : {}", teamDto);

        var organizationId = teamDto.getOrganization_id();
        var organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new IllegalStateException("The organization with ID[" + organizationId + "] was not found !"));

        TeamEntity team = new TeamEntity(
                teamDto.getCode(),
                teamDto.getLabel(),
                teamDto.getTenant(),
                organization
        );

        this.teamRepository.save(team);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(team)
        );
    }

    @Override
    public MessageResponse update(TeamDto teamDto) {
        log.debug("Request to update team : {}", teamDto);

        boolean existe = teamRepository.existsById(teamDto.getId());
        if (existe){
            var organizationId = teamDto.getOrganization_id();
            var organization = this.organizationRepository.findById(organizationId)
                    .orElseThrow(() ->
                            new IllegalStateException("The organization with ID[" + organizationId + "] was not found !"));

            TeamEntity team = new TeamEntity(
                    teamDto.getId(),
                    Instant.now(),
                    teamDto.getCode(),
                    teamDto.getLabel(),
                    teamDto.getTenant(),
                    organization
            );

            this.teamRepository.save(team);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(team)
            );
        } else {
            throw new IllegalStateException("There is already an team");
        }
    }

    @Override
    public TeamDto findById(Long id) {
        log.debug("Request to get team : {}", id);
        return this.teamRepository.findById(id).map(TeamServiceImpl::mapToDto).orElse(null);
    }

    public List<TeamDto> findAll() {
        log.debug("Request to get all team");
        return this.teamRepository.findAll()
                .stream()
                .map(TeamServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete team : {}", id);

        var team = this.teamRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find team with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.teamRepository.delete(team);
    }
}