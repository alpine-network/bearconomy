/*
 * This file is part of Bearconomy - https://github.com/alpine-network/bearconomy
 * Copyright (C) 2025 Crystal Development, LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package co.crystaldev.bearconomy.party;

/**
 * @since 0.1.1
 */
public interface ExperienceHolder {

    /**
     * Retrieves the total amount of experience.
     *
     * @return the experience level.
     */
    int getExperience();

    /**
     * Sets the experience level.
     *
     * @param level The experience level.
     */
    void setExperience(int level);
}
