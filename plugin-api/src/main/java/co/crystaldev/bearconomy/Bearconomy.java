package co.crystaldev.bearconomy;

import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Defines the primary interface for interacting with the Bearconomy system.
 *
 * @since 0.1.0
 */
public interface Bearconomy {

    /**
     * Retrieves the singleton instance of Bearconomy.
     *
     * @return The Bearconomy instance.
     */
    @NotNull
    static Bearconomy get() {
        return Reference.BEARCONOMY;
    }

    /**
     * Registers an external, unmanaged economy into the Bearconomy system.
     *
     * @param economy The economy to be registered.
     */
    void registerEconomy(@NotNull Economy economy);

    /**
     * Registers a new, managed economy with a specific configuration.
     *
     * @param id The unique identifier for the economy.
     * @param currency The currency used by this economy.
     * @param config The configuration settings for the economy.
     */
    void registerEconomy(@NotNull String id, @NotNull Currency currency, @Nullable EconomyConfig config);

    /**
     * Registers a new, managed economy with default configuration.
     *
     * @param id The unique identifier for the economy.
     * @param currency The currency used by this economy.
     */
    default void registerEconomy(@NotNull String id, @NotNull Currency currency) {
        this.registerEconomy(id, currency, null);
    }

    /**
     * Retrieves the default economy.
     *
     * @return The default Economy instance.
     */
    @NotNull
    default Economy getEconomy() {
        return this.getEconomy(Economy.DEFAULT_ID)
                .orElseThrow(() -> new IllegalStateException("no default currency registered"));
    }

    /**
     * Checks if an economy with the specified identifier exists.
     *
     * @param id The economy identifier.
     * @return True if the economy exists, false otherwise.
     */
    boolean hasEconomy(@NotNull String id);

    /**
     * Retrieves an optional economy by its identifier.
     *
     * @param id The economy identifier.
     * @return The economy.
     */
    @NotNull
    Optional<Economy> getEconomy(@NotNull String id);

    /**
     * Retrieves an optional economy by its identifier.
     *
     * @param id The economy identifier.
     * @return The economy.
     */
    @Nullable
    default Economy fetchEconomy(@NotNull String id) {
        return this.getEconomy(id).orElse(null);
    }

    /**
     * Checks if an economy for the specified currency exists.
     *
     * @param currency The currency to check.
     * @return True if an economy using this currency exists, false otherwise.
     */
    boolean hasEconomy(@NotNull Currency currency);

    /**
     * Retrieves an economy by its currency.
     *
     * @param currency The currency of the economy to retrieve.
     * @return The economy.
     */
    @NotNull
    Optional<Economy> getEconomy(@NotNull Currency currency);

    /**
     * Retrieves an economy by its currency.
     *
     * @param currency The currency of the economy to retrieve.
     * @return The economy.
     */
    @Nullable
    default Economy fetchEconomy(@NotNull Currency currency) {
        return this.getEconomy(currency).orElse(null);
    }
}
