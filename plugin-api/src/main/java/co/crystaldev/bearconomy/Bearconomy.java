/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy;

import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.currency.Currency;
import org.bukkit.Bukkit;
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
    static @NotNull Bearconomy get() {
        return (Bearconomy) Bukkit.getPluginManager().getPlugin("Bearconomy");
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
     * @param id       The unique identifier for the economy.
     * @param currency The currency used by this economy.
     * @param config   The configuration settings for the economy.
     */
    void registerEconomy(@NotNull String id, @NotNull Currency currency, @Nullable EconomyConfig config);

    /**
     * Registers a new, managed economy with default configuration.
     *
     * @param id       The unique identifier for the economy.
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
    default @NotNull Economy getEconomy() {
        return this.getEconomy(Economy.DEFAULT_ID)
                .orElseThrow(() -> new IllegalStateException("no default currency registered"));
    }

    /**
     * Retrieves the experience economy.
     *
     * @return The default experience Economy instance.
     */
    default @NotNull Economy getExperienceEconomy() {
        return this.getEconomy(Economy.EXPERIENCE_ID)
                .orElseThrow(() -> new IllegalStateException("no experience currency registered"));
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
    @NotNull Optional<Economy> getEconomy(@NotNull String id);

    /**
     * Retrieves an optional economy by its identifier.
     *
     * @param id The economy identifier.
     * @return The economy.
     */
    default @Nullable Economy fetchEconomy(@NotNull String id) {
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
    @NotNull Optional<Economy> getEconomy(@NotNull Currency currency);

    /**
     * Retrieves an economy by its currency.
     *
     * @param currency The currency of the economy to retrieve.
     * @return The economy.
     */
    default @Nullable Economy fetchEconomy(@NotNull Currency currency) {
        return this.getEconomy(currency).orElse(null);
    }
}
