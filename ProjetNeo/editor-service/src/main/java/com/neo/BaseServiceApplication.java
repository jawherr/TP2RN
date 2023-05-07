package com.neo;
import org.flywaydb.core.Flyway;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class BaseServiceApplication {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

    @Inject
    Flyway flyway;
    public void checkMigration() {
        // This will print 1.0.0
        System.out.println(flyway.info().current().getVersion().toString());
    }
}
