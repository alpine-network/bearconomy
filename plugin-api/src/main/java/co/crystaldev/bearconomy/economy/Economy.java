/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.economy;

import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.currency.DefaultCurrency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Interface defining the core functionalities of an economy system.
 *
 * @since 0.1.0
 */
public interface Economy {

    String DEFAULT_ID = "default";

    String EXPERIENCE_ID = "experience";

    Currency DEFAULT_CURRENCY = new DefaultCurrency(DEFAULT_ID);

    /**
     * Returns the unique identifier for this economy.
     *
     * @return The unique identifier.
     */
    @NotNull String getId();

    /**
     * Retrieves the configuration used by this economy.
     *
     * @return The current {@link EconomyConfig} instance.
     */
    @NotNull EconomyConfig getConfig();

    /**
     * Checks if the economy system is currently enabled.
     *
     * @return True if enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Retrieves the currency used by this economy.
     *
     * @return The current {@link Currency} instance.
     */
    @NotNull Currency getCurrency();

    /**
     * Gets the current balance of a party.
     *
     * @param party The party whose balance is being queried.
     * @return The balance as a {@link BigDecimal}.
     */
    @NotNull BigDecimal getBalance(@NotNull Party party);

    /**
     * Checks if a party has at least a certain balance.
     *
     * @param party   The party to check.
     * @param balance The minimum balance to check for.
     * @return True if the party has at least the specified balance.
     */
    default boolean hasBalance(@NotNull Party party, @NotNull BigDecimal balance) {
        return this.getBalance(party).compareTo(balance) >= 0;
    }

    /**
     * Checks if a party has at least a certain balance.
     *
     * @param party   The party to check.
     * @param balance The minimum balance to check for, as a double.
     * @return True if the party has at least the specified balance.
     */
    default boolean hasBalance(@NotNull Party party, double balance) {
        return this.hasBalance(party, new BigDecimal(balance));
    }

    /**
     * Deposits an amount to a party's balance.
     *
     * @param party       The party to deposit to.
     * @param transaction The transaction details.
     * @param force       Whether to force the transaction.
     * @return A {@link Response} indicating the outcome.
     */
    @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force);

    /**
     * Deposits an amount to a party's balance.
     *
     * @param party       The party to deposit to.
     * @param transaction The transaction details.
     * @return A {@link Response} indicating the outcome.
     */
    default @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction) {
        return this.deposit(party, transaction, false);
    }

    /**
     * Withdraws an amount from a party's balance.
     *
     * @param party       The party to withdraw from.
     * @param transaction The transaction details.
     * @param force       Whether to force the transaction.
     * @return A {@link Response} indicating the outcome.
     */
    @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force);

    /**
     * Withdraws an amount from a party's balance.
     *
     * @param party       The party to withdraw from.
     * @param transaction The transaction details.
     * @return A {@link Response} indicating the outcome.
     */
    default @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction) {
        return this.withdraw(party, transaction, false);
    }

    /**
     * Sets a party's balance to a specific amount.
     *
     * @param party  The party whose balance is being set.
     * @param amount The new balance amount.
     * @param force  Whether to force the transaction.
     * @return A {@link Response} indicating the outcome.
     */
    default @NotNull Response set(@NotNull Party party, @Nullable BigDecimal amount, boolean force) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        Transaction transaction = Transaction.of(this.getBalance(party), "Bearconomy - clearing balance");
        Response response = this.withdraw(party, transaction, force);
        if (response.failed()) {
            return response;
        }

        transaction = Transaction.of(amount, "Bearconomy - setting new balance");
        return this.deposit(party, transaction, force);
    }

    /**
     * Sets a party's balance to a specific amount.
     *
     * @param party  The party whose balance is being set.
     * @param amount The new balance amount.
     * @return A {@link Response} indicating the outcome.
     */
    default @NotNull Response set(@NotNull Party party, @Nullable BigDecimal amount) {
        return this.set(party, amount, false);
    }

    /**
     * Sets a party's balance to a specific amount.
     *
     * @param party  The party whose balance is being set.
     * @param amount The new balance amount.
     * @return A {@link Response} indicating the outcome.
     */
    default @NotNull Response set(@NotNull Party party, double amount) {
        return this.set(party, new BigDecimal(amount));
    }

    /**
     * Flushes data for this economy.
     */
    default void flush() {
        // NO OP
    }

    /**
     * Prepare this economy to be closed.
     */
    default void shutdown() {
        // NO OP
    }
}
