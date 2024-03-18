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
    @NotNull
    UUID getId();

    /**
     * Retrieves the name of this party.
     *
     * @return The name of this party.
     */
    @NotNull
    String getName();

    @NotNull
    static PlayerParty player(@NotNull OfflinePlayer player) {
        return new PlayerParty(player);
    }

    @NotNull
    static Stream<PlayerParty> allPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Party::player);
    }
}
