package com.neo.exp.resource;

import com.neo.exp.service.StorageService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequestScoped
@Path("/s3sv")
public class StorageResource {
    @Inject
    StorageService storageService;
    @POST
    @Path("/bucket/create/{bucketName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String CreateBucket(@PathParam("bucketName")String bucketName) {
        return storageService.CreateBucket(bucketName);
    }
    @DELETE
    @Path("/bucket/delete/{bucketName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteBucket(@PathParam("bucketName")String bucketName) {
        return storageService.DeleteBucket(bucketName);
    }
    @GET
    @Path("/bucket/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> Buckets(@PathParam("bucketName")String bucketName) {
        return storageService.Buckets();
    }
    /*
    @DeleteMapping("/file/delete/{bucketname}/{file}")
    @Operation(summary = "Delete a File")
    public Object removeFile(@PathVariable String bucketname, @PathVariable String file) throws InvalidKeyException {
        return storageService.deleteFile(bucketname,file);
    }
    */
    @POST
    @Path("/file/upload/{bucketName}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@PathParam("bucketName") String bucketName, InputStream inputStream) throws IOException {
        return storageService.uploadFile(bucketName,inputStream);
    }
    @DELETE
    @Path("/file/delete/{bucket}/{filename}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(
            @PathParam("bucket") String bucket,
            @PathParam("filename") String filename) {
        return storageService.deleteFile(bucket,filename);
    }
    @GET
    @Path("/file/view/{bucket}/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response Viewfile(
            @PathParam("bucket") String bucket,
            @PathParam("filename") String filename) {
        return storageService.Viewfile(bucket,filename);
    }
    @GET
    @Path("/file/geturl/{bucket}/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileLink(
            @PathParam("bucket") String bucket,
            @PathParam("key") String key) {
        return storageService.getFileLink(bucket,key);
    }

}
