package com.seido.micro.core.back.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.seido.micro.core.back.model.BuildMetadata;
import com.seido.micro.core.back.model.BuildMetadataStatus;
import com.seido.micro.core.back.repository.BuildMetadataRepository;
import com.seido.micro.core.utils.Utils;
import com.seido.micro.core.utils.exception.ValidationException;
import com.seido.micro.core.utils.resource.BuildMetadataResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;

@Service
public class JenkinsService {

    private static final Logger LOGGER = LogManager.getLogger(JenkinsService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${jenkins.url}")
    private String jenkinsUrl;

    @Value("${jenkins.username}")
    private String jenkinsUsername;

    @Value("${jenkins.password}")
    private String jenkinsPassword;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private final BuildMetadataRepository buildMetadataRepository;

    public JenkinsService( BuildMetadataRepository buildMetadataRepository) {
        this.buildMetadataRepository = buildMetadataRepository;
    }

    public BuildMetadataResource lanzarCI(String jobName) throws ValidationException {
        try {
            // Autenticación en Jenkins
            JenkinsServer jenkinsServer = new JenkinsServer(URI.create(jenkinsUrl), jenkinsUsername, jenkinsPassword);

            // Obtener detalles del job
            JobWithDetails job = jenkinsServer.getJob(jobName);

            // Recoger la información del último build
            Build build = job.getLastBuild();
            int buildNumber = build.getNumber();
            String buildName = build.details().getDisplayName();
            String buildUrl = build.getUrl();

            // Analizar el buildUrl para obtener el pathRepo y la versión
            String[] urlParts = buildUrl.split("/");
            String pathRepo = urlParts[urlParts.length - 2]; // Obtener el penúltimo segmento de la URL
            String version = urlParts[urlParts.length - 1]; // Obtener el último segmento de la URL

            // Obtener el estado del job
            String jobStatus;
            if (job.isInQueue()) {
                jobStatus = BuildMetadataStatus.QUEUED.getDescripcion();
            } else if (build.details().isBuilding()) {
                jobStatus = BuildMetadataStatus.IN_PROGRESS.getDescripcion();
            } else if (!job.hasLastSuccessfulBuildRun() || job.hasLastCompletedBuildRun()) {
                jobStatus = BuildMetadataStatus.SUCCESS.getDescripcion();
            } else if (job.hasLastFailedBuildRun()) {
                jobStatus = BuildMetadataStatus.FAILURE.getDescripcion();
            } else if (job.hasLastUnsuccessfulBuildRun()) {
                jobStatus = BuildMetadataStatus.ABORTED.getDescripcion();
            } else if (job.hasLastUnstableBuildRun()) {
                jobStatus = BuildMetadataStatus.UNSTABLE.getDescripcion();
            } else if (job.hasLastStableBuildRun()) {
                jobStatus = BuildMetadataStatus.STABLE.getDescripcion();
            } else {
                jobStatus = BuildMetadataStatus.UNKNOWN.getDescripcion();
            }
            // Imprimir la información
            LOGGER.debug("ID del job: " + buildNumber);
            LOGGER.debug("Nombre del job: " + buildName);
            LOGGER.debug("Path del repositorio: " + pathRepo);
            LOGGER.debug("Versión: " + version);
            LOGGER.debug("Estado del job: " + jobStatus);

            // Persistir la metadata del build
            BuildMetadata buildMetadata = new BuildMetadata();
            buildMetadata.setBuildNumber(buildNumber);
            buildMetadata.setJobName(buildName);
            buildMetadata.setPathRepo(pathRepo);
            buildMetadata.setVersion(version);
            buildMetadata.setStatusBuild(jobStatus);

            buildMetadata.setCreated_at(Timestamp.valueOf(Utils.getTimeStamp()));

            buildMetadataRepository.save(buildMetadata);

            return mapper.map(buildMetadata, BuildMetadataResource.class);

        }catch (Exception e){
            LOGGER.error(e);
            throw new ValidationException("Problemas con la conexion de Jenkins: "+ e.getMessage());
        }
    }

}
