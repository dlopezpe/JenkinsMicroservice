package com.seido.micro.core.back.dao;

import com.seido.micro.core.back.model.BuildMetadata;
import com.seido.micro.core.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

/**
 * Class Template JobDao
 */
public class JdbcTemplateJobDao implements Dao<BuildMetadata> {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Method post constructor
     */
    @PostConstruct
    public void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void save(BuildMetadata build) {
     String sql = "insert into build_metadata (id,job_name,build_number ,path_repo ,version ,status_buil,created_at ) " +
            "values (?, ?, ?,?, ?, ?, ?)";

        jdbcTemplate.update(sql,  build.getId(),build.getJobName(),build.getBuildNumber(),build.getPathRepo(),
                build.getVersion(),build.getStatusBuild(), Timestamp.valueOf(Utils.getTimeStamp()));
    }

    @Override
    public BuildMetadata load(long id) {
        return null;
    }

    @Override
    public void delete(long id) {
        String sql = "delete from build_metadata" +
                "WHERE where id=?";

        jdbcTemplate.update(sql,  id);

    }

    @Override
    public void update(BuildMetadata build) {
        String updateFields="status_build="+build.getStatusBuild();

        String sql = "update build_metadata set "+ updateFields +" where job_name=?";

        jdbcTemplate.update(sql,  build.getJobName());

    }

    @Override
    public List<BuildMetadata> loadAll() {
        String sql="select * from build_metadata";

        return getJdbcTemplate().query(sql,
                new BeanPropertyRowMapper(BuildMetadata.class));
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
