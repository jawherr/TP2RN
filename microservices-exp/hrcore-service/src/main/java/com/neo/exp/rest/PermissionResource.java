package com.neo.exp.rest;

import com.neo.exp.dto.PermissionDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.PermissionService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/permission")
@Tag(name = "permission", description = "All the permission methods")
public class PermissionResource {
    @Inject
    PermissionService permissionService;
    @GET
    @Path("/{id}")
    public PermissionDto findById(@PathParam("id") Long id) {
        return this.permissionService.findById(id);
    }
    
    @GET
    public List<PermissionDto> findAll() {
        return this.permissionService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(PermissionDto permission) {
        return this.permissionService.create(permission);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, PermissionDto permission) {
        permission.setId(id);
        return this.permissionService.update(permission);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.permissionService.delete(id);
    }
}
