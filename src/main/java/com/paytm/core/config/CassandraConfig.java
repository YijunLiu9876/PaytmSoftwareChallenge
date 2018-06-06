package com.paytm.core.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration

@PropertySource(value = { "classpath:cassandra.properties" })
@EnableCassandraRepositories(basePackages = "com.paytm.core.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    protected final String keyspaceName = "user_events";

    @Override
    protected String getKeyspaceName() {
        return this.keyspaceName;
    }

    @Override
    protected List getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification
                .createKeyspace(keyspaceName).ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication());
    }

    @Override
    protected List getStartupScripts() {
        List<String> list = new ArrayList<>();
        String createKeySpaceScript = "CREATE KEYSPACE IF NOT EXISTS "
                + keyspaceName + " WITH replication = {"
                + " 'class': 'SimpleStrategy', "
                + " 'replication_factor': '1' " + "};";
        String createTableScript = "CREATE TABLE IF NOT EXISTS "
                + keyspaceName + ".user_event ("
                + " username text,"
                + " tweetId text,"
                + " PRIMARY KEY (username, tweetId)" + ")"
                + " with bloom_filter_fp_chance=0.01;";
        list.add(createKeySpaceScript);
        list.add(createTableScript);
        return list;

    }

    @Bean
    public QueryLogger queryLogger(Cluster cluster) {
        QueryLogger queryLogger = QueryLogger.builder()
                .build();
        cluster.register(queryLogger);
        return queryLogger;
    }

}
