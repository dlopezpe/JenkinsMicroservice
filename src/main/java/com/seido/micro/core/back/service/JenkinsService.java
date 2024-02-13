package com.seido.micro.core.back.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import com.seido.micro.core.back.model.BuildMetadata;
import com.seido.micro.core.back.model.BuildMetadataStatus;
import com.seido.micro.core.back.repository.BuildMetadataRepository;
import com.seido.micro.core.utils.Utils;
import com.seido.micro.core.utils.exception.ValidationException;
import com.seido.micro.core.utils.resource.BuildMetadataResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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

    public BuildMetadataResource obtenerInformacionJob(String jobName) throws ValidationException {
        try {
            // Autenticación en Jenkins
            JenkinsServer jenkinsServer = new JenkinsServer(URI.create(jenkinsUrl), jenkinsUsername, jenkinsPassword);

            // Obtener detalles del job
            JobWithDetails job = jenkinsServer.getJob(jobName);

            if (job != null) {

                // Recoger la información del último build
                Build build = job.getLastBuild();

                int buildNumber = build.getNumber();
                String buildName = job.getName();
                String buildUrl = build.getUrl();

                // Analizar el buildUrl para obtener el pathRepo y la versión
                String[] urlParts = buildUrl.split("/");
                String pathRepo = buildUrl; // Obtener el penúltimo segmento de la URL
                String version = urlParts[urlParts.length - 1]; // Obtener el último segmento de la URL

                BuildMetadata buildMetadata = new BuildMetadata();
                // Obtener el estado del job
                if (job.isInQueue()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.QUEUED);
                } else if (build.details().isBuilding()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.IN_PROGRESS);
                } else if (!job.hasLastSuccessfulBuildRun() || job.hasLastCompletedBuildRun()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.SUCCESS);
                } else if (job.hasLastFailedBuildRun()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.FAILURE);
                } else if (job.hasLastUnsuccessfulBuildRun()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.NOT_BUILT);
                } else if (!job.hasLastCompletedBuildRun()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.ABORTED);
                } else if (job.hasLastUnstableBuildRun()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.UNSTABLE);
                } else if (job.hasLastStableBuildRun()) {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.STABLE);
                } else {
                    buildMetadata.setStatusBuild(BuildMetadataStatus.UNKNOWN);
                }
                // Imprimir la información
                LOGGER.debug("ID del job: " + buildNumber);
                LOGGER.debug("Nombre del job: " + buildName);
                LOGGER.debug("Path del repositorio: " + pathRepo);
                LOGGER.debug("Versión: " + version);
                LOGGER.debug("Estado del job: " + buildMetadata.getStatusBuild().name());

                buildMetadata.setBuildNumber(buildNumber);
                buildMetadata.setCreated_at(Timestamp.valueOf(Utils.getTimeStamp()));
                buildMetadata.setJobName(buildName);
                buildMetadata.setModified_at(Timestamp.valueOf(Utils.getTimeStamp()));
                buildMetadata.setPathRepo(pathRepo);
                buildMetadata.setVersion(version);
                buildMetadata.setTook(build.details().getDuration());
                buildMetadata.setEstimatedDuration(build.details().getEstimatedDuration());
                buildMetadata.setTimestamp(new Timestamp(build.details().getTimestamp()));
                buildMetadata.setLogBuild(build.details().getConsoleOutputText());

                return mapper.map(buildMetadata, BuildMetadataResource.class);
            }else{
                throw new ValidationException("No se ha encontrado el siguiente job: "+ jobName);
            }

        }catch (Exception e){
            LOGGER.error(e);
            throw new ValidationException("Problemas con la conexion de Jenkins: "+ e.getMessage());
        }
    }

    public BuildMetadataResource lanzarCI(BuildMetadataResource buildMetadataResource) throws ValidationException {
        try {
            // Autenticación en Jenkins
            JenkinsServer jenkinsServer = new JenkinsServer(URI.create(jenkinsUrl), jenkinsUsername, jenkinsPassword);

            // Nombre del job que quieres ejecutar
            String jobName=buildMetadataResource.getJobName();

            // Parámetros del build
            Map<String, String> buildParameters = new HashMap<>();
            buildParameters.put("buildNumber", buildMetadataResource.getBuildNumber().toString());
            buildParameters.put("jobName", jobName);
            buildParameters.put("pathRepo", buildMetadataResource.getPathRepo());
            buildParameters.put("version", buildMetadataResource.getVersion());

            // Obtener detalles del job
            QueueReference buildAsincronize = jenkinsServer.getJob(jobName).build(buildParameters,true);

            String[] urlPartsAsincronize = buildAsincronize.getQueueItemUrlPart().split("/");
            int buildNumber = Integer.parseInt(urlPartsAsincronize[urlPartsAsincronize.length - 1]);

            // Obtener detalles del job
            JobWithDetails job = jenkinsServer.getJob(jobName);
            if (job != null) {

                 // Obtener el último build
                 Build lastBuild = job.getLastBuild();
                if (lastBuild != null) {


                 String buildName = job.getName();
                 String buildUrl = lastBuild.getUrl();

                 // Analizar el buildUrl para obtener el pathRepo y la versión
                 String[] urlParts = buildUrl.split("/");
                 String pathRepo = buildUrl; // Obtener el penúltimo segmento de la URL
                 String version = urlParts[urlParts.length - 1]; // Obtener el último segmento de la URL

                 // Persistir la metadata del build
                 BuildMetadata buildMetadata = new BuildMetadata();
                 // Obtener el estado del job
                 if (job.isInQueue()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.QUEUED);
                 } else if (lastBuild.details().isBuilding()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.IN_PROGRESS);
                 } else if (!job.hasLastSuccessfulBuildRun() || job.hasLastCompletedBuildRun()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.SUCCESS);
                 } else if (job.hasLastFailedBuildRun()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.FAILURE);
                 } else if (job.hasLastUnsuccessfulBuildRun()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.NOT_BUILT);
                 } else if (!job.hasLastCompletedBuildRun()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.ABORTED);
                 } else if (job.hasLastUnstableBuildRun()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.UNSTABLE);
                 } else if (job.hasLastStableBuildRun()) {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.STABLE);
                 } else {
                     buildMetadata.setStatusBuild(BuildMetadataStatus.UNKNOWN);
                 }
                 // Imprimir la información
                 LOGGER.debug("ID del job: " + buildNumber);
                 LOGGER.debug("Nombre del job: " + buildName);
                 LOGGER.debug("Path del repositorio: " + pathRepo);
                 LOGGER.debug("Versión: " + version);
                 LOGGER.debug("Estado del job: " + buildMetadata.getStatusBuild().name());

                 buildMetadata.setBuildNumber(buildNumber);
                 buildMetadata.setCreated_at(Timestamp.valueOf(Utils.getTimeStamp()));
                 buildMetadata.setJobName(buildName);
                 buildMetadata.setModified_at(Timestamp.valueOf(Utils.getTimeStamp()));
                 buildMetadata.setPathRepo(pathRepo);
                 buildMetadata.setVersion(version);
                 buildMetadata.setTook(lastBuild.details().getDuration());
                 buildMetadata.setEstimatedDuration(lastBuild.details().getEstimatedDuration());
                 buildMetadata.setTimestamp(new Timestamp(lastBuild.details().getTimestamp()));
                 buildMetadata.setLogBuild(lastBuild.details().getConsoleOutputText());

                 buildMetadataRepository.save(buildMetadata);

                 return mapper.map(buildMetadata, BuildMetadataResource.class);
                }else{
                    throw new ValidationException("No se ha encontrado el siguiente job: "+ jobName+ ". Con el siguiente numberJob: +"+buildNumber);
                }
            }else{
                throw new ValidationException("No se ha encontrado el siguiente job: "+ jobName);
            }
        }catch (Exception e){
            LOGGER.error(e);
            throw new ValidationException("Problemas con la conexion de Jenkins: "+ e.getMessage());
        }
    }
}
