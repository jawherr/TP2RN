package com.neo.exp.rest;

import com.neo.exp.dto.AffectedRoleDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.AffectedRoleService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/affectedRole")
@Tag(name = "affectedRole", description = "All the affected role methods")
public class AffectedRoleResource {
    @Inject
    AffectedRoleService affectedRoleService;
    @GET
    @Path("/{id}")
    public AffectedRoleDto findById(@PathParam("id") Long id) {
        return this.affectedRoleService.findById(id);
    }
    
    @GET
    public List<AffectedRoleDto> findAll() {
        return this.affectedRoleService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(AffectedRoleDto affectedRole) {
        return this.affectedRoleService.create(affectedRole);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, AffectedRoleDto affectedRole) {
        affectedRole.setId(id);
        return this.affectedRoleService.update(affectedRole);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.affectedRoleService.delete(id);
    }
}
