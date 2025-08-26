/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.framework.storage;

import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * @since 0.1.0
 */
@AllArgsConstructor
public abstract class EconomyStorage {

    protected final @NotNull String id;

    protected final @NotNull Currency currency;

    protected final @NotNull EconomyConfig config;

    public abstract boolean isEnabled();

    public abstract @NotNull BigDecimal getBalance(@NotNull Party party);

    public abstract @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force);

    public abstract @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force);

    public void flush() {
        // NO OP
    }
}
