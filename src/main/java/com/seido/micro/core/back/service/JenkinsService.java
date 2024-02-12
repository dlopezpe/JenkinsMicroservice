package com.seido.micro.core.back.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.seido.micro.core.back.model.BuildMetadata;
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
            String status = job.isInQueue() ? "En cola" : (job.isBuildable() ? "En construcción" : "Listo"); // Ejemplo de cómo obtener el estado

            // Imprimir la información
            System.out.println("ID del job: " + buildNumber);
            System.out.println("Nombre del job: " + buildName);
            System.out.println("Path del repositorio: " + pathRepo);
            System.out.println("Versión: " + version);
            System.out.println("Estado del job: " + status);

            // Persistir la metadata del build
            BuildMetadata buildMetadata = new BuildMetadata();
            buildMetadata.setBuildNumber(job.getNextBuildNumber());
            buildMetadata.setJobName(buildName);
            buildMetadata.setPathRepo(pathRepo);
            buildMetadata.setVersion(version);
            buildMetadata.setStatusBuild(status);
            buildMetadata.setCreated_at(Timestamp.valueOf(Utils.getTS()));
            buildMetadataRepository.save(buildMetadata);

            return mapper.map(buildMetadata, BuildMetadataResource.class);

        }catch (Exception e){
            throw new ValidationException("Problemas con la conexion de Jenkins: "+ e.getMessage());
        }
    }

}
