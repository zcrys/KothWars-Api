package zcrys.api.koth;

import org.bukkit.entity.Player;

public interface KothListener {
    default void onKothStart(Koth koth, Player startedBy) {}
    default void onKothStartAuto(Koth koth) {}
    default void onKothStop(Koth koth, String reason, Player stoppedBy) {}
    default void onKothSuspend(Koth koth) {}
    default void onCaptureStart(Koth koth, Player player) {}
    default void onCaptureProgress(Koth koth, Player player, int progress, int total, int percentage) {}
    default void onCaptureComplete(Koth koth, Player player, int rewardsCount) {}
    default void onPlayerEnter(Koth koth, Player player) {}
    default void onPlayerLeave(Koth koth, Player player) {}
    default void onPlayerDeath(Koth koth, Player player, Player killer) {}
    default void onKothAlert(Koth koth, int minutesBefore, String scheduledTime) {}
    default void onKothCreated(Koth koth, Player creator) {}
    default void onKothDeleted(Koth koth, Player deleter) {}
}