package com.neo.exp.service.impl;

import com.neo.exp.domain.UserEntity;
import com.neo.exp.domain.enums.Status;
import com.neo.exp.dto.UserDto;
import com.neo.exp.repository.GlobalConfigRepository;
import com.neo.exp.repository.OrganizationRepository;
import com.neo.exp.repository.UserRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.UserService;
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
public class UserServiceImpl implements UserService {
    @Inject
    UserRepository userRepository;
    @Inject
    GlobalConfigRepository globalConfigRepository;
    @Inject
    OrganizationRepository organizationRepository;

    public static UserDto mapToDto(UserEntity user){
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRegistrationNumber(),
                user.getTypeEffectiveDate(),
                user.getProfilePicture(),
                user.getBirthday(),
                user.getNationality(),
                user.getIdentityCard(),
                user.getSocialSecurity(),
                user.getAboutMe(),
                user.getCode(),
                user.getLabel(),
                user.getTenant(),
                user.getStatus(),
                user.getGlobalConfig().getId(),
                user.getOrganization().getId(),
                user.getAddress(),
                user.getContactInfo()
        );
    }

    @Override
    public MessageResponse create(UserDto userDto) {
        log.debug("Request to create user : {}", userDto);

        boolean existe = userRepository.existsByCode(userDto.getCode());
        if (!existe){
            var globalConfigId = userDto.getGlobalConfig_id();
            var globalConfig = this.globalConfigRepository.findById(globalConfigId)
                    .orElseThrow(() ->
                            new IllegalStateException("The global config with ID[" + globalConfigId + "] was not found !"));

            var organizationId = userDto.getOrganization_id();
            var organization = this.organizationRepository.findById(organizationId)
                    .orElseThrow(() ->
                            new IllegalStateException("The organization with ID[" + organizationId + "] was not found !"));
            UserEntity user = new UserEntity(
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getRegistrationNumber(),
                    userDto.getTypeEffectiveDate(),
                    userDto.getProfilePicture(),
                    userDto.getBirthday(),
                    userDto.getNationality(),
                    userDto.getIdentityCard(),
                    userDto.getSocialSecurity(),
                    userDto.getAboutMe(),
                    userDto.getCode(),
                    userDto.getLabel(),
                    userDto.getTenant(),
                    userDto.getStatus(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    globalConfig,
                    organization,
                    userDto.getAddress(),
                    userDto.getContactInfo(),
                    Collections.emptySet()
            );
            return new MessageResponse(
                    true,
                    "Success",
                    mapToDto(user)
            );
        } else {
            throw new IllegalStateException("There is already an status History");
        }
    }

    @Override
    public MessageResponse update(UserDto userDto) {
        log.debug("Request to update user : {}", userDto);

        boolean existe = userRepository.existsById(userDto.getId());
        if (existe){
            var globalConfigId = userDto.getGlobalConfig_id();
            var globalConfig = this.globalConfigRepository.findById(globalConfigId)
                    .orElseThrow(() ->
                            new IllegalStateException("The global config with ID[" + globalConfigId + "] was not found !"));

            var organizationId = userDto.getOrganization_id();
            var organization = this.organizationRepository.findById(organizationId)
                    .orElseThrow(() ->
                            new IllegalStateException("The organization with ID[" + organizationId + "] was not found !"));
            UserEntity user = new UserEntity(
                    userDto.getId(),
                    Instant.now(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getRegistrationNumber(),
                    userDto.getTypeEffectiveDate(),
                    userDto.getProfilePicture(),
                    userDto.getBirthday(),
                    userDto.getNationality(),
                    userDto.getIdentityCard(),
                    userDto.getSocialSecurity(),
                    userDto.getAboutMe(),
                    userDto.getCode(),
                    userDto.getLabel(),
                    userDto.getTenant(),
                    userDto.getStatus(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    globalConfig,
                    organization,
                    userDto.getAddress(),
                    userDto.getContactInfo(),
                    Collections.emptySet()
            );
            return new MessageResponse(
                    true,
                    "Success",
                    mapToDto(user)
            );
        } else {
            throw new IllegalStateException("There is already an status History");
        }
    }

    @Override
    public UserDto findById(Long id) {
        log.debug("Request to get user : {}", id);
        return this.userRepository.findById(id).map(UserServiceImpl::mapToDto).orElse(null);
    }

    @Override
    public List<UserDto> findAll() {
        log.debug("Request to get all user");
        return this.userRepository.findAll()
                .stream()
                .map(UserServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete user : {}", id);

        var user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find user with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);
        user.setStatus(Status.DISABLED);
        this.userRepository.save(user);
        //this.userRepository.delete(user);
    }
}
