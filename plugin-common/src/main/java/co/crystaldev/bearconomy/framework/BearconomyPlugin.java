/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.framework;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.framework.config.Config;
import co.crystaldev.bearconomy.framework.economy.ExperienceEconomy;
import co.crystaldev.bearconomy.framework.economy.ManagedEconomy;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @since 0.1.0
 */
public final class BearconomyPlugin extends AlpinePlugin implements Bearconomy {

    private static final long PERSIST_TASK_PERIOD = 3600L; // ~3m in ticks

    @Getter
    private static BearconomyPlugin instance;
    { instance =  this; }

    private final Map<String, Economy> idToEconomy = new HashMap<>();

    private final Map<Currency, Economy> currencyToEconomy = new HashMap<>();

    private int taskId = -1;

    @Override
    public void onStart() {
        this.registerEconomy(Economy.DEFAULT_ID, Economy.DEFAULT_CURRENCY);
        this.registerEconomy(new ExperienceEconomy());

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Economy economy : this.idToEconomy.values()) {
                economy.flush();
            }
        }, 0L, PERSIST_TASK_PERIOD);
    }

    @Override
    public void onStop() {
        if (this.taskId != -1) {
            Bukkit.getScheduler().cancelTask(this.taskId);
        }

        for (Economy economy : this.idToEconomy.values()) {
            economy.flush();
            economy.shutdown();
        }
    }

    @Override
    public void setupVariables(@NotNull VariableConsumer variableConsumer) {
        variableConsumer.addVariable("prefix", "<bracket>[<info>Bearconomy</info>]</bracket>");
        variableConsumer.addVariable("error_prefix", "<bracket>[<error>Bearconomy</error>]</bracket>");
    }

    @Override
    public void registerEconomy(@NotNull Economy economy) {
        this.log(String.format("Registering economy \"%s\"", economy.getId()));
        this.idToEconomy.put(economy.getId(), economy);
        this.currencyToEconomy.put(economy.getCurrency(), economy);
    }

    @Override
    public void registerEconomy(@NotNull String id, @NotNull Currency currency, @Nullable EconomyConfig config) {
        this.log(String.format("Registering managed economy \"%s\"", id));

        Config bearconomyConfig = Config.getInstance();
        if (config == null) {
            config = bearconomyConfig.defaultEconomyConfig;
        }

        ManagedEconomy economy = new ManagedEconomy(id, currency, config);
        this.idToEconomy.put(id, economy);
        this.currencyToEconomy.put(currency, economy);
    }

    @Override
    public boolean hasEconomy(@NotNull String id) {
        return this.idToEconomy.containsKey(id);
    }

    @Override
    public @NotNull Optional<Economy> getEconomy(@NotNull String id) {
        return Optional.ofNullable(this.idToEconomy.get(id));
    }

    @Override
    public boolean hasEconomy(@NotNull Currency currency) {
        return this.currencyToEconomy.containsKey(currency);
    }

    @Override
    public @NotNull Optional<Economy> getEconomy(@NotNull Currency currency) {
        return Optional.ofNullable(this.currencyToEconomy.get(currency));
    }

    @Override
    public @NotNull Map<String, Economy> getEconomies() {
        return this.idToEconomy;
    }

    @Override
    public @NotNull Map<Currency, Economy> getCurrencies() {
        return this.currencyToEconomy;
    }
}
