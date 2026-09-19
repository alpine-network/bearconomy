/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.economy;

import de.exlll.configlib.Configuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Configuration class for economy settings.
 *
 * @since 0.1.0
 */
@Getter @AllArgsConstructor @NoArgsConstructor
@Configuration
public class EconomyConfig {

    /**
     * The maximum balance a party can hold, or null if there is no limit.
     */
    private @Nullable BigDecimal maxBalance;

    /**
     * The default balance a party is given initially, zero if unset.
     */
    private @NotNull BigDecimal defaultBalance = BigDecimal.ZERO;

    /**
     * Whether the maximum balance should be enforced.
     */
    private boolean enforceMaxBalance;

    /**
     * Whether this currency can be transferred between parties.
     */
    private boolean transferable;

    /**
     * Checks if a maximum balance limit is set.
     *
     * @return True if there is a maximum balance limit, false otherwise.
     */
    public boolean hasMaxBalance() {
        return this.enforceMaxBalance && this.maxBalance != null;
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private BigDecimal maxBalance, defaultBalance = BigDecimal.ZERO;
        private boolean enforceMaxBalance, transferable;

        public @NotNull Builder maxBalance(@Nullable BigDecimal balance) {
            this.maxBalance = balance == null ? BigDecimal.ZERO : balance;
            return this;
        }

        public @NotNull Builder maxBalance(double balance) {
            return this.maxBalance(new BigDecimal(balance));
        }

        public @NotNull Builder defaultBalance(@Nullable BigDecimal balance) {
            this.defaultBalance = balance == null ? BigDecimal.ZERO : balance;
            return this;
        }

        public @NotNull Builder defaultBalance(double balance) {
            return this.defaultBalance(new BigDecimal(balance));
        }

        public @NotNull Builder enforceMaxBalance(boolean enforce) {
            this.enforceMaxBalance = enforce;
            return this;
        }

        public @NotNull Builder enforceMaxBalance() {
            return this.enforceMaxBalance(true);
        }

        public @NotNull Builder transferableBetweenPlayers(boolean transferable) {
            this.transferable = transferable;
            return this;
        }

        public @NotNull Builder transferableBetweenPlayers() {
            return this.transferableBetweenPlayers(true);
        }

        public @NotNull EconomyConfig build() {
            return new EconomyConfig(this.maxBalance, this.defaultBalance, this.enforceMaxBalance, this.transferable);
        }
    }
}
