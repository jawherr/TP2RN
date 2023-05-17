package com.neo.exp.rest;

import com.neo.exp.dto.UserDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.UserService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
@Authenticated
@Tag(name = "users", description = "All the users methods")
public class UserResource {
    @Inject
    JsonWebToken jwt;
    @Inject
    UserService userService;
    @GET
    public List<UserDto> findAll() {
        return this.userService.findAll();
    }

    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(UserDto user) {
        return this.userService.create(user);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, UserDto user) {
        user.setId(id);
        return this.userService.update(user);
    }


    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.userService.delete(id);
    }
}