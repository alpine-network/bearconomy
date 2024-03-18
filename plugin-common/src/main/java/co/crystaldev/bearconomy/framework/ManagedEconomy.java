package co.crystaldev.bearconomy.framework;

import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * @since 0.1.0
 */
@AllArgsConstructor @Getter
public final class ManagedEconomy implements Economy {

    private final String id;

    private final Currency currency;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Party party) {
        return null;
    }

    @Override
    public @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction) {
        return null;
    }

    @Override
    public @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction) {
        return null;
    }
}
