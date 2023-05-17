package com.neo.exp.rest;

import com.neo.exp.dto.FilteredSiteDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FilteredSiteService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/filtered_site")
@Tag(name = "filtered_site", description = "All the filtered_site role methods")
public class FilteredSiteResource {
    @Inject
    FilteredSiteService filteredSiteService;
    @GET
    @Path("/{id}")
    public FilteredSiteDto findById(@PathParam("id") Long id) {
        return this.filteredSiteService.findById(id);
    }
    
    @GET
    public List<FilteredSiteDto> findAll() {
        return this.filteredSiteService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(FilteredSiteDto filteredSiteDto) {
        return this.filteredSiteService.create(filteredSiteDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, FilteredSiteDto filteredSiteDto) {
        filteredSiteDto.setId(id);
        return this.filteredSiteService.update(filteredSiteDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.filteredSiteService.delete(id);
    }
}
