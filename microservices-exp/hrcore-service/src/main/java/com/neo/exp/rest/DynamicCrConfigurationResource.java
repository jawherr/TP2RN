package com.neo.exp.rest;

import com.neo.exp.dto.DynamicCrConfigurationDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.DynamicCrConfigurationService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/affectedRole")
@Tag(name = "affectedRole", description = "All the affected role methods")
public class DynamicCrConfigurationResource {
    @Inject
    DynamicCrConfigurationService dynamicCrConfigurationService;
    @GET
    @Path("/{id}")
    public DynamicCrConfigurationDto findById(@PathParam("id") Long id) {
        return this.dynamicCrConfigurationService.findById(id);
    }
    
    @GET
    public List<DynamicCrConfigurationDto> findAll() {
        return this.dynamicCrConfigurationService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(DynamicCrConfigurationDto dynamicCrConfigurationDto) {
        return this.dynamicCrConfigurationService.create(dynamicCrConfigurationDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, DynamicCrConfigurationDto dynamicCrConfigurationDto) {
        dynamicCrConfigurationDto.setId(id);
        return this.dynamicCrConfigurationService.update(dynamicCrConfigurationDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.dynamicCrConfigurationService.delete(id);
    }
}
