/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.economy.currency;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Represents a currency within the economy system.
 *
 * @since 0.1.0
 */
public interface Currency {

    /**
     * Returns the unique identifier for this currency.
     *
     * @return The currency ID as a {@link String}.
     */
    @NotNull String getId();

    /**
     * Retrieves the singular name of the currency (e.g., "Dollar").
     *
     * @return The singular name as a {@link String}.
     */
    @NotNull String getSingularName();

    /**
     * Retrieves the plural name of the currency (e.g., "Dollars").
     *
     * @return The plural name as a {@link String}.
     */
    @NotNull String getPluralName();

    /**
     * Gets the symbol representing the currency (e.g., "$").
     *
     * @return The currency symbol as a {@link String}.
     */
    @NotNull String getSymbol();

    /**
     * Formats a numerical amount into a string representation, including the currency symbol.
     *
     * @param amount The amount to format.
     * @return The formatted amount as a {@link String}.
     */
    @NotNull String format(@NotNull BigDecimal amount);

    /**
     * Formats a numerical amount into a string representation, including the currency symbol.
     *
     * @param amount The amount to format.
     * @return The formatted amount as a {@link String}.
     */
    default @NotNull String format(double amount) {
        return this.format(new BigDecimal(amount));
    }

    /**
     * Formats the amount into a string representation using the appropriate singular or plural
     * currency name based on the amount.
     *
     * @param amount The amount to format.
     * @return The amount followed by the singular or plural currency name, as applicable.
     */
    @NotNull String formatName(@NotNull BigDecimal amount);

    /**
     * Formats the amount into a string representation using the appropriate singular or plural
     * currency name based on the amount.
     *
     * @param amount The amount to format.
     * @return The amount followed by the singular or plural currency name, as applicable.
     */
    default @NotNull String formatName(double amount) {
        return this.formatName(new BigDecimal(amount));
    }
}
