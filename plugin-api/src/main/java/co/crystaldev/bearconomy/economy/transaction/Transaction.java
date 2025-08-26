/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.economy.transaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Defines the structure and functionality of an economic transaction.
 * <br>
 * Transactions represent changes in balance and are characterized by an amount and a reason.
 *
 * @since 0.1.0
 */
public interface Transaction {

    /**
     * Retrieves the monetary amount involved in this transaction. Transactions cannot have negative amounts.
     *
     * @return The transaction amount.
     */
    @NotNull BigDecimal getAmount();

    /**
     * Provides the reason or context for the transaction (e.g., "crate winning", "player payment").
     *
     * @return The reason for the transaction.
     */
    @Nullable String getReason();

    static @NotNull Transaction of(@NotNull BigDecimal amount, @Nullable String reason) {
        return new BasicTransaction(amount, reason);
    }

    static @NotNull Transaction of(@NotNull BigDecimal amount) {
        return new BasicTransaction(amount, null);
    }

    static @NotNull Transaction of(double amount, @Nullable String reason) {
        return new BasicTransaction(new BigDecimal(amount), reason);
    }

    static @NotNull Transaction of(double amount) {
        return new BasicTransaction(new BigDecimal(amount), null);
    }
}
