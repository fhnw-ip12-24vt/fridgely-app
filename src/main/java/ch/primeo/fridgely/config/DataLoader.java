package ch.primeo.fridgely.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Bean that loads initial data from SQL scripts after Hibernate creates the schema
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    
    private final DataSource dataSource;
    
    @Autowired
    public DataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void run(String... args) {
        try {
            logger.info("Loading initial data from SQL script");
            
            // Load and execute the SQL script
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
            resourceDatabasePopulator.addScript(new ClassPathResource("ch/primeo/fridgely/sql/data.sql"));
            resourceDatabasePopulator.execute(dataSource);
            
            logger.info("Data loaded successfully");
        } catch (Exception e) {
            logger.error("Error loading initial data", e);
        }
    }
}
