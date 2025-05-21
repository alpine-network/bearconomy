package co.crystaldev.example;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.example.economy.GemCurrency;

public final class BearconomyExamplePlugin extends AlpinePlugin {

    @Override
    public void onStart() {
        // Register our custom 'gem' economy

        Bearconomy bearconomy = Bearconomy.get();
        bearconomy.registerEconomy("gem", new GemCurrency());
    }
}
