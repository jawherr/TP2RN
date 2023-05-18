package com.neo.exp.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Dsource {

    int id;

    String tenantId;

    String url;

    String username;

    String password;

    String driverClassName;

}
