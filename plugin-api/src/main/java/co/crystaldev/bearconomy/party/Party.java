/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.party;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Represents a party or entity within the economy system, such as a player or group,
 * that can hold a balance or participate in transactions.
 *
 * @since 0.1.0
 */
public interface Party {

    /**
     * Gets the unique identifier of this party.
     *
     * @return The identifier of the party.
     */
    @NotNull UUID getId();

    /**
     * Retrieves the name of this party.
     *
     * @return The name of this party.
     */
    @NotNull String getName();

    @NotNull static Party player(@NotNull OfflinePlayer player) {
        return new PlayerParty(player);
    }

    @NotNull static Party id(@NotNull UUID id) {
        return new GenericParty(id);
    }

    static @NotNull Stream<Party> allPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Party::player);
    }
}
