package com.neo.exp.service.impl;

import com.neo.exp.domain.StatusHistoryEntity;
import com.neo.exp.dto.StatusHistoryDto;
import com.neo.exp.repository.StatusHistoryRepository;
import com.neo.exp.repository.UserRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.StatusHistoryService;
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
public class StatusHistoryServiceImpl implements StatusHistoryService {
    @Inject
    StatusHistoryRepository statusHistoryRepository;
    @Inject
    UserRepository userRepository;
    public static StatusHistoryDto mapToDto(StatusHistoryEntity statusHistory) {
        return new StatusHistoryDto(
                statusHistory.getId(),
                statusHistory.getAffectationEffectiveDate(),
                statusHistory.getAffectationEndDate(),
                statusHistory.getStatusCode(),
                statusHistory.getUser().getId()
        );
    }
    @Override
    public MessageResponse create(StatusHistoryDto statusHistoryDto) {
        log.debug("Request to create status history : {}", statusHistoryDto);

        boolean existe = statusHistoryRepository.existsById(statusHistoryDto.getId());
        if (!existe){
            var userId = statusHistoryDto.getUser_id();
            var user = this.userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalStateException("The user with ID[" + userId + "] was not found !"));

            StatusHistoryEntity statusHistory = new StatusHistoryEntity(
                    statusHistoryDto.getAffectationEffectiveDate(),
                    statusHistoryDto.getAffectationEndDate(),
                    statusHistoryDto.getStatusCode(),
                    user
            );
            this.statusHistoryRepository.save(statusHistory);

            return new MessageResponse(
                    true,
                    "Success",
                    mapToDto(statusHistory)
            );
        } else {
            throw new IllegalStateException("There is already an status History");
        }
    }

    @Override
    public MessageResponse update(StatusHistoryDto statusHistoryDto) {
        log.debug("Request to update status history : {}", statusHistoryDto);

        boolean existe = statusHistoryRepository.existsById(statusHistoryDto.getId());
        if (existe){
            var userId = statusHistoryDto.getUser_id();
            var user = this.userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalStateException("The user with ID[" + userId + "] was not found !"));

            StatusHistoryEntity statusHistory = new StatusHistoryEntity(
                    statusHistoryDto.getId(),
                    Instant.now(),
                    statusHistoryDto.getAffectationEffectiveDate(),
                    statusHistoryDto.getAffectationEndDate(),
                    statusHistoryDto.getStatusCode(),
                    user
            );
            this.statusHistoryRepository.save(statusHistory);

            return new MessageResponse(
                    true,
                    "Success",
                    mapToDto(statusHistory)
            );
        } else {
            throw new IllegalStateException("There is already an status History");
        }
    }
    @Override
    public StatusHistoryDto findById(Long id) {
        log.debug("Request to get status history : {}", id);
        return this.statusHistoryRepository.findById(id).map(StatusHistoryServiceImpl::mapToDto).orElse(null);
    }
    @Override
    public List<StatusHistoryDto> findAll() {
        log.debug("Request to get all status history");
        return this.statusHistoryRepository.findAll()
                .stream()
                .map(StatusHistoryServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        log.debug("Request to delete status history : {}", id);

        var statusHistory = this.statusHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find status history with id " + id));

        //Optional.ofNullable(media.getFolder()).ifPresent(folderRepository::delete);

        this.statusHistoryRepository.delete(statusHistory);
    }
    }
