package com.neo.exp.rest;

import com.neo.exp.dto.RoleDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.RoleService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/role")
@Tag(name = "role", description = "All the role methods")
public class RoleResource {
    @Inject
    RoleService roleService;
    @GET
    @Path("/{id}")
    public RoleDto findById(@PathParam("id") Long id) {
        return this.roleService.findById(id);
    }
    
    @GET
    public List<RoleDto> findAll() {
        return this.roleService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(RoleDto role) {
        return this.roleService.create(role);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, RoleDto role) {
        role.setId(id);
        return this.roleService.update(role);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.roleService.delete(id);
    }
}
