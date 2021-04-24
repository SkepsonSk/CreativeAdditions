package pl.trollcraft.crv.config.config;

import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.controller.CredentialsController;
import pl.trollcraft.crv.model.Credentials;

public class DataSourceConfig implements Config{

    private final CredentialsController credentialsController;

    public DataSourceConfig(CredentialsController credentialsController) {
        this.credentialsController = credentialsController;
    }

    @Override
    public void configure(ConfigProvider provider) {
        String host = provider.read("mysql.host", String.class);
        int port = provider.read("mysql.port", Integer.class);
        String username = provider.read("mysql.username", String.class);
        String password = provider.read("mysql.password", String.class);
        String database = provider.read("mysql.database", String.class);

        Credentials c = new Credentials(host, port, username, password, database);
        credentialsController.setCredentials(c);
    }
}
