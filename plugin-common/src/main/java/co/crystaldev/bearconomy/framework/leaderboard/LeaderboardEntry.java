package co.crystaldev.bearconomy.framework.leaderboard;

import co.crystaldev.bearconomy.party.Party;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BestBearr <crumbygames12@gmail.com>
 * @since 03/20/2024
 */
@Data
public final class LeaderboardEntry {
    private final Party party;
    private final BigDecimal balance;
    private int position;
}
