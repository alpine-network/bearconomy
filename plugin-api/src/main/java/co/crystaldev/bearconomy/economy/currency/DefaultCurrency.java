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
import java.text.DecimalFormat;

/**
 * @since 0.1.0
 */
public class DefaultCurrency implements Currency {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");

    private final String id;

    public DefaultCurrency(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull String getSingularName() {
        return "dollar";
    }

    @Override
    public @NotNull String getPluralName() {
        return "dollars";
    }

    @Override
    public @NotNull String getSymbol() {
        return "$";
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount) {
        return this.getSymbol() + DECIMAL_FORMAT.format(amount);
    }

    @Override
    public @NotNull String formatName(@NotNull BigDecimal amount) {
        String name = BigDecimal.ONE.equals(amount) ? this.getSingularName() : this.getPluralName();
        return DECIMAL_FORMAT.format(amount) + " " + name;
    }
}
