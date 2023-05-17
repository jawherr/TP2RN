package com.neo.exp.rest;

import com.neo.exp.dto.TeamDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.TeamService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/team")
@Tag(name = "team", description = "All the team methods")
public class TeamResource {
    @Inject
    TeamService teamService;
    @GET
    @Path("/{id}")
    public TeamDto findById(@PathParam("id") Long id) {
        return this.teamService.findById(id);
    }
    
    @GET
    public List<TeamDto> findAll() {
        return this.teamService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(TeamDto teamDto) {
        return this.teamService.create(teamDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, TeamDto teamDto) {
        teamDto.setId(id);
        return this.teamService.update(teamDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.teamService.delete(id);
    }
}
