package co.crystaldev.bearconomy.economy;

import co.crystaldev.bearconomy.economy.currency.Currency;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Configuration class for economy settings.
 *
 * @since 0.1.0
 */
@Data
public class EconomyConfig {

    /**
     * The currency used in the economy.
     */
    private final @NotNull Currency currency;

    /**
     * The symbol representing the economy's currency.
     */
    private final @NotNull String symbol;

    /**
     * The singular name of the currency (e.g., "Dollar").
     */
    private final @NotNull String singularName;

    /**
     * The plural name of the currency (e.g., "Dollars").
     */
    private final @NotNull String pluralName;

    /**
     * The maximum balance a party can hold, or null if there is no limit.
     */
    private final @Nullable BigDecimal maxBalance;

    /**
     * Retrieves the name of the currency in either singular or plural form.
     *
     * @param plural Whether to use the plural name.
     * @return The currency name.
     */
    @NotNull
    public String getName(boolean plural) {
        return plural ? this.pluralName : this.singularName;
    }

    /**
     * Retrieves the name of the currency in either singular or plural form.
     *
     * @param amount The amount.
     * @return The currency name.
     */
    @NotNull
    public String getName(@NotNull BigDecimal amount) {
        return this.getName(BigDecimal.ONE.equals(amount));
    }

    /**
     * Retrieves the name of the currency in either singular or plural form.
     *
     * @param amount The amount.
     * @return The currency name.
     */
    @NotNull
    public String getName(double amount) {
        return this.getName(amount == 1.0);
    }

    /**
     * Checks if a maximum balance limit is set.
     *
     * @return True if there is a maximum balance limit, false otherwise.
     */
    public boolean hasMaxBalance() {
        return this.maxBalance != null;
    }
}
