package com.neo.exp.rest;

import com.neo.exp.dto.GlobalConfigDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.GlobalConfigService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/globalConfig")
@Tag(name = "globalConfig", description = "All the global config methods")
public class GlobalConfigResource {
    @Inject
    GlobalConfigService globalConfigService;
    @GET
    @Path("/{id}")
    public GlobalConfigDto findById(@PathParam("id") Long id) {
        return this.globalConfigService.findById(id);
    }
    
    @GET
    public List<GlobalConfigDto> findAll() {
        return this.globalConfigService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(GlobalConfigDto globalConfig) {
        return this.globalConfigService.create(globalConfig);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, GlobalConfigDto globalConfig) {
        globalConfig.setId(id);
        return this.globalConfigService.update(globalConfig);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.globalConfigService.delete(id);
    }
}
