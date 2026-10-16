/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.framework.storage;

import co.crystaldev.alpinecore.util.UuidTypeAdapter;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.Reasons;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.Result;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.framework.BearconomyPlugin;
import co.crystaldev.bearconomy.party.Party;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * @since 0.1.0
 */
public final class FlatFileStorage extends EconomyStorage {

    private final File directory;

    private final Map<UUID, BigDecimal> balances = new ConcurrentHashMap<>();

    private final Set<UUID> dirty = ConcurrentHashMap.newKeySet();

    public FlatFileStorage(@NotNull String id, @NotNull Currency currency, @NotNull EconomyConfig config) {
        super(id, currency, config);

        this.directory = new File(BearconomyPlugin.getInstance().getDataFolder(), "storage/" + id);
        if (!this.directory.exists() && !this.directory.mkdirs()) {
            BearconomyPlugin.getInstance().log(Level.SEVERE,
                    String.format("&cUnable to create storage directory for the &d%s &ceconomy", id));
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Party party) {
        return this.balances.computeIfAbsent(party.getId(), this::load);
    }

    @Override
    public @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        BigDecimal balance = this.getBalance(party);
        BigDecimal newBalance = balance.add(transaction.getAmount());

        String reason = null;
        if (this.config.hasMaxBalance() && newBalance.compareTo(this.config.getMaxBalance()) > 0) {
            if (force) {
                reason = Reasons.OVERSIZE_BALANCE;
                newBalance = this.config.getMaxBalance();
            }
            else {
                return new Response(party, transaction, balance, balance, Reasons.OVERSIZE_BALANCE, Result.FAILURE);
            }
        }

        this.put(party.getId(), newBalance);
        return new Response(party, transaction, balance, newBalance, reason, Result.SUCCESS);
    }

    @Override
    public @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        BigDecimal balance = this.getBalance(party);
        BigDecimal newBalance = balance.subtract(transaction.getAmount());

        if (!force && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return new Response(party, transaction, balance, balance, Reasons.INSUFFICIENT_BALANCE, Result.FAILURE);
        }

        this.put(party.getId(), newBalance);
        return new Response(party, transaction, balance, newBalance, null, Result.SUCCESS);
    }

    @Override
    public void flush() {
        Iterator<UUID> iterator = this.dirty.iterator();
        while (iterator.hasNext()) {
            UUID key = iterator.next();
            iterator.remove();

            BigDecimal balance = this.balances.get(key);
            if (balance != null && !this.save(key, balance)) {
                this.dirty.add(key);
            }
        }
    }

    private void put(@NotNull UUID key, @NotNull BigDecimal balance) {
        this.balances.put(key, balance);
        this.dirty.add(key);
    }

    private @NotNull BigDecimal load(@NotNull UUID key) {
        File file = this.getFile(key);
        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim();
                return new BigDecimal(content);
            }
            catch (Exception ex) {
                BearconomyPlugin.getInstance().log(String.format("&cError reading balance for &d%s &cin the &d%s &ceconomy", key, this.id), ex);
            }
        }

        this.dirty.add(key);
        return this.config.getDefaultBalance();
    }

    private boolean save(@NotNull UUID key, @NotNull BigDecimal balance) {
        Path target = this.getFile(key).toPath();
        Path temp = target.resolveSibling(target.getFileName() + ".tmp");
        try {
            Files.write(temp, balance.toPlainString().getBytes(StandardCharsets.UTF_8));
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            return true;
        }
        catch (IOException ex) {
            BearconomyPlugin.getInstance().log(String.format("&cError saving balance for &d%s &cin the &d%s &ceconomy", key, this.id), ex);
            return false;
        }
    }

    private @NotNull File getFile(@NotNull UUID key) {
        return new File(this.directory, UuidTypeAdapter.fromUUID(key) + ".json");
    }
}
