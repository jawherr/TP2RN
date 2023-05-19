package com.neo.exp.service.impl;

import com.neo.exp.domain.AffectedRoleEntity;
import com.neo.exp.dto.AffectedRoleDto;
import com.neo.exp.repository.AffectedRoleRepository;
import com.neo.exp.repository.RoleRepository;
import com.neo.exp.repository.UserRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.AffectedRoleService;
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
public class AffectedRoleServiceImpl implements AffectedRoleService{
    @Inject
    AffectedRoleRepository affectedRoleRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    RoleRepository roleRepository;

    public static AffectedRoleDto mapToDto(AffectedRoleEntity affectedRole) {
        return new AffectedRoleDto(
                affectedRole.getId(),
                affectedRole.getUser().getId(),
                affectedRole.getStartingDate(),
                affectedRole.getEndingDate(),
                affectedRole.getRole().getId()
        );
    }

    @Override
    public MessageResponse create(AffectedRoleDto affectedRoleDto) {
        log.debug("Request to create affected role : {}", affectedRoleDto);

        var userId = affectedRoleDto.getUser_id();
        var user = this.userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalStateException("The user with ID[" + userId + "] was not found !"));

        var roleId = affectedRoleDto.getRole_id();
        var role = this.roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalStateException("The role with ID[" + roleId + "] was not found !"));

        AffectedRoleEntity affectedRole = new AffectedRoleEntity(
                affectedRoleDto.getId(),
                Instant.now(),
                user,
                affectedRoleDto.getStartingDate(),
                affectedRoleDto.getEndingDate(),
                Collections.emptySet(),
                Collections.emptySet(),
                role
        );

        this.affectedRoleRepository.save(affectedRole);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(affectedRole)
        );
    }

    @Override
    public MessageResponse update(AffectedRoleDto affectedRoleDto) {
        log.debug("Request to update affected role : {}", affectedRoleDto);

        boolean existe = roleRepository.existsById(affectedRoleDto.getId());
        if (existe){
            var userId = affectedRoleDto.getUser_id();
            var user = this.userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalStateException("The user with ID[" + userId + "] was not found !"));

            var roleId = affectedRoleDto.getRole_id();
            var role = this.roleRepository.findById(roleId)
                    .orElseThrow(() ->
                            new IllegalStateException("The role with ID[" + roleId + "] was not found !"));

            AffectedRoleEntity affectedRole = new AffectedRoleEntity(
                    user,
                    affectedRoleDto.getStartingDate(),
                    affectedRoleDto.getEndingDate(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    role
            );

            this.affectedRoleRepository.save(affectedRole);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(affectedRole)
            );
        } else {
            throw new IllegalStateException("There is already an affected role");
        }
    }

    @Override
    public AffectedRoleDto findById(Long id) {
        log.debug("Request to get affected role : {}", id);
        return this.affectedRoleRepository.findById(id).map(AffectedRoleServiceImpl::mapToDto).orElse(null);
    }

    public List<AffectedRoleDto> findAll() {
        log.debug("Request to get all affected role");
        return this.affectedRoleRepository.findAll()
                .stream()
                .map(AffectedRoleServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete affected role : {}", id);

        var affectedRole = this.affectedRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find affected role with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.affectedRoleRepository.delete(affectedRole);
    }
}