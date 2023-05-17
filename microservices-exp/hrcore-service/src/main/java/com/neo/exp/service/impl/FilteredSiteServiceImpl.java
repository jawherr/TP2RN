package com.neo.exp.service.impl;

import com.neo.exp.domain.FilteredSiteEntity;
import com.neo.exp.dto.FilteredSiteDto;
import com.neo.exp.repository.*;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FilteredSiteService;
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
public class FilteredSiteServiceImpl implements FilteredSiteService {
    @Inject
    FilteredSiteRepository filteredSiteRepository;
    @Inject
    AffectedRoleRepository affectedRoleRepository;
    @Inject
    SiteRepository siteRepository;

    public static FilteredSiteDto mapToDto(FilteredSiteEntity filteredSiteEntity) {
        return new FilteredSiteDto(
                filteredSiteEntity.getId(),
                filteredSiteEntity.getAffectedRole().getId(),
                filteredSiteEntity.getSite().getId()
        );
    }

    @Override
    public MessageResponse create(FilteredSiteDto filteredSiteDto) {
        log.debug("Request to create filtered site : {}", filteredSiteDto);

        var affectedRoleId = filteredSiteDto.getAffectedRole_id();
        var affectedRole = this.affectedRoleRepository.findById(affectedRoleId)
                .orElseThrow(() ->
                        new IllegalStateException("The affected role with ID[" + affectedRoleId + "] was not found !"));

        var siteId = filteredSiteDto.getSite_id();
        var site = this.siteRepository.findById(siteId)
                .orElseThrow(() ->
                        new IllegalStateException("The site with ID[" + siteId + "] was not found !"));

        FilteredSiteEntity filteredSite = new FilteredSiteEntity(
                affectedRole,
                site
        );

        this.affectedRoleRepository.save(affectedRole);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(filteredSite)
        );
    }

    @Override
    public MessageResponse update(FilteredSiteDto filteredSiteDto) {
        log.debug("Request to update filtered site : {}", filteredSiteDto);

        boolean existe = filteredSiteRepository.existsById(filteredSiteDto.getId());
        if (!existe){
            var affectedRoleId = filteredSiteDto.getAffectedRole_id();
            var affectedRole = this.affectedRoleRepository.findById(affectedRoleId)
                    .orElseThrow(() ->
                            new IllegalStateException("The affected role with ID[" + affectedRoleId + "] was not found !"));

            var siteId = filteredSiteDto.getSite_id();
            var site = this.siteRepository.findById(siteId)
                    .orElseThrow(() ->
                            new IllegalStateException("The site with ID[" + siteId + "] was not found !"));

            FilteredSiteEntity filteredSite = new FilteredSiteEntity(
                    filteredSiteDto.getId(),
                    Instant.now(),
                    affectedRole,
                    site
            );

            this.affectedRoleRepository.save(affectedRole);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(filteredSite)
            );
        } else {
            throw new IllegalStateException("There is already an affected role");
        }
    }

    @Override
    public FilteredSiteDto findById(Long id) {
        log.debug("Request to get filtered site : {}", id);
        return this.filteredSiteRepository.findById(id).map(FilteredSiteServiceImpl::mapToDto).orElse(null);
    }

    public List<FilteredSiteDto> findAll() {
        log.debug("Request to get all filtered site");
        return this.filteredSiteRepository.findAll()
                .stream()
                .map(FilteredSiteServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete filtered site : {}", id);

        var filteredSite = this.filteredSiteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find filtered site with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.filteredSiteRepository.delete(filteredSite);
    }
}