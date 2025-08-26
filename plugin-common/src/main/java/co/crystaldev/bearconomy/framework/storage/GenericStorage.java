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
import co.crystaldev.bearconomy.economy.Result;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.framework.BearconomyPlugin;
import co.crystaldev.bearconomy.party.Party;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.logging.Level;

/**
 * @since 0.1.0
 */
public final class GenericStorage extends EconomyStorage {
    public GenericStorage(@NotNull String id, @NotNull Currency currency, @NotNull EconomyConfig config) {
        super(id, currency, config);

        BearconomyPlugin.getInstance().log(Level.WARNING,
                String.format("No storage provider is being used for the \"%s\" economy", id));
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Party party) {
        return BigDecimal.ZERO;
    }

    @Override
    public @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        return new Response(party, transaction, BigDecimal.ZERO, BigDecimal.ZERO, null, Result.NOT_IMPLEMENTED);
    }

    @Override
    public @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        return new Response(party, transaction, BigDecimal.ZERO, BigDecimal.ZERO, null, Result.NOT_IMPLEMENTED);
    }
}
