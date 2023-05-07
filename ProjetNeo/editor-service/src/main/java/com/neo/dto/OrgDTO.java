package com.neo.dto;

import lombok.*;

import java.beans.ConstructorProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrgDTO {
    private String email;
    private String orgName;
    private String firstName;
    private String lastName;
    private String instanceName;
}
