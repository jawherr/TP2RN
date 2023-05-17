package com.neo.exp.service.impl;

import com.neo.exp.domain.CrContentEntity;
import com.neo.exp.dto.CrContentDto;
import com.neo.exp.repository.*;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.CrContentService;
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
public class CrContentServiceImpl implements CrContentService {
    @Inject
    CrContentRepository crContentRepository;
    @Inject
    DynamicCrConfigurationRepository dynamicCrConfigurationRepository;

    public static CrContentDto mapToDto(CrContentEntity crContent) {
        return new CrContentDto(
                crContent.getId(),
                crContent.getValue(),
                crContent.getCodeLanguages(),
                crContent.getCanEdit(),
                crContent.getCanDelete(),
                crContent.getDynamicCrConfiguration().getId()
        );
    }

    @Override
    public MessageResponse create(CrContentDto crContentDto) {
        log.debug("Request to create cr content role : {}", crContentDto);

        var dynamicCrConfigurationId = crContentDto.getDynamicCrConfiguration_id();
        var dynamicCrConfiguration = this.dynamicCrConfigurationRepository.findById(dynamicCrConfigurationId)
                .orElseThrow(() ->
                        new IllegalStateException("The dynamic cr configuration with ID[" + dynamicCrConfigurationId + "] was not found !"));

        CrContentEntity crContent = new CrContentEntity(
                crContentDto.getValue(),
                crContentDto.getCodeLanguages(),
                crContentDto.getCanEdit(),
                crContentDto.getCanDelete(),
                Collections.emptySet(),
                Collections.emptySet(),
                dynamicCrConfiguration
        );

        this.crContentRepository.save(crContent);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(crContent)
        );
    }

    @Override
    public MessageResponse update(CrContentDto crContentDto) {
        log.debug("Request to update cr content role : {}", crContentDto);

        boolean existe = crContentRepository.existsById(crContentDto.getId());
        if (!existe){
            var dynamicCrConfigurationId = crContentDto.getDynamicCrConfiguration_id();
            var dynamicCrConfiguration = this.dynamicCrConfigurationRepository.findById(dynamicCrConfigurationId)
                    .orElseThrow(() ->
                            new IllegalStateException("The dynamic cr configuration with ID[" + dynamicCrConfigurationId + "] was not found !"));

            CrContentEntity crContent = new CrContentEntity(
                    crContentDto.getId(),
                    Instant.now(),
                    crContentDto.getValue(),
                    crContentDto.getCodeLanguages(),
                    crContentDto.getCanEdit(),
                    crContentDto.getCanDelete(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    dynamicCrConfiguration
            );

            this.crContentRepository.save(crContent);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(crContent)
            );
        } else {
            throw new IllegalStateException("There is already an affected role");
        }
    }

    @Override
    public CrContentDto findById(Long id) {
        log.debug("Request to get affected role : {}", id);
        return this.crContentRepository.findById(id).map(CrContentServiceImpl::mapToDto).orElse(null);
    }

    public List<CrContentDto> findAll() {
        log.debug("Request to get all cr content");
        return this.crContentRepository.findAll()
                .stream()
                .map(CrContentServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Affected role : {}", id);

        var affectedRole = this.crContentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find cr content with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.crContentRepository.delete(affectedRole);
    }
}