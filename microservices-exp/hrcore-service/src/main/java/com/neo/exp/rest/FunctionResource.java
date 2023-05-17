package com.neo.exp.rest;

import com.neo.exp.dto.FunctionDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FunctionService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/function")
@Tag(name = "function", description = "All the function methods")
public class FunctionResource {
    @Inject
    FunctionService functionService;
    @GET
    @Path("/{id}")
    public FunctionDto findById(@PathParam("id") Long id) {
        return this.functionService.findById(id);
    }
    
    @GET
    public List<FunctionDto> findAll() {
        return this.functionService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(FunctionDto functionDto) {
        return this.functionService.create(functionDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, FunctionDto functionDto) {
        functionDto.setId(id);
        return this.functionService.update(functionDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.functionService.delete(id);
    }
}
