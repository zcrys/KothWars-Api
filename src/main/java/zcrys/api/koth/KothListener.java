package zcrys.api.koth;

import org.bukkit.entity.Player;

/**
 * Listener interface to receive events related to KOTHs.
 * All methods have empty default implementations, so you only need
 * to override the events you are interested in.

 *
 * Implementation example:
 * public class MyListener implements KothListener {
 *
 *     &#64;Override
 *     public void onCaptureStart(Koth koth, Player player) {
 *         // Runs when a player starts capturing
 *         Bukkit.broadcastMessage(player.getName() + " is capturing " + koth.getName());
 *     }
 *
 *     &#64;Override
 *     public void onCaptureComplete(Koth koth, Player player, int rewardsCount) {
 *         // Runs when a player completes the capture
 *         player.sendMessage("You have captured " + koth.getName() + "!");
 *     }
 * }
 *
 */
public interface KothListener {

    /**
     * Called when a KOTH is started manually by a player.
     *
     * @param koth The KOTH that was started
     * @param startedBy The player who started the KOTH
     */
    default void onKothStart(Koth koth, Player startedBy) {}

    /**
     * Called when a KOTH starts automatically through the scheduling system.
     *
     * @param koth The KOTH that started automatically
     */
    default void onKothStartAuto(Koth koth) {}

    /**
     * Called when a KOTH is stopped, either manually or by the system.
     *
     * @param koth The KOTH that was stopped
     * @param reason Reason for stopping (e.g. "manual", "auto-suspend", "error")
     * @param stoppedBy The player who stopped the KOTH, or null if it was automatic
     */
    default void onKothStop(Koth koth, String reason, Player stoppedBy) {}

    /**
     * Called when a KOTH is suspended due to inactivity.
     * This happens when there are no players in the area for the time
     * configured in autoSuspendTime.
     *
     * @param koth The KOTH that was suspended
     */
    default void onKothSuspend(Koth koth) {}

    /**
     * Called when a player begins capturing a KOTH.
     * This occurs when a player enters the zone and there is no
     * active capturer, or when the current capturer leaves and
     * a new player takes their place.
     *
     * @param koth The KOTH being captured
     * @param player The player who started capturing
     */
    default void onCaptureStart(Koth koth, Player player) {}

    /**
     * Called every second during capture to report progress.
     * This event fires on every capture tick (each second)
     * while a player is actively capturing.
     *
     * @param koth The KOTH being captured
     * @param player The player who is capturing
     * @param progress Current progress in seconds
     * @param total Total time required in seconds
     * @param percentage Completion percentage (0–100)
     */
    default void onCaptureProgress(Koth koth, Player player, int progress, int total, int percentage) {}

    /**
     * Called when a player successfully completes capturing a KOTH.
     * At this moment the configured rewards are executed and the
     * victory message is displayed.
     *
     * @param koth The KOTH that was captured
     * @param player The player who captured the KOTH
     * @param rewardsCount Total number of rewards given
     */
    default void onCaptureComplete(Koth koth, Player player, int rewardsCount) {}

    /**
     * Called when a player enters the area of an active KOTH.
     *
     * @param koth The KOTH whose area the player entered
     * @param player The player who entered the area
     */
    default void onPlayerEnter(Koth koth, Player player) {}

    /**
     * Called when a player leaves the area of an active KOTH.
     *
     * @param koth The KOTH whose area the player left
     * @param player The player who left the area
     */
    default void onPlayerLeave(Koth koth, Player player) {}

    /**
     * Called when a player dies inside the area of an active KOTH.
     * This includes deaths caused by other players, the environment,
     * or natural causes.
     *
     * @param koth The KOTH where the death occurred
     * @param player The player who died
     * @param killer The player who killed the victim, or null if not killed by another player
     */
    default void onPlayerDeath(Koth koth, Player player, Player killer) {}

    /**
     * Called when a KOTH alert is sent.
     * This happens in two situations:
     *   Capture alerts: when someone is about to capture
     *   Auto-start alerts: minutes before an automatic start
     *
     * @param koth The KOTH related to the alert
     * @param minutesBefore Minutes before the event (0 for capture alerts)
     * @param scheduledTime Scheduled event time (HH:MM)
     */
    default void onKothAlert(Koth koth, int minutesBefore, String scheduledTime) {}

    /**
     * Called when a new KOTH is created.
     *
     * @param koth The KOTH that was created
     * @param creator The player who created the KOTH
     */
    default void onKothCreated(Koth koth, Player creator) {}

    /**
     * Called when a KOTH is deleted.
     *
     * @param koth The KOTH that was deleted
     * @param deleter The player who deleted the KOTH
     */
    default void onKothDeleted(Koth koth, Player deleter) {}

    // ========== NEW EVENTS (v1.2.0) ==========

    /**
     * Called when a player's kill is recorded inside a KOTH zone.
     * This event provides detailed information about the kill,
     * useful for statistics tracking and custom rewards.
     *
     * @param koth The KOTH where the kill occurred
     * @param killer The player who made the kill
     * @param victim The player who was killed
     * @param assistCount Number of players who assisted in the kill
     * @param assists List of players who assisted (may be empty)
     * @since 1.2.0
     */
    default void onKothKill(Koth koth, Player killer, Player victim, int assistCount, java.util.List<Player> assists) {}

    /**
     * Called when a player's statistics are updated.
     * This event fires whenever any statistic changes for a player
     * (captures, kills, deaths, etc.).
     *
     * @param player The player whose stats changed
     * @param oldStats The player's statistics before the change
     * @param newStats The player's statistics after the change
     * @since 1.2.0
     */
    default void onPlayerStatsUpdated(Player player, PlayerStats oldStats, PlayerStats newStats) {}

    /**
     * Called when a KOTH's statistics are updated.
     * This event fires whenever any statistic changes for a KOTH
     * (total captures, kills in zone, etc.).
     *
     * @param koth The KOTH whose stats changed
     * @param oldStats The KOTH's statistics before the change
     * @param newStats The KOTH's statistics after the change
     * @since 1.2.0
     */
    default void onKothStatsUpdated(Koth koth, KothStats oldStats, KothStats newStats) {}
}