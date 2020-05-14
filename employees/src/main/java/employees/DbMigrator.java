package employees;

import org.flywaydb.core.Flyway;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.sql.DataSource;

@ApplicationScoped
public class DbMigrator {

    @Resource(mappedName = "java:/jdbc/BankDS")
    private DataSource dataSource;

    public void init( @Observes @Initialized( ApplicationScoped.class ) Object init ) {
        var flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
    }

}
