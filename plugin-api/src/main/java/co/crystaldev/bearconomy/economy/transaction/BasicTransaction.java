package co.crystaldev.bearconomy.economy.transaction;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * @since 0.1.0
 */
@Getter @EqualsAndHashCode @ToString
public final class BasicTransaction implements Transaction {

    private final BigDecimal amount;

    private final String reason;

    BasicTransaction(@NotNull BigDecimal amount, @Nullable String reason) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            amount = amount.multiply(new BigDecimal(-1));
        }

        this.amount = amount;
        this.reason = reason;
    }
}