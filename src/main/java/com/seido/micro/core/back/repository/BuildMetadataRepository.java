package com.seido.micro.core.back.repository;

import com.seido.micro.core.back.model.BuildMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPARepository for the table build_metadata
 * The table build_metadata is define by the compilation and build about launch of Jeninks
 */
public interface BuildMetadataRepository extends JpaRepository<BuildMetadata, Long>  {
    /**
     * Method to find the Build
     * @param id Send parameter the identified Build Number
     * @return Return job
     */
    public Optional<BuildMetadata> findByBuildNumber(Integer id);

    /**
     * Method to find by name and status
     * @param name Send required parameter
     * @param status Send required parameter
     * @return List Builds jenkins
     */
    public List<BuildMetadata> findByJobNameAndStatusBuild(String name, String status);

}
