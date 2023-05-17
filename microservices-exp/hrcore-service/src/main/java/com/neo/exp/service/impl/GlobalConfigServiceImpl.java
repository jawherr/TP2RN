package com.neo.exp.service.impl;

import com.neo.exp.domain.GlobalConfigEntity;
import com.neo.exp.dto.GlobalConfigDto;
import com.neo.exp.repository.GlobalConfigRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.GlobalConfigService;
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
public class GlobalConfigServiceImpl implements GlobalConfigService {
    @Inject
    GlobalConfigRepository globalConfigRepository;

    public static GlobalConfigDto mapToDto(GlobalConfigEntity globalConfig){
        return new GlobalConfigDto(
                globalConfig.getId(),
                globalConfig.getTenant(),
                globalConfig.getLogo()
        );
    }

    @Override
    public MessageResponse create(GlobalConfigDto globalConfigDto) {
        log.debug("Request to create global config : {}", globalConfigDto);

        boolean existe = globalConfigRepository.existsById(globalConfigDto.getId());
        if (!existe){
            GlobalConfigEntity globalConfig = new GlobalConfigEntity(
                    globalConfigDto.getTenant(),
                    globalConfigDto.getLogo()
            );

            this.globalConfigRepository.save(globalConfig);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(globalConfig)
            );
        } else {
            throw new IllegalStateException("There is already an folder");
        }
    }

    @Override
    public MessageResponse update(GlobalConfigDto globalConfigDto) {
        log.debug("Request to update global config : {}", globalConfigDto);

        boolean existe = globalConfigRepository.existsById(globalConfigDto.getId());
        if (existe){
            GlobalConfigEntity globalConfig = new GlobalConfigEntity(
                    globalConfigDto.getId(),
                    Instant.now(),
                    globalConfigDto.getTenant(),
                    globalConfigDto.getLogo()
            );

            this.globalConfigRepository.save(globalConfig);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(globalConfig)
            );
        } else {
            throw new IllegalStateException("There is already an folder");
        }
    }
    @Override
    public GlobalConfigDto findById(Long id) {
        log.debug("Request to get global config : {}", id);
        return this.globalConfigRepository.findById(id).map(GlobalConfigServiceImpl::mapToDto).orElse(null);
    }
    @Override
    public List<GlobalConfigDto> findAll() {
        log.debug("Request to get all global config");
        return this.globalConfigRepository.findAll()
                .stream()
                .map(GlobalConfigServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        log.debug("Request to delete global config : {}", id);

        var globalConfig = this.globalConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find global config with id " + id));

        this.globalConfigRepository.delete(globalConfig);

    }
}
