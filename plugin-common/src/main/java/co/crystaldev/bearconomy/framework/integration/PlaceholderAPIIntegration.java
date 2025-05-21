package co.crystaldev.bearconomy.framework.integration;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.integration.AlpineIntegration;
import co.crystaldev.alpinecore.framework.integration.AlpineIntegrationEngine;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.1.0
 * @see <a href="https://github.com/PlaceholderAPI/Vault-Expansion/blob/master/src/main/java/at/helpch/placeholderapi/expansion/vault/EconomyHook.java">PAPI Vault Expansion</a>
 */
public final class PlaceholderAPIIntegration extends AlpineIntegration {
    PlaceholderAPIIntegration(AlpinePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean shouldActivate() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    protected @NotNull Class<? extends AlpineIntegrationEngine> getEngineClass() {
        return Engine.class;
    }

    public static final class Engine extends AlpineIntegrationEngine {
        Engine(AlpinePlugin plugin) {
            super(plugin);
            new BearconomyPlaceholderExpansion().register();
        }
    }
}
