package com.axelor.apps.testtask.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 150)
    private String surname;

    @Column(name = "EMAIL", nullable = false, length = 300)
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 260)
    private String password;

    @ColumnDefault("TRUE")
    @Column(name = "ENABLED")
    private Boolean enabled;

}