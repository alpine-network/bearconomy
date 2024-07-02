package co.crystaldev.bearconomy.economy;

import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Represents the result of an economic transaction, detailing the outcomes and changes.
 *
 * @since 0.1.0
 */
@Data
public class Response {

    /**
     * The party involved in the transaction.
     */
    private final @NotNull Party party;

    /**
     * The transaction details.
     */
    private final @NotNull Transaction transaction;

    /**
     * The party's balance before the transaction.
     */
    private final @NotNull BigDecimal previousBalance;

    /**
     * The party's balance after the transaction.
     */
    private final @NotNull BigDecimal newBalance;

    /**
     * The reason for the transaction result, if any.
     */
    private final @Nullable String reason;

    /**
     * The outcome of the transaction.
     */
    private final Result result;

    /**
     * Checks if the transaction was successful.
     *
     * @return True if the transaction succeeded, false otherwise.
     */
    public boolean succeeded() {
        return this.result == Result.SUCCESS;
    }

    /**
     * Checks if the transaction failed.
     *
     * @return True if the transaction failed, false otherwise.
     */
    public boolean failed() {
        return this.result == Result.FAILURE || this.result == Result.NOT_IMPLEMENTED;
    }

    /**
     * Provides a reason for the transaction result.
     *
     * @return The reason for the transaction result.
     * @see Reasons
     */
    public @NotNull String getReason() {
        return this.result == Result.NOT_IMPLEMENTED ? Reasons.NOT_IMPLEMENTED : this.reason == null ? "" : this.reason;
    }

    /**
     * Retrieves the previous balance as a double.
     *
     * @return The previous balance.
     */
    public double getPreviousBalanceAsDouble() {
        return this.previousBalance.doubleValue();
    }

    /**
     * Retrieves the new balance as a double.
     *
     * @return The new balance.
     */
    public double getNewBalanceAsDouble() {
        return this.newBalance.doubleValue();
    }
}
