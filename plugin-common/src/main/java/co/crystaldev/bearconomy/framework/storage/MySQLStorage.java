/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.framework.storage;

import co.crystaldev.alpinecore.framework.storage.CachingStrategy;
import co.crystaldev.alpinecore.util.DatabaseConnection;
import co.crystaldev.alpinecore.util.UuidTypeAdapter;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.Reasons;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.Result;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.framework.DatabaseHandler;
import co.crystaldev.bearconomy.party.Party;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @since 0.1.0
 */
public final class MySQLStorage extends EconomyStorage {

    private final DatabaseConnection connection;

    private final LoadingCache<UUID, BigDecimal> readCache;

    private final Map<UUID, BigDecimal> writeCache;

    private volatile boolean initializing;

    public MySQLStorage(@NotNull String id, @NotNull Currency currency, @NotNull EconomyConfig economyConfig) {
        super(id, currency, economyConfig);

        // setup caching
        CachingStrategy strategy = CachingStrategy.builder()
                .expireTime(2L, TimeUnit.HOURS)
                .build();
        this.readCache = CacheBuilder.newBuilder()
                .maximumSize(strategy.getMaximumSize())
                .expireAfterAccess(strategy.getExpireTimeValue(), strategy.getExpireTimeUnit())
                .concurrencyLevel(strategy.getConcurrencyLevel())
                .build(new CacheLoader<UUID, BigDecimal>() {
                    @Override
                    public BigDecimal load(UUID key) throws Exception {
                        if (MySQLStorage.this.writeCache.containsKey(key))
                            return MySQLStorage.this.writeCache.get(key);
                        else
                            return MySQLStorage.this.queryBalance(Party.id(key));
                    }
                });
        this.writeCache = new HashMap<>();

        // connect to the database
        this.connection = DatabaseHandler.getInstance().getSQLConnection();
        new Thread(() -> {
            // ensure the database schema is correct
            this.initializing = true;

            try (Connection connection = this.connection.getConnection()) {
                ensureTable(connection, MySQLStorage.this.currency);
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }

            this.initializing = false;

            // preload players into database
            try (Connection conn = this.connection.getConnection();
                 Statement statement = conn.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM currency_" + currency.getId());
                while (resultSet.next()) {
                    UUID uuid = UuidTypeAdapter.fromString(resultSet.getString("uuid"));
                    BigDecimal balance = resultSet.getBigDecimal("balance");
                    this.readCache.put(uuid, balance);
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean isEnabled() {
        return !this.initializing;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Party party) {
        try {
            return Optional.ofNullable(this.readCache.get(party.getId())).orElse(BigDecimal.ZERO);
        }
        catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        BigDecimal balance = BigDecimal.ZERO;

        if (!this.initializing) {
            balance = this.getBalance(party);
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

            this.writeCache.put(party.getId(), newBalance);
            this.readCache.refresh(party.getId());
            return new Response(party, transaction, balance, newBalance, reason, Result.SUCCESS);
        }

        return new Response(party, transaction, balance, balance, Reasons.TRANSACTION_ERROR, Result.FAILURE);
    }

    @Override
    public @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        BigDecimal balance = BigDecimal.ZERO;

        if (!this.initializing) {
            balance = this.getBalance(party);
            BigDecimal newBalance = balance.subtract(transaction.getAmount());

            if (!force && newBalance.compareTo(BigDecimal.ZERO) < 0) {
                return new Response(party, transaction, balance, balance, Reasons.INSUFFICIENT_BALANCE, Result.FAILURE);
            }

            this.writeCache.put(party.getId(), newBalance);
            this.readCache.refresh(party.getId());
            return new Response(party, transaction, balance, newBalance, null, Result.SUCCESS);
        }

        return new Response(party, transaction, balance, balance, Reasons.TRANSACTION_ERROR, Result.FAILURE);
    }

    @Override
    public void flush() {
        if (this.writeCache.isEmpty()) {
            return;
        }

        try (Connection connection = this.connection.getConnection()) {
            this.writeCache.forEach((k, v) -> {
                this.updateBalance(connection, Party.id(k), v);
                this.readCache.put(k, v);
            });

            this.writeCache.clear();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private @NotNull BigDecimal queryBalance(@NotNull Connection connection, @NotNull Party party) {
        BigDecimal balance = BigDecimal.ZERO;

        String tableName = "currency_" + this.currency.getId();
        String query = String.format("SELECT balance FROM %s WHERE uuid = ?", tableName);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, party.getId().toString());
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                // Balance saved
                balance = resultSet.getBigDecimal("balance");
            }
            else {
                // Set to the default balance
                balance = this.config.getDefaultBalance();
                this.updateBalance(connection, party, balance);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return balance;
    }

    private @NotNull BigDecimal queryBalance(@NotNull Party party) throws SQLException {
        try (Connection connection = this.connection.getConnection()) {
            return this.queryBalance(connection, party);
        }
    }

    private void updateBalance(@NotNull Connection connection, @NotNull Party party, @NotNull BigDecimal newBalance) {
        String tableName = "currency_" + this.currency.getId();
        String upsertSql = String.format("INSERT INTO %s (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = VALUES(balance)", tableName);

        try (PreparedStatement statement = connection.prepareStatement(upsertSql)) {
            statement.setString(1, party.getId().toString());
            statement.setBigDecimal(2, newBalance);
            statement.executeUpdate();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void ensureTable(@NotNull Connection connection, @NotNull Currency currency) throws SQLException {
        String tableName = "currency_" + currency.getId();
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid CHAR(36) UNIQUE NOT NULL, " +
                    "balance DECIMAL(65, 2) NOT NULL" +
                    ")";
            stmt.execute(sql);
        }
    }
}
