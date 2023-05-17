package com.neo.exp.rest;

import com.neo.exp.dto.DynamicCrConfigTranslatableDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.DynamicCrConfigTranslatableService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/dynamic_cr_config_translatable")
@Tag(name = "dynamic_cr_config_translatable", description = "All the dynamic cr config translatable role methods")
public class DynamicCrConfigTranslatableResource {
    @Inject
    DynamicCrConfigTranslatableService dynamicCrConfigTranslatableService;
    @GET
    @Path("/{id}")
    public DynamicCrConfigTranslatableDto findById(@PathParam("id") Long id) {
        return this.dynamicCrConfigTranslatableService.findById(id);
    }
    
    @GET
    public List<DynamicCrConfigTranslatableDto> findAll() {
        return this.dynamicCrConfigTranslatableService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(DynamicCrConfigTranslatableDto dynamicCrConfigTranslatableDto) {
        return this.dynamicCrConfigTranslatableService.create(dynamicCrConfigTranslatableDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, DynamicCrConfigTranslatableDto dynamicCrConfigTranslatableDto) {
        dynamicCrConfigTranslatableDto.setId(id);
        return this.dynamicCrConfigTranslatableService.update(dynamicCrConfigTranslatableDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.dynamicCrConfigTranslatableService.delete(id);
    }
}
