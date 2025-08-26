/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
