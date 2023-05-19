package com.neo.exp.service.impl;

import com.neo.exp.domain.PermissionEntity;
import com.neo.exp.dto.PermissionDto;
import com.neo.exp.repository.PermissionRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.PermissionService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Inject
    PermissionRepository permissionRepository;
    public static PermissionDto mapToDto(PermissionEntity permission) {
        return new PermissionDto(
                permission.getId(),
                permission.getCode(),
                permission.getLabel(),
                permission.getHasChildren(),
                permission.getChild_permission().getId()
        );
    }
    @Override
    public MessageResponse create(PermissionDto permissionDto) {
        log.debug("Request to create permission : {}", permissionDto);

        var permissionId = permissionDto.getChild_permission_id();
        var permission = this.permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new IllegalStateException("The permission with ID[" + permissionId + "] was not found !"));

        PermissionEntity permissionEntity = new PermissionEntity(
                permissionDto.getLabel(),
                permissionDto.getCode(),
                permissionDto.getHasChildren(),
                Collections.emptySet(),
                permission,
                Collections.emptySet()
        );

        this.permissionRepository.save(permission);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(permission)
        );
    }

    @Override
    public MessageResponse update(PermissionDto permissionDto) {
        log.debug("Request to update permission : {}", permissionDto);

        boolean existe = permissionRepository.existsByCode(permissionDto.getCode());
        if (existe){
            var permissionId = permissionDto.getChild_permission_id();
            var permission = this.permissionRepository.findById(permissionId)
                    .orElseThrow(() ->
                            new IllegalStateException("The permission with ID[" + permissionId + "] was not found !"));

            PermissionEntity permissionEntity = new PermissionEntity(
                    permissionDto.getId(),
                    Instant.now(),
                    permissionDto.getLabel(),
                    permissionDto.getCode(),
                    permissionDto.getHasChildren(),
                    Collections.emptySet(),
                    permission,
                    Collections.emptySet()
            );

            this.permissionRepository.save(permission);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(permission)
            );
        } else {
            throw new IllegalStateException("There is already an permission");
        }
    }
    @Override
    public PermissionDto findById(Long id) {
        log.debug("Request to get permission : {}", id);
        return this.permissionRepository.findById(id).map(PermissionServiceImpl::mapToDto).orElse(null);
    }
    @Override
    public List<PermissionDto> findAll() {
        log.debug("Request to get all permission");
        return this.permissionRepository.findAll()
                .stream()
                .map(PermissionServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        log.debug("Request to delete permission : {}", id);

        var permission = this.permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find permission with id " + id));
        Optional.ofNullable(permission.getChild_permission()).ifPresent(permissionRepository::delete);

        this.permissionRepository.delete(permission);
    }
}
