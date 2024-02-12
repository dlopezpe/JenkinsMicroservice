package com.seido.micro.core.back.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity for the table build_compilation
 * The table build_compilation is define
 */
@Entity
@Table(name = "build_compilation")
@Getter
@Setter
public class BuildMetadata implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_name", nullable = true)
    private String jobName;

    @Column(name = "build_number", nullable = true)
    private Integer buildNumber;

    @Column(name = "path_repo", nullable = false)
    private String pathRepo;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "status_build", nullable = false)
    private String statusBuild;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "modified_at", nullable = true)
    private Timestamp modified_at=null;
}
