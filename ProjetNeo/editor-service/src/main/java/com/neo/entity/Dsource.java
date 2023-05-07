package com.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="dsource")
public class Dsource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(name="tenantId")
    String tenantId;

    @Column(name="url")
    String url;


    @Column(name="username")
    String username;

    @Column(name="password")
    String password;

    @Column(name="driverClassName")
    String driverClassName;
}
