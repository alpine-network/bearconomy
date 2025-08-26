/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.framework.leaderboard;

import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.party.Party;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @since 0.1.0
 */
@RequiredArgsConstructor
public final class SortedEconomy {

    // Updates after ten minutes
    private static final long UPDATE_DURATION = TimeUnit.MINUTES.toMillis(10L);

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private final Economy economy;

    private final List<LeaderboardEntry> entries = new LinkedList<>();

    private CompletableFuture<List<LeaderboardEntry>> future;

    private long lastUpdate;

    public @NotNull CompletableFuture<List<LeaderboardEntry>> getSortedEntries() {
        if (System.currentTimeMillis() - this.lastUpdate < UPDATE_DURATION) {
            // return the cached result
            return CompletableFuture.completedFuture(this.entries);
        }
        else if (this.future != null && System.currentTimeMillis() - this.lastUpdate > UPDATE_DURATION) {
            // reset the future and resort
            this.future = null;
        }

        // if sorting, wait for that to conclude
        if (this.future != null) {
            return this.future;
        }

        // refresh entries
        this.future = new CompletableFuture<>();
        this.entries.clear();

        EXECUTOR_SERVICE.submit(() -> {
            // collect all players into the list and sort
            // this is a blocking task

            AtomicInteger counter = new AtomicInteger();
            Stream.of(Bukkit.getOfflinePlayers())
                    .map(Party::player)
                    .map(p -> new LeaderboardEntry(p, this.economy.getBalance(p)))
                    .sorted((v1, v2) -> v2.getBalance().compareTo(v1.getBalance()))
                    .forEachOrdered(entry -> {
                        entry.setPosition(counter.incrementAndGet());
                        this.entries.add(entry);
                    });

            this.lastUpdate = System.currentTimeMillis();
            this.future.complete(this.entries);
        });

        return this.future;
    }
}
