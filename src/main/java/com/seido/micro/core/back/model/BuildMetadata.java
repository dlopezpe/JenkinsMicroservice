package com.seido.micro.core.back.model;

import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity for the table build_metadata
 * The table build_metadata is define
 */
@Entity
@Table(name = "build_metadata")
@Data
public class BuildMetadata implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "build_number")
    private Integer buildNumber;

    @Column(name = "path_repo")
    private String pathRepo;

    @Column(name = "version")
    private String version;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::state_type")
    @Column(name = "status_build")
    private BuildMetadataStatus statusBuild;

    @Column(name = "took")
    private Long took;

    @Column(name = "estimated_duration")
    private Long estimatedDuration;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "log_build")
    private String logBuild;


    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "modified_at")
    private Timestamp modified_at;
}
