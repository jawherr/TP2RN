package com.neo.exp.rest;

import com.neo.exp.dto.StatusHistoryDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.StatusHistoryService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/statusHistory")
@Tag(name = "statusHistory", description = "All the status history methods")
public class StatusHistoryResource {
    @Inject
    StatusHistoryService statusHistoryService;
    @GET
    @Path("/{id}")
    public StatusHistoryDto findById(@PathParam("id") Long id) {
        return this.statusHistoryService.findById(id);
    }
    
    @GET
    public List<StatusHistoryDto> findAll() {
        return this.statusHistoryService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(StatusHistoryDto statusHistory) {
        return this.statusHistoryService.create(statusHistory);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id,StatusHistoryDto statusHistory) {
        statusHistory.setId(id);
        return this.statusHistoryService.update(statusHistory);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.statusHistoryService.delete(id);
    }
}
