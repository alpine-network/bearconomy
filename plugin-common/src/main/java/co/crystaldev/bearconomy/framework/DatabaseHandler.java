package co.crystaldev.bearconomy.framework;

import co.crystaldev.alpinecore.util.DatabaseConnection;
import co.crystaldev.bearconomy.framework.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.1.0
 */
public final class DatabaseHandler {

    private static final DatabaseHandler INSTANCE = new DatabaseHandler();

    public static @NotNull DatabaseHandler getInstance() {
        return INSTANCE;
    }

    private DatabaseConnection sqlConnection;

    public @NotNull DatabaseConnection getSQLConnection() {
        if (this.sqlConnection == null) {
            Config config = Config.getInstance();
            String url = String.format("jdbc:mysql://%s:%s/", config.host, config.port);
            this.sqlConnection = new DatabaseConnection(config.database, url, config.username, config.password);
        }

        return this.sqlConnection;
    }
}
