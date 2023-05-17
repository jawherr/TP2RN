package com.neo.exp.rest;

import com.neo.exp.dto.CrContentDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.CrContentService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/cr_content")
@Tag(name = "crContent", description = "All the cr_content role methods")
public class CrContentResource {
    @Inject
    CrContentService crContentService;
    @GET
    @Path("/{id}")
    public CrContentDto findById(@PathParam("id") Long id) {
        return this.crContentService.findById(id);
    }
    
    @GET
    public List<CrContentDto> findAll() {
        return this.crContentService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(CrContentDto crContentDto) {
        return this.crContentService.create(crContentDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, CrContentDto crContentDto) {
        crContentDto.setId(id);
        return this.crContentService.update(crContentDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.crContentService.delete(id);
    }
}
