package com.neo.exp.service.impl;

import com.neo.exp.domain.CrContentTranslatableEntity;
import com.neo.exp.dto.CrContentTranslatableDto;
import com.neo.exp.repository.CrContentRepository;
import com.neo.exp.repository.CrContentTranslatableRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.CrContentTranslatableService;
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
public class CrContentTranslatableServiceImpl implements CrContentTranslatableService {
    @Inject
    CrContentTranslatableRepository crContentTranslatableRepository;

    @Inject
    CrContentRepository crContentRepository;

    public static CrContentTranslatableDto mapToDto(CrContentTranslatableEntity crContentTranslatable) {
        return new CrContentTranslatableDto(
                crContentTranslatable.getId(),
                crContentTranslatable.getCode(),
                crContentTranslatable.getName(),
                crContentTranslatable.getModule(),
                crContentTranslatable.getTenant(),
                crContentTranslatable.getCanAdd(),
                crContentTranslatable.getCrContent().getId()
        );
    }

    @Override
    public MessageResponse create(CrContentTranslatableDto crContentTranslatableDto) {
        log.debug("Request to create cr content translatable : {}", crContentTranslatableDto);

        var crContentId = crContentTranslatableDto.getCrContent_id();
        var crContent = this.crContentRepository.findById(crContentId)
                .orElseThrow(() ->
                        new IllegalStateException("The new cr content with ID[" + crContentId + "] was not found !"));

        CrContentTranslatableEntity crContentTranslatable = new CrContentTranslatableEntity(
                crContentTranslatableDto.getCode(),
                crContentTranslatableDto.getName(),
                crContentTranslatableDto.getModule(),
                crContentTranslatableDto.getTenant(),
                crContentTranslatableDto.getCanAdd(),
                crContent
        );

        this.crContentRepository.save(crContent);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(crContentTranslatable)
        );
    }

    @Override
    public MessageResponse update(CrContentTranslatableDto crContentTranslatableDto) {
        log.debug("Request to update cr cr content translatable : {}", crContentTranslatableDto);

        boolean existe = crContentRepository.existsById(crContentTranslatableDto.getId());
        if (existe){
            var crContentId = crContentTranslatableDto.getCrContent_id();
            var crContent = this.crContentRepository.findById(crContentId)
                    .orElseThrow(() ->
                            new IllegalStateException("The new cr content with ID[" + crContentId + "] was not found !"));

            CrContentTranslatableEntity crContentTranslatable = new CrContentTranslatableEntity(
                    crContentTranslatableDto.getId(),
                    Instant.now(),
                    crContentTranslatableDto.getCode(),
                    crContentTranslatableDto.getName(),
                    crContentTranslatableDto.getModule(),
                    crContentTranslatableDto.getTenant(),
                    crContentTranslatableDto.getCanAdd(),
                    crContent
            );

            this.crContentRepository.save(crContent);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(crContentTranslatable)
            );
        } else {
            throw new IllegalStateException("There is already an cr content translatable");
        }
    }

    @Override
    public CrContentTranslatableDto findById(Long id) {
        log.debug("Request to get cr content translatable : {}", id);
        return this.crContentTranslatableRepository.findById(id).map(CrContentTranslatableServiceImpl::mapToDto).orElse(null);
    }

    public List<CrContentTranslatableDto> findAll() {
        log.debug("Request to get all cr content translatable");
        return this.crContentTranslatableRepository.findAll()
                .stream()
                .map(CrContentTranslatableServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete cr content translatable : {}", id);

        var crContentTranslatable = this.crContentTranslatableRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find cr content translatable with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.crContentTranslatableRepository.delete(crContentTranslatable);
    }
}