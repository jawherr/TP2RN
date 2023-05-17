package com.neo.exp.service.impl;

import com.neo.exp.domain.FolderEntity;
import com.neo.exp.dto.FolderDto;
import com.neo.exp.repository.FolderRepository;
import com.neo.exp.repository.UserRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FolderService;
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
public class FolderServiceImpl implements FolderService {
    @Inject
    FolderRepository folderRepository;
    @Inject
    UserRepository userRepository;

    public static FolderDto mapToDto(FolderEntity folder) {
        return new FolderDto(
                folder.getId(),
                folder.getCode(),
                folder.getLabel(),
                folder.getTenant(),
                folder.getUser().getId()
        );
    }
    @Override
    public MessageResponse create(FolderDto folderDto) {
        log.debug("Request to create folder : {}", folderDto);

        boolean existe = folderRepository.existsById(folderDto.getId());
        if (!existe){
            var userId = folderDto.getUserId();
            var user = this.userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalStateException("The user with ID[" + userId + "] was not found !"));

            FolderEntity folder = new FolderEntity(
                    user,
                    folderDto.getCode(),
                    folderDto.getLabel(),
                    folderDto.getTenant(),
                    Collections.emptySet()
            );

            this.folderRepository.save(folder);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(folder)
            );
        } else {
            throw new IllegalStateException("There is already an folder");
        }
    }
    @Override
    public MessageResponse update(FolderDto folderDto) {
        log.debug("Request to update folder : {}", folderDto);

        boolean existe = folderRepository.existsByCode(folderDto.getCode());
        if (existe){
            var userId = folderDto.getUserId();
            var user = this.userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalStateException("The user with ID[" + userId + "] was not found !"));

            FolderEntity folder = new FolderEntity(
                    folderDto.getId(),
                    Instant.now(),
                    user,
                    folderDto.getCode(),
                    folderDto.getLabel(),
                    folderDto.getTenant(),
                    Collections.emptySet()
            );

            this.folderRepository.save(folder);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(folder)
            );
        } else {
            throw new IllegalStateException("There is no already an folder");
        }
    }
    @Override
    public FolderDto findById(Long id) {
        log.debug("Request to get folder : {}", id);
        return this.folderRepository.findById(id).map(FolderServiceImpl::mapToDto).orElse(null);
    }
    @Override
    public List<FolderDto> findAll() {
        log.debug("Request to get all folder");

        return this.folderRepository.findAll()
                .stream()
                .map(FolderServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Folder : {}", id);
        var folder = this.folderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find folder with id " + id));

        this.folderRepository.delete(folder);
    }
}
