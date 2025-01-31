package co.crystaldev.bearconomy.framework.config;

import co.crystaldev.alpinecore.framework.config.AlpineConfig;
import co.crystaldev.alpinecore.framework.config.object.ConfigMessage;
import de.exlll.configlib.Comment;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    @Comment({
            "",
            "Standard Economy Configuration"
    })
    public boolean enforceMaxBalance = true;

    public BigDecimal balanceCapacity = new BigDecimal(25_000_000);

    public BigDecimal defaultBalance = new BigDecimal(0);

    @Comment({
            "",
            "Plugin Messages"
    })
    public ConfigMessage error = ConfigMessage.of(
            "%error_prefix% Unable to complete transaction:",
            "    <i><error_highlight>%response%</error_highlight></i>"
    );

    public ConfigMessage deposit = ConfigMessage.of(
            "%prefix% Deposited <highlight>%amount%</highlight> into the account of <highlight>%player%</highlight>");

    public ConfigMessage deposited = ConfigMessage.of(
            "%prefix% <highlight>%amount%</highlight> has been deposited into your account");

    public ConfigMessage withdraw = ConfigMessage.of(
            "%prefix% Withdrew <highlight>%amount%</highlight> from the account of <highlight>%player%</highlight>");

    public ConfigMessage withdrew = ConfigMessage.of(
            "%prefix% <highlight>%amount%</highlight> has been withdrawn from your account");

    public ConfigMessage paymentSent = ConfigMessage.of(
            "%prefix% <highlight>%amount%</highlight> was sent to <highlight>%player%</highlight>");

    public ConfigMessage paymentReceived = ConfigMessage.of(
            "%prefix% <highlight>%amount%</highlight> was received from <highlight>%player%</highlight>");

    public ConfigMessage paySelf = ConfigMessage.of(
            "%error_prefix% You are unable to send money to yourself");

    public ConfigMessage hasNotPlayedBefore = ConfigMessage.of(
            "%error_prefix% %player_name% has not joined before");

    public ConfigMessage balanceUpdatedOther = ConfigMessage.of(
            "%prefix% Balance for <highlight>%player%</highlight> was set to <highlight>%amount%</highlight>");

    public ConfigMessage balanceUpdatedSelf = ConfigMessage.of(
            "%prefix% Your balance was set to <highlight>%amount%</highlight>");

    public ConfigMessage balanceOther = ConfigMessage.of(
            "%prefix% Balance of %player%: <highlight>%amount%</highlight>");

    public ConfigMessage balanceSelf = ConfigMessage.of(
            "%prefix% Balance: <highlight>%amount%</highlight>");

    public ConfigMessage sortingBalances = ConfigMessage.of(
            "%prefix% Sorting balances of <highlight>%amount% players</highlight>...");

    public ConfigMessage balanceTopTitle = ConfigMessage.of(
            "Balance Top");

    public ConfigMessage serverTotal = ConfigMessage.of(
            "<highlight>Server Total:</highlight> <emphasis>%amount%</emphasis>");

    public ConfigMessage balanceTopEntry = ConfigMessage.of(
            "<highlight>#%position%</highlight> %player%: <emphasis>%amount%</emphasis>");

    public Map<String, ConfigMessage> currencyBalanceTopEntries = new HashMap<>();

    @Comment({
            "",
            "Error Messages"
    })
    public ConfigMessage oversizeBalanceOther = ConfigMessage.of(
            "The balance of <error_highlight>%player%</error_highlight> would exceed the balance capacity of <error_highlight>%limit%</error_highlight>");

    public ConfigMessage oversizeBalanceSelf = ConfigMessage.of(
            "Your balance would exceed the balance limit of <error_highlight>%limit%</error_highlight>");

    public ConfigMessage insufficientBalanceOther = ConfigMessage.of(
            "<error_highlight>%player%</error_highlight> does not have sufficient funds");

    public ConfigMessage insufficientBalanceSelf = ConfigMessage.of(
            "You not have sufficient funds");

    public ConfigMessage invalidTransactionAmount = ConfigMessage.of(
            "The transaction amount must be a positive number");

    public ConfigMessage invalidNumber = ConfigMessage.of(
            "The number must be a valid positive number");

    public enum StorageType {
        // TODO: More storage types for managed economies
        JSON,
        MYSQL
    }
}
