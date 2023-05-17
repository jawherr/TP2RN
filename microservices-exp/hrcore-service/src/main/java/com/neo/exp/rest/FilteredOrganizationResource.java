package com.neo.exp.rest;

import com.neo.exp.dto.FilteredOrganizationDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FilteredOrganizationService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/filteredOrganization")
@Tag(name = "filteredOrganization", description = "All the filtered organization methods")
public class FilteredOrganizationResource {
    @Inject
    FilteredOrganizationService filteredOrganizationService;
    @GET
    @Path("/{id}")
    public FilteredOrganizationDto findById(@PathParam("id") Long id) {
        return this.filteredOrganizationService.findById(id);
    }
    
    @GET
    public List<FilteredOrganizationDto> findAll() {
        return this.filteredOrganizationService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(FilteredOrganizationDto filteredOrganization) {
        return this.filteredOrganizationService.create(filteredOrganization);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, FilteredOrganizationDto filteredOrganization) {
        filteredOrganization.setId(id);
        return this.filteredOrganizationService.update(filteredOrganization);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.filteredOrganizationService.delete(id);
    }
}
