package com.neo.exp.service.impl;

import com.neo.exp.domain.DynamicCrConfigTranslatableEntity;
import com.neo.exp.dto.*;
import com.neo.exp.repository.*;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.DynamicCrConfigTranslatableService;
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
public class DynamicCrConfigTranslatableServiceImpl implements DynamicCrConfigTranslatableService {
    @Inject
    DynamicCrConfigTranslatableRepository dynamicCrConfigTranslatableRepository;
    @Inject
    DynamicCrConfigurationRepository dynamicCrConfigRepository;

    public static DynamicCrConfigTranslatableDto mapToDto(DynamicCrConfigTranslatableEntity dynamicCrConfigTranslatable) {
        return new DynamicCrConfigTranslatableDto(
                dynamicCrConfigTranslatable.getId(),
                dynamicCrConfigTranslatable.getLabel(),
                dynamicCrConfigTranslatable.getCodeLanguage(),
                dynamicCrConfigTranslatable.getDynamicCrConfiguration().getId()
        );
    }

    @Override
    public MessageResponse create(DynamicCrConfigTranslatableDto dynamicCrConfigTranslatableDto) {
        log.debug("Request to create dynamic cr configuration translatable : {}", dynamicCrConfigTranslatableDto);

        var dynamicCrConfigId = dynamicCrConfigTranslatableDto.getDynamicCrConfiguration_id();
        var dynamicCrConfig = this.dynamicCrConfigRepository.findById(dynamicCrConfigId)
                .orElseThrow(() ->
                        new IllegalStateException("The dynamic cr configId with ID[" + dynamicCrConfigId + "] was not found !"));

        DynamicCrConfigTranslatableEntity dynamicCrConfigTranslatable = new DynamicCrConfigTranslatableEntity(
                dynamicCrConfigTranslatableDto.getLabel(),
                dynamicCrConfigTranslatableDto.getCodeLanguage(),
                dynamicCrConfig
        );

        this.dynamicCrConfigTranslatableRepository.save(dynamicCrConfigTranslatable);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(dynamicCrConfigTranslatable)
        );
    }

    @Override
    public MessageResponse update(DynamicCrConfigTranslatableDto dynamicCrConfigTranslatableDto) {
        log.debug("Request to update dynamic cr configuration : {}", dynamicCrConfigTranslatableDto);

        boolean existe = dynamicCrConfigRepository.existsById(dynamicCrConfigTranslatableDto.getId());
        if (!existe){
            var dynamicCrConfigId = dynamicCrConfigTranslatableDto.getDynamicCrConfiguration_id();
            var dynamicCrConfig = this.dynamicCrConfigRepository.findById(dynamicCrConfigId)
                    .orElseThrow(() ->
                            new IllegalStateException("The dynamic cr configId with ID[" + dynamicCrConfigId + "] was not found !"));

            DynamicCrConfigTranslatableEntity dynamicCrConfigTranslatable = new DynamicCrConfigTranslatableEntity(
                    dynamicCrConfigTranslatableDto.getId(),
                    Instant.now(),
                    dynamicCrConfigTranslatableDto.getLabel(),
                    dynamicCrConfigTranslatableDto.getCodeLanguage(),
                    dynamicCrConfig
            );

            this.dynamicCrConfigTranslatableRepository.save(dynamicCrConfigTranslatable);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(dynamicCrConfigTranslatable)
            );
        } else {
            throw new IllegalStateException("There is already an affected role");
        }
    }

    @Override
    public DynamicCrConfigTranslatableDto findById(Long id) {
        log.debug("Request to get dynamic cr config : {}", id);
        return this.dynamicCrConfigTranslatableRepository.findById(id).map(DynamicCrConfigTranslatableServiceImpl::mapToDto).orElse(null);
    }

    public List<DynamicCrConfigTranslatableDto> findAll() {
        log.debug("Request to get all affected role");
        return this.dynamicCrConfigTranslatableRepository.findAll()
                .stream()
                .map(DynamicCrConfigTranslatableServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete contract : {}", id);

        var affectedRole = this.dynamicCrConfigTranslatableRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find dynamic cr config translatable with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.dynamicCrConfigTranslatableRepository.delete(affectedRole);
    }
}