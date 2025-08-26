/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
