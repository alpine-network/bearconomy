package co.crystaldev.bearconomy.framework.leaderboard;

import co.crystaldev.bearconomy.economy.Economy;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @since 0.1.0
 */
public final class Leaderboard {

    private final Map<Economy, SortedEconomy> leaderboards = new HashMap<>();

    public @NotNull CompletableFuture<List<LeaderboardEntry>> getForEconomy(@NotNull Economy economy) {
        return this.leaderboards.computeIfAbsent(economy, SortedEconomy::new).getSortedEntries();
    }
}
