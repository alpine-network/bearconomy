/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.framework.integration;

import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.framework.BearconomyPlugin;
import co.crystaldev.bearconomy.party.Party;
import com.google.common.collect.ImmutableMap;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @since 1.0.4
 */
final class BearconomyPlaceholderExpansion extends PlaceholderExpansion implements Configurable {

    private static final List<String> PLACEHOLDERS = Arrays.asList(
            "%bearconomy_balance%",
            "%bearconomy_balance_fixed%",
            "%bearconomy_balance_formatted%",
            "%bearconomy_balance_commas%"
    );

    private static final DecimalFormat PRIMARY_FORMAT = new DecimalFormat("0.##");

    private static final DecimalFormat COMMAS_FORMAT = new DecimalFormat("#,###");

    private static final DecimalFormat FIXED_FORMAT = new DecimalFormat("#");

    private static final NavigableMap<Long, String> SUFFIXES = new TreeMap<>();

    {
        SUFFIXES.put(1_000L, this.getString("formatting.thousands", "K"));
        SUFFIXES.put(1_000_000L, this.getString("formatting.millions", "M"));
        SUFFIXES.put(1_000_000_000L, this.getString("formatting.billions", "B"));
        SUFFIXES.put(1_000_000_000_000L, this.getString("formatting.trillions", "T"));
        SUFFIXES.put(1_000_000_000_000_000L, this.getString("formatting.quadrillions", "Q"));
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bearconomy";
    }

    @Override
    public @NotNull String getVersion() {
        return BearconomyPlugin.getInstance().getDescription().getVersion();
    }

    @Override
    public @NotNull String getAuthor() {
        return "BestBearr";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return PLACEHOLDERS;
    }

    @Override
    public Map<String, Object> getDefaults() {
        return ImmutableMap.<String, Object>builder()
                .put("formatting.thousands", "k")
                .put("formatting.millions", "M")
                .put("formatting.billions", "B")
                .put("formatting.trillions", "T")
                .put("formatting.quadrillions", "Q")
                .build();
    }

    @Override
    public @Nullable String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        Bearconomy bearconomy = Bearconomy.get();

        // handle other economies, for example %bearconomy_balance_souls% and %bearconomy_balance_souls_formatted%
        if (params.startsWith("balance_")) {
            String[] split = params.split("_", 3);
            Optional<Economy> economy = bearconomy.getEconomy(split[1]);

            if (economy.isPresent()) {
                BigDecimal balance = economy.get().getBalance(Party.player(player));

                switch (split.length < 3 ? "" : split[2]) {
                    case "fixed":
                        return FIXED_FORMAT.format(balance);
                    case "formatted":
                        return this.formatBalance(balance.longValue());
                    case "commas":
                        return COMMAS_FORMAT.format(balance);
                    default:
                        return PRIMARY_FORMAT.format(balance);
                }
            }
        }

        BigDecimal balance = bearconomy.getEconomy().getBalance(Party.player(player));

        switch (params) {
            case "balance":
                return PRIMARY_FORMAT.format(balance);
            case "balance_fixed":
                return FIXED_FORMAT.format(balance);
            case "balance_formatted":
                return this.formatBalance(balance.longValue());
            case "balance_commas":
                return COMMAS_FORMAT.format(balance);
            default:
                return null;
        }
    }

    /**
     * Format player's balance, 1200 -> 1.2K
     *
     * @param balance balance to format
     * @return balance formatted
     * @author <a href="https://stackoverflow.com/users/829571/assylias">assylias</a> (<a href="https://stackoverflow.com/a/30661479/11496439">source</a>)
     */
    @NotNull
    private String formatBalance(long balance) {
        //Long.MIN_VALUE == -Long.MIN_VALUE, so we need an adjustment here
        if (balance == Long.MIN_VALUE) {
            return formatBalance(Long.MIN_VALUE + 1);
        }
        if (balance < 0) {
            return "-" + formatBalance(-balance);
        }

        if (balance < 1000) {
            return Long.toString(balance); //deal with easy case
        }

        Map.Entry<Long, String> e = SUFFIXES.floorEntry(balance);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = balance / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}
