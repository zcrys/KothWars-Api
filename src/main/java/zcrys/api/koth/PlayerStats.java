package zcrys.api.koth;

import java.util.UUID;

/**
 * Represents statistics for a player in KothWars.
 *
 * This object contains all tracked statistics for a player's
 * performance in KOTH events across the server.
 */
public interface PlayerStats {

    /**
     * Gets the player's UUID.
     */
    UUID getPlayerUUID();

    /**
     * Gets the player's current name.
     */
    String getPlayerName();

    /**
     * Gets the total number of KOTH captures completed by this player.
     */
    int getTotalCaptures();

    /**
     * Gets the total number of kills made by this player inside KOTH zones.
     */
    int getTotalKills();

    /**
     * Gets the total number of deaths suffered by this player inside KOTH zones.
     */
    int getTotalDeaths();

    /**
     * Gets the total number of assists made by this player inside KOTH zones.
     *
     * An assist is counted when the player damages a victim who is
     * later killed by another player within a 20-block radius.
     */
    int getTotalAssists();

    /**
     * Gets the total time this player has spent capturing KOTHs.
     */
    long getTotalTimeCapturing();

    /**
     * Gets the total number of KOTHs this player has participated in.
     *
     * A participation is counted when the player is inside an active
     * KOTH zone at any point during the event.
     */
    int getTotalKothsParticipated();

    /**
     * Gets the player's Kill/Death ratio.
     *
     * Calculated as: totalKills / totalDeaths (if deaths > 0),
     * or totalKills if deaths == 0.
     *
     * Kill/Death ratio rounded to 2 decimal places.
     */
    double getKDR();

    /**
     * Gets the total time formatted as a human-readable string.
     *
     * Format examples:
     * - If > 1 hour: "2h 15m 30s"
     * - If > 1 minute: "15m 30s"
     * - Otherwise: "45s"
     */
    String getFormattedTime();
}