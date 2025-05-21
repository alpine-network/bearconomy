package co.crystaldev.bearconomy.framework.integration;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.integration.AlpineIntegration;
import co.crystaldev.alpinecore.framework.integration.AlpineIntegrationEngine;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.1.0
 */
public final class VaultIntegration extends AlpineIntegration {
    VaultIntegration(AlpinePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean shouldActivate() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    @Override
    protected @NotNull Class<? extends AlpineIntegrationEngine> getEngineClass() {
        return VaultIntegrationEngine.class;
    }
}
