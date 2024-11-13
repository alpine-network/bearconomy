package co.crystaldev.bearconomy.party;

import co.crystaldev.alpinecore.util.ExpUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * @since 0.1.0
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class PlayerParty implements Party, ExperienceHolder {

    private final OfflinePlayer player;

    @Override
    public @NotNull UUID getId() {
        return this.player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        return Optional.ofNullable(this.player.getName()).orElse(this.getId().toString());
    }

    @Override
    public int getExperience() {
        return this.player.isOnline() ? ExpUtils.getExp(this.player.getPlayer()) : 0;
    }

    @Override
    public void setExperience(int exp) {
        if (!this.player.isOnline()) {
            return;
        }

        if (exp < 0) {
            exp = 0;
        }

        Player player = this.player.getPlayer();
        double levelAndExp = ExpUtils.getLevelFromExp(exp);
        int level = (int) levelAndExp;
        player.setLevel(level);
        player.setExp((float)(levelAndExp - (double)level));
    }
}
