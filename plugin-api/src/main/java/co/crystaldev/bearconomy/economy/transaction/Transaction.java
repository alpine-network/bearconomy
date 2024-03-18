package co.crystaldev.bearconomy.economy.transaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Defines the structure and functionality of an economic transaction.
 * <br>
 * Transactions represent changes in balance and are characterized by an amount and a reason.
 *
 * @since 0.1.0
 */
public interface Transaction {

    /**
     * Retrieves the monetary amount involved in this transaction. Transactions cannot have negative amounts.
     *
     * @return The transaction amount.
     */
    @NotNull
    BigDecimal getAmount();

    /**
     * Provides the reason or context for the transaction (e.g., "crate winning", "player payment").
     *
     * @return The reason for the transaction.
     */
    @Nullable
    String getReason();

    @NotNull
    static Transaction of(@NotNull BigDecimal amount, @Nullable String reason) {
        return new BasicTransaction(amount, reason);
    }

    @NotNull
    static Transaction of(@NotNull BigDecimal amount) {
        return new BasicTransaction(amount, null);
    }

    @NotNull
    static Transaction of(double amount, @Nullable String reason) {
        return new BasicTransaction(new BigDecimal(amount), reason);
    }

    @NotNull
    static Transaction of(double amount) {
        return new BasicTransaction(new BigDecimal(amount), null);
    }
}
