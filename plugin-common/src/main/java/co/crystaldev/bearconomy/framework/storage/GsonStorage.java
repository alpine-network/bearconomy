package co.crystaldev.bearconomy.framework.storage;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.Initializable;
import co.crystaldev.alpinecore.framework.storage.AlpineStore;
import co.crystaldev.alpinecore.framework.storage.driver.FlatfileDriver;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.Reasons;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.Result;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.framework.BearconomyPlugin;
import co.crystaldev.bearconomy.framework.config.Config;
import co.crystaldev.bearconomy.party.Party;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @since 0.1.0
 */
public final class GsonStorage extends EconomyStorage {

    private final Store store;

    public GsonStorage(@NotNull String id, @NotNull Currency currency, @NotNull EconomyConfig config) {
        super(id, currency, config);

        BearconomyPlugin instance = BearconomyPlugin.getInstance();
        this.store = new Store(instance, id);
        instance.addActivatable(this.store);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Party party) {
        if (!this.store.has(party.getId())) {

            // Set to the default balance
            BearconomyPlugin plugin = BearconomyPlugin.getInstance();
            Config config = plugin.getConfiguration(Config.class);
            this.store.put(party.getId(), config.defaultBalance);

            // Return the default balance
            return config.defaultBalance;
        }

        return this.store.get(party.getId());
    }

    @Override
    public @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        BigDecimal balance = this.getBalance(party);
        BigDecimal newBalance = balance.add(transaction.getAmount());

        String reason = null;
        if (this.config.hasMaxBalance() && newBalance.compareTo(this.config.getMaxBalance()) > 0) {
            if (force) {
                reason = Reasons.OVERSIZE_BALANCE;
                newBalance = this.config.getMaxBalance();
            }
            else {
                return new Response(party, transaction, balance, balance, Reasons.OVERSIZE_BALANCE, Result.FAILURE);
            }
        }

        this.store.put(party.getId(), newBalance);
        return new Response(party, transaction, balance, newBalance, reason, Result.SUCCESS);
    }

    @Override
    public @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        BigDecimal balance = this.getBalance(party);
        BigDecimal newBalance = balance.subtract(transaction.getAmount());

        if (!force && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return new Response(party, transaction, balance, balance, Reasons.INSUFFICIENT_BALANCE, Result.FAILURE);
        }

        this.store.put(party.getId(), newBalance);
        return new Response(party, transaction, balance, newBalance, null, Result.SUCCESS);
    }

    private static final class Store extends AlpineStore<UUID, BigDecimal> implements Initializable {

        Store(@NotNull AlpinePlugin plugin, @NotNull  String id) {
            super(plugin, FlatfileDriver.<UUID, BigDecimal>builder()
                    .directory(new File(plugin.getDataFolder(), "storage/" + id))
                    .dataType(BigDecimal.class)
                    .build(plugin));
            this.activate(plugin);
        }

        @Deprecated
        Store(@NotNull AlpinePlugin plugin) {
            super(plugin, FlatfileDriver.<UUID, BigDecimal>builder()
                    .directory(new File(plugin.getDataFolder(), "storage"))
                    .dataType(BigDecimal.class)
                    .build(plugin));
            // AlpineCore requires a constructor
        }

        @Override
        public boolean init() {
            // Do not let AlpineCore initialize this
            return false;
        }
    }
}
