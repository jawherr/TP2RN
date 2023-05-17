package com.neo.exp.rest;

import com.neo.exp.dto.SiteDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.SiteService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/site")
@Tag(name = "site", description = "All the site methods")
public class SiteResource {
    @Inject
    SiteService siteService;
    @GET
    @Path("/{id}")
    public SiteDto findById(@PathParam("id") Long id) {
        return this.siteService.findById(id);
    }
    
    @GET
    public List<SiteDto> findAll() {
        return this.siteService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(SiteDto siteDto) {
        return this.siteService.create(siteDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, SiteDto siteDto) {
        siteDto.setId(id);
        return this.siteService.update(siteDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.siteService.delete(id);
    }
}
