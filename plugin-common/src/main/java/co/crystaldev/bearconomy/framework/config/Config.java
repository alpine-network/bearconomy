package co.crystaldev.bearconomy.framework.config;

import co.crystaldev.alpinecore.framework.config.AlpineConfig;
import de.exlll.configlib.Comment;
import lombok.Getter;

/**
 * @since 0.1.0
 */
public final class Config extends AlpineConfig {

    @Getter
    private static Config instance;
    { instance = this; }

    @Comment({
            "The default storage type used for managed economies.",
            " ",
            "Valid Types: MYSQL, JSON"
    })
    public StorageType storageType = StorageType.JSON;

    @Comment({
            "",
            "Database connection details, required for database storage types.",
            " ",
            "host    : IP address or hostname of the database server",
            "port    : Port number for the database server",
            "username: Username for database access",
            "password: Password for database access",
            "database: Name of the Bearconomy database"
    })
    public String host = "0.0.0.0";
    public int port = 3306;
    public String username = "username";
    public String password = "password";
    public String database = "bearconomy";

    public enum StorageType {
        // TODO: More storage types for managed economies
        JSON,
        MYSQL
    }
}
