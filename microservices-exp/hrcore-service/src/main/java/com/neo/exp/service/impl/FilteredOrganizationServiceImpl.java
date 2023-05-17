package com.neo.exp.service.impl;

import com.neo.exp.domain.FilteredOrganizationEntity;
import com.neo.exp.dto.FilteredOrganizationDto;
import com.neo.exp.repository.*;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FilteredOrganizationService;
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
public class FilteredOrganizationServiceImpl implements FilteredOrganizationService {
    @Inject
    FilteredOrganizationRepository filteredOrganizationRepository;
    @Inject
    OrganizationRepository organizationRepository;
    @Inject
    AffectedRoleRepository affectedRoleRepository;

    public static FilteredOrganizationDto mapToDto(FilteredOrganizationEntity filteredOrganization) {
        return new FilteredOrganizationDto(
                filteredOrganization.getId(),
                filteredOrganization.getOrganization().getId(),
                filteredOrganization.getAffectedRole().getId()
        );
    }

    @Override
    public MessageResponse create(FilteredOrganizationDto filteredOrganizationDto) {
        log.debug("Request to create filtered organization : {}", filteredOrganizationDto);

        boolean existe = filteredOrganizationRepository.existsById(filteredOrganizationDto.getId());
        if (!existe){
            var organizationId = filteredOrganizationDto.getOrganization_id();
            var organization = this.organizationRepository.findById(organizationId)
                    .orElseThrow(() ->
                            new IllegalStateException("The organization with ID[" + organizationId + "] was not found !"));
            var affectedRoleId = filteredOrganizationDto.getAffectedRole_id();
            var affectedRole = this.affectedRoleRepository.findById(affectedRoleId)
                    .orElseThrow(() ->
                            new IllegalStateException("The affected role with ID[" + affectedRoleId + "] was not found !"));

            FilteredOrganizationEntity filteredOrganization = new FilteredOrganizationEntity(
                    organization,
                    affectedRole
            );

            this.filteredOrganizationRepository.save(filteredOrganization);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(filteredOrganization)
            );
        } else {
            throw new IllegalStateException("There is already an filtered organization");
        }
    }



    @Override
    public MessageResponse update(FilteredOrganizationDto filteredOrganizationDto) {
        log.debug("Request to update filtered organization : {}", filteredOrganizationDto);

        boolean existe = filteredOrganizationRepository.existsById(filteredOrganizationDto.getId());
        if (existe){
            var organizationId = filteredOrganizationDto.getOrganization_id();
            var organization = this.organizationRepository.findById(organizationId)
                    .orElseThrow(() ->
                            new IllegalStateException("The organization with ID[" + organizationId + "] was not found !"));
            var affectedRoleId = filteredOrganizationDto.getAffectedRole_id();
            var affectedRole = this.affectedRoleRepository.findById(affectedRoleId)
                    .orElseThrow(() ->
                            new IllegalStateException("The affected role with ID[" + affectedRoleId + "] was not found !"));

            FilteredOrganizationEntity filteredOrganization = new FilteredOrganizationEntity(
                    filteredOrganizationDto.getId(),
                    Instant.now(),
                    organization,
                    affectedRole
            );

            this.filteredOrganizationRepository.save(filteredOrganization);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(filteredOrganization)
            );
        } else {
            throw new IllegalStateException("There is already an filtered organization");
        }
    }

    @Override
    public FilteredOrganizationDto findById(Long id) {
        log.debug("Request to get filtered organization : {}", id);
        return this.filteredOrganizationRepository.findById(id).map(FilteredOrganizationServiceImpl::mapToDto).orElse(null);
    }

    @Override
    public List<FilteredOrganizationDto> findAll() {
        log.debug("Request to get all filtered organization");
        return this.filteredOrganizationRepository.findAll()
                .stream()
                .map(FilteredOrganizationServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Filtered organization : {}", id);

        var filteredOrganization = this.filteredOrganizationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find filtered organization with id " + id));

        this.filteredOrganizationRepository.delete(filteredOrganization);
    }
}
