package com.seido.micro.core.web.controller;


import com.seido.micro.core.back.service.JenkinsService;
import com.seido.micro.core.utils.exception.ValidationException;
import com.seido.micro.core.utils.resource.BuildMetadataResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controler for the Job Rest API
 */
@RestController
@RequestMapping("/api/ci")
public class CIController {
    private static final Logger LOG = LogManager.getLogger(CIController.class);
    private static final String PATH_JOBNAME = "/{jobname}";

    @Autowired
    protected ModelMapper mapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JenkinsService jenkinsService;

    protected static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    /**
     * Get information a job with jobname only
     *
     * @param jobName Job name
     * @return ResponseEntity<>
     * @throws ValidationException ValidationException
     */
    @ApiOperation(value = "Get information a job with jobname only", response = BuildMetadataResource.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns a buildMetadataResource"),
                    @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
                    @ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Bad credentials"),
                    @ApiResponse(
                            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                            message = "Internal Server Error")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public ResponseEntity<BuildMetadataResource> launchCIJobName(@RequestParam @Valid String jobName)
            throws ValidationException {


        LOG.info("INIT: Rest service launchCI");
        LOG.info("Validate fields");

        // Is true validating all fields and false alone validated environment and scope
        BuildMetadataResource buildMetadataResource =new BuildMetadataResource();
        buildMetadataResource.setJobName(jobName);
        validateBuildMetadataResource(buildMetadataResource, false);

        // Lanza la CI
        buildMetadataResource = jenkinsService.obtenerInformacionJob(buildMetadataResource.getJobName());

        LOG.info("END: Rest service launchCI");
        HttpHeaders headers = createHeaders();

        headers.setLocation(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path(PATH_JOBNAME)
                        .buildAndExpand(buildMetadataResource)
                        .toUri());

        return new ResponseEntity<>(buildMetadataResource, headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create a job for build in CI (Jenkins)", response = BuildMetadataResource.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = HttpURLConnection.HTTP_CREATED,
                            message = "Returns a buildMetadataResource"),
                    @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
                    @ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Bad credentials"),
                    @ApiResponse(
                            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                            message = "Internal Server Error")
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/launchCI")
    public ResponseEntity<BuildMetadataResource> launchCI(@RequestBody
                                                              @Valid
                                                              BuildMetadataResource buildMetadataResource)
            throws ValidationException {


        LOG.info("INIT: Rest service launchCI");
        LOG.info("Validate fields");

        // Is true validating all fields and false alone validated
        validateBuildMetadataResource(buildMetadataResource, true);

        //Validate format version
        validateFormatVersion(buildMetadataResource.getVersion());

        // Lanza la CI
        buildMetadataResource = jenkinsService.lanzarCI(buildMetadataResource);

        LOG.info("END: Rest service launchCI");
        HttpHeaders headers = createHeaders();

        headers.setLocation(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path(PATH_JOBNAME)
                        .buildAndExpand(buildMetadataResource)
                        .toUri());

        return new ResponseEntity<>(buildMetadataResource, headers, HttpStatus.CREATED);
    }


    /**
     * Para verificar el formato de la version (nombre de etiqueta) en GitHub, debes tener en cuenta las siguientes reglas:
     *
     *     Longitud Máxima:
     *         La longitud máxima del tagname en GitHub es de 255 caracteres.
     *
     *     Caracteres Permitidos:
     *         Puedes utilizar letras (mayúsculas y minúsculas).
     *         Puedes utilizar números.
     *         Puedes utilizar guiones (-).
     *         Puedes utilizar puntos (.).
     *         Puedes utilizar barras inclinadas (/).
     *
     *     Restricciones Especiales:
     *         GitHub puede tener restricciones adicionales dependiendo del contexto del uso del tagname.
     *
     * Ejemplo de un tagname válido:
     *
     * plaintext
     *
     * v1.0.0
     * release-2.3
     * feature/awesome-feature
     *
     * Ejemplo de un tagname no válido:
     *
     * plaintext
     *
     * my tag // contiene un espacio
     * #invalid // contiene un carácter especial no permitido
     * too_long_tag_name_to_make_it_invalid // más de 255 caracteres
     * @param version
     */
    private void validateFormatVersion(String version) {
        StringBuilder msg = new StringBuilder();
        if (version.length() >255){
            msg.append("La longitud máxima del tagname en GitHub es de 255 caracteres\n");
        }

        if (isValidVersion(version)){
            msg.append("Caracteres Permitidos:\n" +
                    "\n" +
                    "    Puedes utilizar letras (mayúsculas y minúsculas).\n" +
                    "    Puedes utilizar números.\n" +
                    "    Puedes utilizar guiones (-).\n" +
                    "    Puedes utilizar puntos (.).\n" +
                    "    Puedes utilizar barras inclinadas (/).\n");
        }
    }
    private static final String TAGNAME_REGEX = "^[a-zA-Z0-9_.\\-/]+$";
    private static final Pattern TAGNAME_PATTERN = Pattern.compile(TAGNAME_REGEX);

    public static boolean isValidVersion(String tagName) {
        Matcher matcher = TAGNAME_PATTERN.matcher(tagName);
        return matcher.matches();
    }

    private static boolean verificarSoloLetras(String cadena) {
        return cadena.matches("[a-zA-Z]+");
    }

    private static boolean contieneBackslash(String pathRepo) {
        // Verificar si la cadena contiene el carácter \
        return pathRepo.contains("\\");
    }

    /**
     * Validate all fields or alone depends if create or find to query
     *
     * @param nexec     Param execution resource
     * @param allFields if true is all validations fields or false alone ENV and SCOPE
     * @throws ValidationException
     */
    private void validateBuildMetadataResource(BuildMetadataResource nexec, boolean allFields)
            throws ValidationException {

        StringBuilder msg = new StringBuilder();

        // Verify JobName not null of empty
        if (StringUtils.isEmpty(nexec.getJobName())) {
            msg.append("JobName cannot be empty or null\n");
        }

        if (allFields) {
            //Verify only letters
            if (!verificarSoloLetras(nexec.getJobName())) {
                msg.append("JobName has only letters\n");
            }

            // Verify PathRepo not null of empty
            if (StringUtils.isEmpty(nexec.getPathRepo())) {
                msg.append("PathRepo cannot be empty or null\n");
            }

            if (!contieneBackslash(nexec.getPathRepo())) {
                msg.append("PathRepo hasnot contains \\ \n");
            }

        }
    }
}
