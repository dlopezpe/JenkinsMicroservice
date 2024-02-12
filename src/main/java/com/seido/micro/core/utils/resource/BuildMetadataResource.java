package com.seido.micro.core.utils.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seido.micro.core.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Class to get GITHUB Manager
 */
@Getter
@Setter
@AllArgsConstructor
public class BuildMetadataResource implements Serializable {

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("buildNumber")
    private Integer buildNumber;

    @JsonProperty("pathRepo")
    private String pathRepo;

    @JsonProperty("version")
    private String version;

    @JsonProperty("statusBuild")
    private String statusBuild;


    @Override
    public String toString() {
        return "gitResource{" +
                ", jobName=" + jobName + '\'' +
                ", buildNumber=" + buildNumber + '\'' +
                ", pathRepo=" + pathRepo + '\'' +
                ", version=" + version + '\'' +
                ", statusBuild=" + statusBuild + '\'' +
                '}';
    }
}
