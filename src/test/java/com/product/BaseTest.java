package com.product;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.product.configuration.CassandraConfig;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes = {ProductApplication.class, CassandraConfig.class})
@TestPropertySource("/application-test.properties")
public class BaseTest {

    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private static Boolean databaseStarted = false;
    private static Boolean databaseInitialized = false;

    public static final Instant DUMMY_DATE = Instant.parse("2020-03-20T12:00:00Z");
    protected static final UUID TEST_UUID = UUID.fromString("517455b5-24a2-41ce-a014-30962f5d6a25");

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    public BaseTest() {
        startEmbeddedCassandra();
        initializeDatabase();
    }

    private void startEmbeddedCassandra() {

        if (BaseTest.databaseStarted) {
            logger.info("Database already started");
            return;
        }

        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BaseTest.databaseStarted = true;
    }

    private void initializeDatabase() {
        if (BaseTest.databaseInitialized) {
            logger.info("Database already initialized");
            return;
        }
        try {
            String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS testKeySpace WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' };";
            String KEYSPACE_ACTIVATE_QUERY = "USE testKeySpace;";
            Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9142).build();
            final Session session = cluster.connect();
            session.execute(KEYSPACE_CREATION_QUERY);
            session.execute(KEYSPACE_ACTIVATE_QUERY);
            final CQLDataLoader cqlDataLoader = new CQLDataLoader(session);
            cqlDataLoader.load(new ClassPathCQLDataSet("schema.cql", false, false));
            logger.info("Database first time initialized");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        BaseTest.databaseInitialized = true;
    }

    @Before
    public void before() throws IOException, TTransportException {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
}
