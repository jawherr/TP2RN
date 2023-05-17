package com.neo.exp.rest;

import com.neo.exp.dto.MediaDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.MediaService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/media")
@Tag(name = "media", description = "All the media methods")
public class MediaResource {
    @Inject
    MediaService mediaService;
    @GET
    @Path("/{id}")
    public MediaDto findById(@PathParam("id") Long id) {
        return this.mediaService.findById(id);
    }
    
    @GET
    public List<MediaDto> findAll() {
        return this.mediaService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(MediaDto media) {
        return this.mediaService.create(media);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, MediaDto media) {
        media.setId(id);
        return this.mediaService.update(media);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.mediaService.delete(id);
    }
}
