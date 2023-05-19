package com.neo.exp.service.impl;

import com.neo.exp.domain.RoleEntity;
import com.neo.exp.dto.RoleDto;
import com.neo.exp.repository.RoleRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.RoleService;
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
public class RoleServiceImpl implements RoleService {
    @Inject
    RoleRepository roleRepository;

    public static RoleDto mapToDto(RoleEntity role){
        return new RoleDto(
                role.getId(),
                role.getLabel(),
                role.getVisibility(),
                role.getTenant()
        );
    }
    @Override
    public MessageResponse create(RoleDto roleDto) {
        log.debug("Request to create role : {}", roleDto);

        RoleEntity role= new RoleEntity(
                Collections.emptySet(),
                roleDto.getLabel(),
                roleDto.getVisibility(),
                roleDto.getTenant(),
                Collections.emptySet()
        );
        this.roleRepository.save(role);

        return new MessageResponse(
                true,
                "Success",
                mapToDto(role)
        );
    }
    @Override
    public MessageResponse update(RoleDto roleDto) {
        log.debug("Request to update role : {}", roleDto);

        boolean existe = roleRepository.existsById(roleDto.getId());
        if (existe){
            RoleEntity role= new RoleEntity(
                    roleDto.getId(),
                    Instant.now(),
                    Collections.emptySet(),
                    roleDto.getLabel(),
                    roleDto.getVisibility(),
                    roleDto.getTenant(),
                    Collections.emptySet()
            );
            this.roleRepository.save(role);

            return new MessageResponse(
                    true,
                    "Success",
                    mapToDto(role)
            );
        } else {
            throw new IllegalStateException("There is already an role");
        }
    }
    @Override
    public RoleDto findById(Long id) {
        log.debug("Request to get role : {}", id);
        return this.roleRepository.findById(id).map(RoleServiceImpl::mapToDto).orElse(null);
    }
    @Override
    public List<RoleDto> findAll() {
        log.debug("Request to get all role");
        return this.roleRepository.findAll()
                .stream()
                .map(RoleServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        log.debug("Request to delete role : {}", id);

        var role = this.roleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find role with id " + id));

        this.roleRepository.delete(role);
    }
}
