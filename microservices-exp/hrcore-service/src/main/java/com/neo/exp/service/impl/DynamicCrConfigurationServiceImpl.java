package com.neo.exp.service.impl;

import com.neo.exp.domain.DynamicCrConfigurationEntity;
import com.neo.exp.dto.DynamicCrConfigurationDto;
import com.neo.exp.repository.DynamicCrConfigurationRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.DynamicCrConfigurationService;
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
public class DynamicCrConfigurationServiceImpl implements DynamicCrConfigurationService {
    @Inject
    DynamicCrConfigurationRepository dynamicCrConfigurationRepository;

    public static DynamicCrConfigurationDto mapToDto(DynamicCrConfigurationEntity dynamicCrConfiguration) {
        return new DynamicCrConfigurationDto(
                dynamicCrConfiguration.getId(),
                dynamicCrConfiguration.getCode(),
                dynamicCrConfiguration.getName(),
                dynamicCrConfiguration.getModule(),
                dynamicCrConfiguration.getTenant(),
                dynamicCrConfiguration.getCanAdd()
        );
    }

    @Override
    public MessageResponse create(DynamicCrConfigurationDto dynamicCrConfigurationDto) {
        log.debug("Request to create dynamic cr configuration : {}", dynamicCrConfigurationDto);

        DynamicCrConfigurationEntity dynamicCrConfiguration = new DynamicCrConfigurationEntity(
                dynamicCrConfigurationDto.getCode(),
                dynamicCrConfigurationDto.getName(),
                dynamicCrConfigurationDto.getModule(),
                dynamicCrConfigurationDto.getTenant(),
                dynamicCrConfigurationDto.getCanAdd(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        this.dynamicCrConfigurationRepository.save(dynamicCrConfiguration);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(dynamicCrConfiguration)
        );
    }

    @Override
    public MessageResponse update(DynamicCrConfigurationDto dynamicCrConfigurationDto) {
        log.debug("Request to update dynamic cr configuration : {}", dynamicCrConfigurationDto);

        boolean existe = dynamicCrConfigurationRepository.existsById(dynamicCrConfigurationDto.getId());
        if (!existe){
            DynamicCrConfigurationEntity dynamicCrConfiguration = new DynamicCrConfigurationEntity(
                    dynamicCrConfigurationDto.getId(),
                    Instant.now(),
                    dynamicCrConfigurationDto.getCode(),
                    dynamicCrConfigurationDto.getName(),
                    dynamicCrConfigurationDto.getModule(),
                    dynamicCrConfigurationDto.getTenant(),
                    dynamicCrConfigurationDto.getCanAdd(),
                    Collections.emptySet(),
                    Collections.emptySet()
            );

            this.dynamicCrConfigurationRepository.save(dynamicCrConfiguration);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(dynamicCrConfiguration)
            );
        } else {
            throw new IllegalStateException("There is already an affected role");
        }
    }

    @Override
    public DynamicCrConfigurationDto findById(Long id) {
        log.debug("Request to get dynamic cr config : {}", id);
        return this.dynamicCrConfigurationRepository.findById(id).map(DynamicCrConfigurationServiceImpl::mapToDto).orElse(null);
    }

    public List<DynamicCrConfigurationDto> findAll() {
        log.debug("Request to get all affected role");
        return this.dynamicCrConfigurationRepository.findAll()
                .stream()
                .map(DynamicCrConfigurationServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete contract : {}", id);

        var affectedRole = this.dynamicCrConfigurationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find dynamic cr config with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.dynamicCrConfigurationRepository.delete(affectedRole);
    }
}