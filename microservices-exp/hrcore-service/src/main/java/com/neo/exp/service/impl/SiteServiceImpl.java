package com.neo.exp.service.impl;

import com.neo.exp.domain.SiteEntity;
import com.neo.exp.dto.SiteDto;
import com.neo.exp.repository.SiteRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.SiteService;
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
public class SiteServiceImpl implements SiteService {
    @Inject
    SiteRepository siteRepository;

    public static SiteDto mapToDto(SiteEntity site) {
        return new SiteDto(
                site.getId(),
                site.getCode(),
                site.getLabel(),
                site.getAddress(),
                site.getCountry(),
                site.getCity(),
                site.getPostalCode(),
                site.getTenant()
        );
    }

    @Override
    public MessageResponse create(SiteDto siteDto) {
        log.debug("Request to create site : {}", siteDto);

        SiteEntity site = new SiteEntity(
                siteDto.getCode(),
                siteDto.getLabel(),
                siteDto.getAddress(),
                siteDto.getCountry(),
                siteDto.getCity(),
                siteDto.getPostalCode(),
                siteDto.getTenant(),
                Collections.emptySet()
        );

        this.siteRepository.save(site);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(site)
        );
    }

    @Override
    public MessageResponse update(SiteDto siteDto) {
        log.debug("Request to update site : {}", siteDto);

        boolean existe = siteRepository.existsById(siteDto.getId());
        if (existe){
            SiteEntity site = new SiteEntity(
                    siteDto.getId(),
                    Instant.now(),
                    siteDto.getCode(),
                    siteDto.getLabel(),
                    siteDto.getAddress(),
                    siteDto.getCountry(),
                    siteDto.getCity(),
                    siteDto.getPostalCode(),
                    siteDto.getTenant(),
                    Collections.emptySet()
            );

            this.siteRepository.save(site);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(site)
            );
        } else {
            throw new IllegalStateException("There is already an site");
        }
    }

    @Override
    public SiteDto findById(Long id) {
        log.debug("Request to get site : {}", id);
        return this.siteRepository.findById(id).map(SiteServiceImpl::mapToDto).orElse(null);
    }

    public List<SiteDto> findAll() {
        log.debug("Request to get all site");
        return this.siteRepository.findAll()
                .stream()
                .map(SiteServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete site : {}", id);

        var site = this.siteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find site with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.siteRepository.delete(site);
    }
}