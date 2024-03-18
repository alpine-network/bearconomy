package co.crystaldev.bearconomy;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @since 0.1.0
 */
final class Reference {
    @NotNull
    static final Bearconomy BEARCONOMY = Optional
            .ofNullable((Bearconomy) Bukkit.getPluginManager().getPlugin("Bearconomy"))
            .orElseThrow(() -> new IllegalStateException("Attempted to access Bearconomy prior to initialization"));
}
