package com.neo.exp.rest;

import com.neo.exp.dto.FolderDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.FolderService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

//@Authenticated
@Path("/folder")
@Authenticated
@Tag(name = "folder", description = "All the folder methods")
public class FolderResource {
    @Inject
    FolderService folderService;
    //String authServerUrl = "http://102.219.179.72:4206/auth/realms/${tenant}";
    @GET
    @Path("/{id}")
    //@Logging(context=Module.COLLABORATOR, description="api to hiring a new collaborator {}", infoParams={"entity.registrationNumber"})
    public FolderDto findById(@PathParam("id") Long id) {
        return this.folderService.findById(id);
    }

    //@RolesAllowed("user")
    @GET
    public List<FolderDto> findAll() {
        return this.folderService.findAll();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(FolderDto folder){
        return this.folderService.create(folder);
    }

    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, FolderDto folder) {
        folder.setId(id);
        return this.folderService.update(folder);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.folderService.delete(id);
    }
}
