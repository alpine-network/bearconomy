package co.crystaldev.bearconomy.framework;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.EconomyConfig;
import co.crystaldev.bearconomy.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @since 0.1.0
 */
public final class BearconomyPlugin extends AlpinePlugin implements Bearconomy {

    private final Map<String, Economy> idToEconomy = new HashMap<>();

    private final Map<Currency, Economy> currencyToEconomy = new HashMap<>();

    @Override
    public void registerEconomy(@NotNull Economy economy) {
        this.log(String.format("Registering economy %s", economy.getId()));
        this.idToEconomy.put(economy.getId(), economy);
        this.currencyToEconomy.put(economy.getCurrency(), economy);
    }

    @Override
    public void registerEconomy(@NotNull String id, @NotNull Currency currency, @Nullable EconomyConfig config) {
        this.log(String.format("Registering managed economy %s", id));

        ManagedEconomy economy = new ManagedEconomy(id, currency);
        this.idToEconomy.put(id, economy);
        this.currencyToEconomy.put(currency, economy);
    }

    @Override
    public boolean hasEconomy(@NotNull String id) {
        return this.idToEconomy.containsKey(id);
    }

    @Override
    public @NotNull Optional<Economy> getEconomy(@NotNull String id) {
        return Optional.ofNullable(this.idToEconomy.get(id));
    }

    @Override
    public boolean hasEconomy(@NotNull Currency currency) {
        return this.currencyToEconomy.containsKey(currency);
    }

    @Override
    public @NotNull Optional<Economy> getEconomy(@NotNull Currency currency) {
        return Optional.ofNullable(this.currencyToEconomy.get(currency));
    }
}
