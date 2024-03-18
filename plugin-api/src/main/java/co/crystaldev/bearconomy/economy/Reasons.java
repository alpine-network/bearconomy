package co.crystaldev.bearconomy.economy;

import lombok.experimental.UtilityClass;

/**
 * @since 0.1.0
 */
@UtilityClass
public final class Reasons {
    public static final String NOT_IMPLEMENTED = "bearconomy: not implemented";

    public static final String OVERSIZE_BALANCE = "bearconomy: oversize balance";

    public static final String INSUFFICIENT_BALANCE = "bearconomy: insufficient balance";

    public static final String TRANSACTION_ERROR = "bearconomy: transaction failure";
}
