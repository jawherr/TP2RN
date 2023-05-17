package com.neo.exp.service.impl;

import com.neo.exp.domain.MediaEntity;
import com.neo.exp.dto.MediaDto;
import com.neo.exp.repository.FolderRepository;
import com.neo.exp.repository.MediaRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.MediaService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class MediaServiceImpl implements MediaService {
    @Inject
    MediaRepository mediaRepository;
    @Inject
    FolderRepository folderRepository;
    public static MediaDto mapToDto(MediaEntity media) {
        return new MediaDto(
                media.getId(),
                media.getFolder().getId(),
                media.getExtension(),
                media.getLink()
        );
    }
    @Override
    public MessageResponse create(MediaDto mediaDto) {
        log.debug("Request to create media : {}", mediaDto);

        boolean existe = folderRepository.existsById(mediaDto.getId());
        if (!existe){
            var folderId = mediaDto.getFolder_id();
            var folder = this.folderRepository.findById(folderId)
                    .orElseThrow(() ->
                            new IllegalStateException("The folder with ID[" + folderId + "] was not found !"));

            MediaEntity media = new MediaEntity(
                    folder,
                    mediaDto.getExtension(),
                    mediaDto.getLink()
            );

            this.mediaRepository.save(media);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(media)
            );
        } else {
            throw new IllegalStateException("There is already an media");
        }
    }

    @Override
    public MessageResponse update(MediaDto mediaDto) {
        log.debug("Request to update media : {}", mediaDto);

        boolean existe = folderRepository.existsById(mediaDto.getId());
        if (existe){
            var folderId = mediaDto.getFolder_id();
            var folder = this.folderRepository.findById(folderId)
                    .orElseThrow(() ->
                            new IllegalStateException("The folder with ID[" + folderId + "] was not found !"));

            MediaEntity media = new MediaEntity(
                    mediaDto.getId(),
                    Instant.now(),
                    folder,
                    mediaDto.getExtension(),
                    mediaDto.getLink()
            );

            this.mediaRepository.save(media);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(media)
            );
        } else {
            throw new IllegalStateException("There is already an media");
        }
    }
    @Override
    public MediaDto findById(Long id) {
        log.debug("Request to get media : {}", id);
        return this.mediaRepository.findById(id).map(MediaServiceImpl::mapToDto).orElse(null);
    }
    @Override
    public List<MediaDto> findAll() {
        log.debug("Request to get all media");
        return this.mediaRepository.findAll()
                .stream()
                .map(MediaServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        log.debug("Request to delete media : {}", id);

        var media = this.mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find media with id " + id));

        Optional.ofNullable(media.getFolder()).ifPresent(folderRepository::delete);

        this.mediaRepository.delete(media);
    }

}
