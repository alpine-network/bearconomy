/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.framework.economy;

import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.framework.config.Config;
import co.crystaldev.bearconomy.framework.storage.EconomyStorage;
import co.crystaldev.bearconomy.framework.storage.GenericStorage;
import co.crystaldev.bearconomy.framework.storage.GsonStorage;
import co.crystaldev.bearconomy.framework.storage.MySQLStorage;
import co.crystaldev.bearconomy.party.Party;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * @since 0.1.0
 */
@Getter
public final class ManagedEconomy implements Economy {

    private final String id;

    private final Currency currency;

    private final EconomyConfig config;

    private final EconomyStorage storage;

    public ManagedEconomy(@NotNull String id, @NotNull Currency currency, @Nullable EconomyConfig economyConfig) {
        this.id = id;
        this.currency = currency;
        this.config = economyConfig == null ? new EconomyConfig(null) : economyConfig;

        Config config = Config.getInstance();
        switch (config.storageType) {
            case MYSQL:
                this.storage = new MySQLStorage(id, currency, this.config);
                break;
            case JSON:
                this.storage = new GsonStorage(id, currency, this.config);
                break;
            default:
                this.storage = new GenericStorage(id, currency, this.config);
        }
    }

    @Override
    public boolean isEnabled() {
        return this.storage.isEnabled();
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Party party) {
        return this.storage.getBalance(party);
    }

    @Override
    public @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        return this.storage.deposit(party, transaction, force);
    }

    @Override
    public @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        return this.storage.withdraw(party, transaction, force);
    }

    @Override
    public void flush() {
        this.storage.flush();
    }
}
