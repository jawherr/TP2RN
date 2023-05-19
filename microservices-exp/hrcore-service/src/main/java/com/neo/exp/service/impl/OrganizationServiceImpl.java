package com.neo.exp.service.impl;

import com.neo.exp.domain.OrganizationEntity;
import com.neo.exp.dto.OrganizationDto;
import com.neo.exp.repository.OrganizationRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    @Inject
    OrganizationRepository organizationRepository;

    public static OrganizationDto mapToDto(OrganizationEntity organization){
        return new OrganizationDto(
                organization.getId(),
                organization.getCode(),
                organization.getLabel(),
                organization.getLogo(),
                organization.getTenant()
        );
    }

    @Override
    public MessageResponse create(OrganizationDto organizationDto) {
        log.debug("Request to create organization : {}", organizationDto);

        boolean existe = organizationRepository.existsByCode(organizationDto.getCode());
        if (!existe){
            OrganizationEntity organization = new OrganizationEntity(
                    Collections.emptySet(),
                    Collections.emptySet(),
                    organizationDto.getCode(),
                    organizationDto.getLabel(),
                    organizationDto.getLogo(),
                    organizationDto.getTenant(),
                    Collections.emptySet()
            );

            this.organizationRepository.save(organization);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(organization)
            );
        } else {
            throw new IllegalStateException("There is already an organization");
        }
    }

    @Override
    public MessageResponse update(OrganizationDto organizationDto) {
        log.debug("Request to update organization : {}", organizationDto);

        boolean existe = organizationRepository.existsById(organizationDto.getId());
        if (existe){
            OrganizationEntity organization = new OrganizationEntity(
                    organizationDto.getId(),
                    Instant.now(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    organizationDto.getCode(),
                    organizationDto.getLabel(),
                    organizationDto.getLogo(),
                    organizationDto.getTenant(),
                    Collections.emptySet()
            );

            this.organizationRepository.save(organization);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(organization)
            );
        } else {
            throw new IllegalStateException("There is already an organization");
        }
    }
    @Override
    public OrganizationDto findById(Long id) {
        log.debug("Request to get organization : {}", id);
        return this.organizationRepository.findById(id).map(OrganizationServiceImpl::mapToDto).orElse(null);
    }
    @Override
    public List<OrganizationDto> findAll() {
        log.debug("Request to get all organization");
        return this.organizationRepository.findAll()
                .stream()
                .map(OrganizationServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        log.debug("Request to delete organization : {}", id);
        var organization = this.organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find organization with id " + id));

        this.organizationRepository.delete(organization);
    }
}
