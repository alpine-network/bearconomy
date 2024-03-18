package co.crystaldev.bearconomy.party;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * @since 0.1.0
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class PlayerParty implements Party {

    private final OfflinePlayer player;

    @Override
    public @NotNull UUID getId() {
        return this.player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        return Optional.ofNullable(this.player.getName()).orElse(this.getId().toString());
    }
}
