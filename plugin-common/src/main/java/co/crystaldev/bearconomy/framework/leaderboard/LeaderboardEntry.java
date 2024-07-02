package co.crystaldev.bearconomy.framework.leaderboard;

import co.crystaldev.bearconomy.party.Party;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @since 0.1.0
 */
@Data
public final class LeaderboardEntry {
    private final Party party;
    private final BigDecimal balance;
    private int position;
}
