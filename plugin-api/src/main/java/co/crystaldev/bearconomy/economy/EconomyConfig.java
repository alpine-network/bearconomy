package co.crystaldev.bearconomy.economy;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Configuration class for economy settings.
 *
 * @since 0.1.0
 */
@Data
public class EconomyConfig {

    /**
     * The maximum balance a party can hold, or null if there is no limit.
     */
    private final @Nullable BigDecimal maxBalance;

    /**
     * Checks if a maximum balance limit is set.
     *
     * @return True if there is a maximum balance limit, false otherwise.
     */
    public boolean hasMaxBalance() {
        return this.maxBalance != null;
    }
}
