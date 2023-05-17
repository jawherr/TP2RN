package com.neo.exp.rest;

import com.neo.exp.dto.CrContentTranslatableDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.CrContentTranslatableService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/cr_content_translatable")
@Tag(name = "cr_content_translatable", description = "All the cr content translatable methods")
public class CrContentTranslatableResource {
    @Inject
    CrContentTranslatableService crContentTranslatableService;
    @GET
    @Path("/{id}")
    public CrContentTranslatableDto findById(@PathParam("id") Long id) {
        return this.crContentTranslatableService.findById(id);
    }
    
    @GET
    public List<CrContentTranslatableDto> findAll() {
        return this.crContentTranslatableService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(CrContentTranslatableDto crContentTranslatableDto) {
        return this.crContentTranslatableService.create(crContentTranslatableDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, CrContentTranslatableDto crContentTranslatableDto) {
        crContentTranslatableDto.setId(id);
        return this.crContentTranslatableService.update(crContentTranslatableDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.crContentTranslatableService.delete(id);
    }
}
