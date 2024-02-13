package com.seido.micro.core.utils.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seido.micro.core.back.model.BuildMetadataStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Class to get GITHUB Manager
 */
@Getter
@Setter
@NoArgsConstructor
public class BuildMetadataResource implements Serializable {

    @JsonProperty("id")
    @JsonIgnore
    private String id;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("buildNumber")
    private Integer buildNumber;

    @JsonProperty("pathRepo")
    private String pathRepo;

    @JsonProperty("version")
    private String version;

    @JsonProperty("statusBuild")
    @JsonIgnore
    private BuildMetadataStatus statusBuild;

    @JsonProperty("took")
    @JsonIgnore
    private Integer took;

    @JsonIgnore
    @JsonProperty("estimatedDuration")
    private Long estimatedDuration;

    @JsonIgnore
    @JsonProperty("timestamp")
    private Timestamp timestamp;

    @JsonIgnore
    @JsonProperty("logBuild")
    private String logBuild;

    @JsonProperty("created_at")
    @JsonIgnore
    private Timestamp created_at;

    @JsonIgnore
    private Timestamp modified_at;
    @Override
    public String toString() {
        return "gitResource{" +
                ", id=" + id + '\'' +
                ", jobName=" + jobName + '\'' +
                ", buildNumber=" + buildNumber + '\'' +
                ", pathRepo=" + pathRepo + '\'' +
                ", version=" + version + '\'' +
                ", statusBuild=" + statusBuild.name() + '\'' +
                ", took=" + took + '\'' +
                ", estimatedDuration=" + estimatedDuration + '\'' +
                ", timestamp=" + timestamp + '\'' +
                ", logBuild=" + logBuild + '\'' +
                ", created_at=" + created_at + '\'' +
                '}';
    }
}
