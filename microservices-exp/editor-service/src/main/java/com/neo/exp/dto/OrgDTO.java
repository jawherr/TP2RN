package com.neo.exp.dto;

import lombok.*;

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
