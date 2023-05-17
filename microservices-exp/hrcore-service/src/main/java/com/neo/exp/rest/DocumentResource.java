package com.neo.exp.rest;

import com.neo.exp.dto.DocumentDto;
import com.neo.exp.responses.MessageResponse;
import com.neo.exp.service.DocumentService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Authenticated
@Path("/document")
@Tag(name = "document", description = "All the document role methods")
public class DocumentResource {
    @Inject
    DocumentService documentService;
    @GET
    @Path("/{id}")
    public DocumentDto findById(@PathParam("id") Long id) {
        return this.documentService.findById(id);
    }
    
    @GET
    public List<DocumentDto> findAll() {
        return this.documentService.findAll();
    }
    @Authenticated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse create(DocumentDto documentDto) {
        return this.documentService.create(documentDto);
    }
    @Authenticated
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public MessageResponse update(@PathParam("id") Long id, DocumentDto documentDto) {
        documentDto.setId(id);
        return this.documentService.update(documentDto);
    }
    
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.documentService.delete(id);
    }
}
