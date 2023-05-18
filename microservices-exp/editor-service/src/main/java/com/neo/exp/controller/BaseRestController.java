package com.neo.exp.controller;

import com.neo.exp.dto.OrgDTO;
import com.neo.exp.entity.Dsource;
import com.neo.exp.entity.OrgEntity;
import com.neo.exp.repository.DsourceRepository;
import com.neo.exp.repository.OrgEntityRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/base")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BaseRestController {
    @Inject
    private DsourceRepository dsourceRepository;

    @Inject
    private OrgEntityRepository orgEntityRepository;

    @Inject
    private Flyway flyway;

    @ConfigProperty(name = "quarkus.datasource.username")
    String datasourceUsername;


    @CrossOrigin(origins = "*")
    @POST
    @Path("/addsource/{tenant}")
    public ResponseEntity<Object> addDSource(@RequestBody OrgDTO orgDTO, @PathParam("tenant") String tenant){
        Dsource dsource = new Dsource();
        /*dsource.setTenantId(tenant);
        dsource.setUsername("root");
        dsource.setPassword("Password123#@!");
        dsource.setUrl("jdbc:postgresql://localhost:5432/"+tenant+"?useSSL=false");
        dsource.setDriverClassName("org.postgresql.Driver");*/
        dsource.setUsername("root");
        dsource.setPassword("Password123#@!");
        dsource.setUrl("jdbc:mysql://localhost:3306/"+tenant+"?useSSL=false");
        dsource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dsourceRepository.save(dsource);

        OrgEntity orgEntity = new OrgEntity();
        orgEntity.setEmail(orgDTO.getEmail());
        orgEntity.setOrgName(orgDTO.getOrgName());
        orgEntity.setFirstName(orgDTO.getFirstName());
        orgEntity.setLastName(orgDTO.getLastName());
        orgEntity.setInstanceName(orgDTO.getInstanceName());
        orgEntityRepository.save(orgEntity);

        Flyway fly = Flyway.configure()
                .configuration(flyway.getConfiguration())
                .schemas(tenant)
                .defaultSchema(tenant)
                .load();
        fly.migrate();

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/getAll")
    public List<Dsource> getAll() {
        return dsourceRepository.findAll();
    }
}
