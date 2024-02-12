package com.seido.micro.core.back.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * Class Template JobDao
 */
public class JdbcTemplateJobDao implements Dao<Jobs> {
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
    public void save(Jobs job) {
     String sql = "insert into JOBS (container_id,env,scope ,sandbox,params_json,date_insert ,date_init,date_end," +
            "date_killed,id_execution ,resume,state,status,priority ,retries) " +
            "values (?, ?, ?,?,?, ?, ?,?,?, ?, ?,?,?, ?)";

        jdbcTemplate.update(sql,  job.getContainerId(),
                job.getEnv(),job.getScope(),job.getSandbox(),job.getParamsJson(),job.getDateInsert(),job.getDateInit(),
                job.getDateEnd(),job.getDateKilled(),job.getIdExecution(),job.isResume(),job.getState(),
                job.getStatus(),job.getPriority(),job.getRetries());
    }

    @Override
    public Jobs load(long id) {
        return null;
    }

    @Override
    public void delete(long id) {
        String sql = "delete from JOBS" +
                "WHERE where id=?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(Jobs job) {
        String updateFields="status="+job.getStatus();

        String sql = "update JOBS set "+ updateFields +" where id=?";

        jdbcTemplate.update(sql,  job.getId());

    }

    @Override
    public List<Jobs> loadAll() {
        String sql="select * from JOBS";

        return getJdbcTemplate().query(sql,
                new BeanPropertyRowMapper(Jobs.class));
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
