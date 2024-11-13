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
