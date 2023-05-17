package com.neo.exp.rest;

import com.neo.exp.dto.OrganizationDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.OrganizationService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/organization")
@Tag(name = "organization", description = "All the organization methods")
public class OrganizationResource {
    @Inject
    OrganizationService organizationService;
    @GET
    @Path("/{id}")
    public OrganizationDto findById(@PathParam("id") Long id) {
        return this.organizationService.findById(id);
    }
    
    @GET
    public List<OrganizationDto> findAll() {
        return this.organizationService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(OrganizationDto organization) {
        return this.organizationService.create(organization);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, OrganizationDto organization) {
        organization.setId(id);
        return this.organizationService.update(organization);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.organizationService.delete(id);
    }
}
