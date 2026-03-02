package zcrys.api.koth;

/**
 * Represents statistics for a KOTH in KothWars.
 *
 * This object contains all tracked statistics for a specific KOTH
 * across all events and captures.
 */
public interface KothStats {

    /**
     * Gets the name of the KOTH.
     */
    String getKothName();

    /**
     * Gets the total number of times this KOTH has been captured.
     */
    int getTotalCaptures();

    /**
     * Gets the total number of kills that have occurred inside this KOTH zone.
     */
    int getTotalKills();

    /**
     * Gets the total number of deaths that have occurred inside this KOTH zone.
     */
    int getTotalDeaths();

    /**
     * Gets the total number of times this KOTH has been activated (started).
     *
     * This includes both manual starts and automatic scheduled starts.
     */
    int getTotalActivations();

    /**
     * Gets the total time this KOTH has been active (in seconds).
     *
     * The counter runs while the KOTH is active and stops when it's deactivated.
     */
    long getTotalActiveTime();

    /**
     * Gets the average number of kills per activation.
     *
     * Calculated as: totalKills / totalActivations (if activations > 0),
     * or 0 if no activations.
     *
     * Average kills per activation rounded to 2 decimal places.
     */
    double getAverageKillsPerActivation();

    /**
     * Gets the average number of deaths per activation.
     *
     * Calculated as: totalDeaths / totalActivations (if activations > 0),
     * or 0 if no activations.
     *
     * Average deaths per activation rounded to 2 decimal places.
     */
    double getAverageDeathsPerActivation();

    /**
     * Gets the total active time formatted as a human-readable string.
     *
     * Format examples:
     * - If > 1 hour: "2h 15m"
     * - If > 1 minute: "45m"
     * - Otherwise: "30s"
     */
    String getFormattedActiveTime();
}