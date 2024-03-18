package co.crystaldev.bearconomy.economy;

import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.currency.DefaultCurrency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Interface defining the core functionalities of an economy system.
 *
 * @since 0.1.0
 */
public interface Economy {

    Currency DEFAULT_CURRENCY = new DefaultCurrency();

    /**
     * Returns the unique identifier for this economy.
     *
     * @return The unique identifier.
     */
    @NotNull
    String getId();

    /**
     * Checks if the economy system is currently enabled.
     *
     * @return True if enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Retrieves the currency used by this economy.
     *
     * @return The current {@link Currency} instance.
     */
    @NotNull
    Currency getCurrency();

    /**
     * Gets the current balance of a party.
     *
     * @param party The party whose balance is being queried.
     * @return The balance as a {@link BigDecimal}.
     */
    @NotNull
    BigDecimal getBalance(@NotNull Party party);

    /**
     * Checks if a party has at least a certain balance.
     *
     * @param party The party to check.
     * @param balance The minimum balance to check for.
     * @return True if the party has at least the specified balance.
     */
    default boolean hasBalance(@NotNull Party party, @NotNull BigDecimal balance) {
        return this.getBalance(party).compareTo(balance) >= 0;
    }

    /**
     * Checks if a party has at least a certain balance.
     *
     * @param party The party to check.
     * @param balance The minimum balance to check for, as a double.
     * @return True if the party has at least the specified balance.
     */
    default boolean hasBalance(@NotNull Party party, double balance) {
        return this.hasBalance(party, new BigDecimal(balance));
    }

    /**
     * Deposits an amount to a party's balance.
     *
     * @param party The party to deposit to.
     * @param transaction The transaction details.
     * @return A {@link Response} indicating the outcome.
     */
    @NotNull
    Response deposit(@NotNull Party party, @NotNull Transaction transaction);

    /**
     * Withdraws an amount from a party's balance.
     *
     * @param party The party to withdraw from.
     * @param transaction The transaction details.
     * @return A {@link Response} indicating the outcome.
     */
    @NotNull
    Response withdraw(@NotNull Party party, @NotNull Transaction transaction);

    /**
     * Sets a party's balance to a specific amount.
     *
     * @param party The party whose balance is being set.
     * @param amount The new balance amount.
     * @return A {@link Response} indicating the outcome.
     */
    @NotNull
    default Response set(@NotNull Party party, @Nullable BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        Transaction transaction = Transaction.of(this.getBalance(party), "Bearconomy - clearing balance");
        Response response = this.withdraw(party, transaction);
        if (response.failed()) {
            return response;
        }

        transaction = Transaction.of(amount, "Bearconomy - setting new balance");
        return this.deposit(party, transaction);
    }

    @NotNull
    default Response set(@NotNull Party party, double amount) {
        return this.set(party, new BigDecimal(amount));
    }
}
