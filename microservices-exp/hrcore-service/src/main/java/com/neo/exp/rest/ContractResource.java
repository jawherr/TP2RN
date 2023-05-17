package com.neo.exp.rest;

import com.neo.exp.dto.ContractDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.ContractService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/contract")
@Tag(name = "contract", description = "All the contract role methods")
public class ContractResource {
    @Inject
    ContractService contractService;
    @GET
    @Path("/{id}")
    public ContractDto findById(@PathParam("id") Long id) {
        return this.contractService.findById(id);
    }
    
    @GET
    public List<ContractDto> findAll() {
        return this.contractService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(ContractDto contractDto) {
        return this.contractService.create(contractDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, ContractDto contractDto) {
        contractDto.setId(id);
        return this.contractService.update(contractDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.contractService.delete(id);
    }
}
