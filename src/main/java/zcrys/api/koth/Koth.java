package zcrys.api.koth;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Koth {
    String getName();
    UUID getUniqueId();
    World getWorld();
    Location getCenter();
    boolean isActive();
    Optional<UUID> getCurrentCapturer();
    Optional<String> getCurrentCapturerName();
    int getCaptureProgress();
    int getCaptureTime();
    int getRemainingTime();
    int getCapturePercentage();
    int getAutoSuspendTime();
    String getBossbarType();
    String getOutlineColor();
    boolean hasParticleDisplay();
    String getTimezone();
    boolean isAutoStartEnabled();
    boolean isAlertEnabled();
    int getAlertTime();
    List<Integer> getAlertMinutes();
    boolean isAlertMinutesEnabled();
    List<String> getScheduledTimes(String day);
    boolean isDayEnabled(String day);
    Collection<Integer> getActiveRewardSlots();
    int getRewardCount(int slot);
    boolean contains(Location location);
    Optional<String> getLastKothTime();
    long getTimeUntilNextStart();
    long getTimeSinceLastStart();
}