package com.product.configuration;

import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.product.exception.ApplicationException;
import com.product.util.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories({"com.product.repository"})
public class CassandraConfig extends AbstractCassandraConfiguration {

    private static final String domainPackage = "com.product.domain";

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspacename;
    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;
    @Value("${spring.data.cassandra.port}")
    private String port;
    @Value("${spring.data.cassandra.username}")
    private String username;
    @Value("${spring.data.cassandra.password}")
    private String password;
    @Value("${spring.data.cassandra.consistency-level}")
    private String consistencyLevel;

    @Override
    protected String getKeyspaceName() {
        return this.keyspacename;
    }

    @Override
    protected String getContactPoints() {
        return this.contactPoints;
    }

    @Override
    protected int getPort() {
        return Integer.parseInt(this.port);
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { domainPackage };
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contactPoints);
        cluster.setPort(Integer.valueOf(port));
        cluster.setUsername(username);
        cluster.setPassword(password);
        return cluster;
    }


    @Override
    public CassandraSessionFactoryBean session()  {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        try {
            session.setCluster(cluster().getObject());
            session.setKeyspaceName(keyspacename);
            session.setConverter(new MappingCassandraConverter(cassandraMapping()));
            session.setSchemaAction(SchemaAction.NONE);
        } catch (ClassNotFoundException c) {
            throw new ApplicationException(c.toString(),ErrorCode.NOT_FOUND);
        }

        return session;
    }

    @Bean
    public CassandraConverter converter() throws ClassNotFoundException {
        return new MappingCassandraConverter(cassandraMapping());
    }

    @Bean
    public CassandraOperations cassandraOperations() throws Exception {
        return new CassandraTemplate(session().getObject());
    }
}
