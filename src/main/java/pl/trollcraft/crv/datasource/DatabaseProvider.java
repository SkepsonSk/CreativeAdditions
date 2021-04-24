package pl.trollcraft.crv.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pl.trollcraft.crv.controller.CredentialsController;
import pl.trollcraft.crv.model.Credentials;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseProvider {

    private final HikariConfig config = new HikariConfig();
    private final HikariDataSource ds;

    public DatabaseProvider(CredentialsController credentialsController) {
        Credentials c = credentialsController.getCredentials();

        String host = c.getHost();
        int port = c.getPort();
        String database = c.getDatabase();

        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", host, port, database);

        config.setJdbcUrl(jdbcUrl);
        config.setUsername( c.getUsername() );
        config.setPassword( c.getPassword() );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        ds = new HikariDataSource( config );
    }

    public Connection conn() throws SQLException {
        return ds.getConnection();
    }

}
