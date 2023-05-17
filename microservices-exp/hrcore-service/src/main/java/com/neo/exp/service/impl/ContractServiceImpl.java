package com.neo.exp.service.impl;

import com.neo.exp.domain.ContractEntity;
import com.neo.exp.dto.ContractDto;
import com.neo.exp.repository.ContractRepository;
import com.neo.exp.repository.CrContentRepository;
import com.neo.exp.repository.UserRepository;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.ContractService;
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
public class ContractServiceImpl implements ContractService {
    @Inject
    ContractRepository contractRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    CrContentRepository crContentRepository;

    public static ContractDto mapToDto(ContractEntity contract) {
        return new ContractDto(
                contract.getId(),
                contract.getContractStartDate(),
                contract.getContractEndTrialPeriod(),
                contract.getLeaveType(),
                contract.getContractEndDate(),
                contract.getLeaveReason(),
                contract.getObservation(),
                contract.getStatus(),
                contract.getUser().getId(),
                contract.getCrContent().getId()
        );
    }

    @Override
    public MessageResponse create(ContractDto contractDto) {
        log.debug("Request to create contract : {}", contractDto);

        var userId = contractDto.getUser_id();
        var user = this.userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalStateException("The user with ID[" + userId + "] was not found !"));

        var crContentId = contractDto.getCrContent_id();
        var crContent = this.crContentRepository.findById(crContentId)
                .orElseThrow(() ->
                        new IllegalStateException("The cr content with ID[" + crContentId + "] was not found !"));

        ContractEntity contract = new ContractEntity(
                contractDto.getContractStartDate(),
                contractDto.getContractEndTrialPeriod(),
                contractDto.getLeaveType(),
                contractDto.getContractEndDate(),
                contractDto.getLeaveReason(),
                contractDto.getObservation(),
                contractDto.getStatus(),
                user,
                crContent
        );

        this.contractRepository.save(contract);

        return new MessageResponse(
                true ,
                "Success",
                mapToDto(contract)
        );
    }

    @Override
    public MessageResponse update(ContractDto contractDto) {
        log.debug("Request to update contract : {}", contractDto);

        boolean existe = contractRepository.existsById(contractDto.getId());
        if (!existe){
            var userId = contractDto.getUser_id();
            var user = this.userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalStateException("The user with ID[" + userId + "] was not found !"));

            var crContentId = contractDto.getCrContent_id();
            var crContent = this.crContentRepository.findById(crContentId)
                    .orElseThrow(() ->
                            new IllegalStateException("The cr content with ID[" + crContentId + "] was not found !"));

            ContractEntity contract = new ContractEntity(
                    contractDto.getId(),
                    Instant.now(),
                    contractDto.getContractStartDate(),
                    contractDto.getContractEndTrialPeriod(),
                    contractDto.getLeaveType(),
                    contractDto.getContractEndDate(),
                    contractDto.getLeaveReason(),
                    contractDto.getObservation(),
                    contractDto.getStatus(),
                    user,
                    crContent
            );

            this.contractRepository.save(contract);

            return new MessageResponse(
                    true ,
                    "Success",
                    mapToDto(contract)
            );
        } else {
            throw new IllegalStateException("There is already an affected role");
        }
    }

    @Override
    public ContractDto findById(Long id) {
        log.debug("Request to get affected role : {}", id);
        return this.contractRepository.findById(id).map(ContractServiceImpl::mapToDto).orElse(null);
    }

    public List<ContractDto> findAll() {
        log.debug("Request to get all affected role");
        return this.contractRepository.findAll()
                .stream()
                .map(ContractServiceImpl::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete contract : {}", id);

        var affectedRole = this.contractRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find contract with id " + id));

        //Optional.ofNullable(affectedRole.getUser()).ifPresent(userRepository::delete);
        //Optional.ofNullable(affectedRole.getRole()).ifPresent(roleRepository::delete);

        this.contractRepository.delete(affectedRole);
    }
}