package com.neo.exp.service.impl;

import com.neo.exp.domain.FunctionEntity;
import com.neo.exp.dto.FunctionDto;
import com.neo.exp.repository.FunctionRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FunctionService;
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
public class FunctionServiceImpl implements FunctionService {
    @Inject
    FunctionRepository functionRepository;

    public static FunctionDto mapToDto(FunctionEntity function) {
        return new FunctionDto(
                function.getId(),
                function.getCode(),
                function.getLabel(),
                function.getDescription(),
                function.getTenant()
        );
    }

    @Override
    public MessageResponse create(FunctionDto functionDto) {
        log.debug("Request to create function : {}", functionDto);

        FunctionEntity function = new FunctionEntity(
                functionDto.getCode(),
                functionDto.getLabel(),
                functionDto.getDescription(),
                functionDto.getTenant(),
                Collections.emptySet()
        );

        this.functionRepository.save(function);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(function)
        );
    }

    @Override
    public MessageResponse update(FunctionDto functionDto) {
        log.debug("Request to update function : {}", functionDto);

        boolean existe = functionRepository.existsById(functionDto.getId());
        if (existe){
            FunctionEntity function = new FunctionEntity(
                    functionDto.getId(),
                    Instant.now(),
                    functionDto.getCode(),
                    functionDto.getLabel(),
                    functionDto.getDescription(),
                    functionDto.getTenant(),
                    Collections.emptySet()
            );

            this.functionRepository.save(function);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(function)
            );
        } else {
            throw new IllegalStateException("There is already an function");
        }
    }

    @Override
    public FunctionDto findById(Long id) {
        log.debug("Request to get function : {}", id);
        return this.functionRepository.findById(id).map(FunctionServiceImpl::mapToDto).orElse(null);
    }

    public List<FunctionDto> findAll() {
        log.debug("Request to get all function");
        return this.functionRepository.findAll()
                .stream()
                .map(FunctionServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete function : {}", id);

        var function = this.functionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find function with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.functionRepository.delete(function);
    }
}