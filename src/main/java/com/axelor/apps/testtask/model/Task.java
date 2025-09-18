package com.axelor.apps.testtask.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@ToString
@Builder
@Table(name = "TASKS")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TASK_NAME", nullable = false, length = 150)
    private String taskName;

    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "CREATED_DATE", nullable = false)
    private Instant createdDate;

    @Column(name = "UPDATED_DATE")
    private Instant updatedDate;

    @ColumnDefault("FALSE")
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

}