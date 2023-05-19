package com.neo.exp.service.impl;

import com.neo.exp.domain.DocumentEntity;
import com.neo.exp.dto.DocumentDto;
import com.neo.exp.repository.*;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.DocumentService;
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
public class DocumentServiceImpl implements DocumentService {
    @Inject
    DocumentRepository documentRepository;
    @Inject
    FunctionRepository functionRepository;

    public static DocumentDto mapToDto(DocumentEntity document) {
        return new DocumentDto(
                document.getId(),
                document.getContent(),
                document.getExtension(),
                document.getFunction().getId()
        );
    }

    @Override
    public MessageResponse create(DocumentDto documentDto) {
        log.debug("Request to create document : {}", documentDto);

        var functionId = documentDto.getFunction_id();
        var function = this.functionRepository.findById(functionId)
                .orElseThrow(() ->
                        new IllegalStateException("The function with ID[" + functionId + "] was not found !"));

        DocumentEntity document = new DocumentEntity(
                documentDto.getContent(),
                documentDto.getExtension(),
                function
        );

        this.functionRepository.save(function);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(document)
        );
    }

    @Override
    public MessageResponse update(DocumentDto documentDto) {
        log.debug("Request to update document : {}", documentDto);

        boolean existe = documentRepository.existsById(documentDto.getId());
        if (existe){
            var functionId = documentDto.getFunction_id();
            var function = this.functionRepository.findById(functionId)
                    .orElseThrow(() ->
                            new IllegalStateException("The function with ID[" + functionId + "] was not found !"));

            DocumentEntity document = new DocumentEntity(
                    documentDto.getId(),
                    Instant.now(),
                    documentDto.getContent(),
                    documentDto.getExtension(),
                    function
            );

            this.functionRepository.save(function);

            return new MessageResponse(
                    true,
                    "Success",
                    mapToDto(document)
            );
        } else {
            throw new IllegalStateException("There is already an document");
        }
    }

    @Override
    public DocumentDto findById(Long id) {
        log.debug("Request to get document : {}", id);
        return this.documentRepository.findById(id).map(DocumentServiceImpl::mapToDto).orElse(null);
    }

    public List<DocumentDto> findAll() {
        log.debug("Request to get all document");
        return this.documentRepository.findAll()
                .stream()
                .map(DocumentServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete document : {}", id);

        var document = this.documentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find document with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.documentRepository.delete(document);
    }
}