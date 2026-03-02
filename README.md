
# KothWars API

**Official API for integrating King of the Hill events into Paper plugins.**

[![Java](https://img.shields.io/badge/Java-21-orange)]()
[![Paper](https://img.shields.io/badge/Paper-1.21.x-blue)]()
[![License](https://img.shields.io/badge/License-MIT-green)]()
[![JitPack](https://img.shields.io/badge/JitPack-1.2.0-purple)](https://jitpack.io/#zcrys/KothWars-Api)

---

## Table of Contents

- [Overview](#overview)
- [New in v1.2.0](#new-in-v120)
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Events](#events)
- [Koth Interface](#koth-interface)
- [Statistics System](#statistics-system)
- [Clan Integration](#clan-integration)
- [Examples](#examples)
- [Performance](#performance)
- [FAQ](#faq)
- [Compatibility](#compatibility)

---

## Overview

KothWars API provides a clean, event-driven interface to interact with King of the Hill game mechanics on your Paper server. It follows familiar Bukkit/Paper patterns, uses default listener methods so you only override what you need, and maintains an internal cache for low-overhead reads.

**Version 1.2.0** introduces comprehensive statistics tracking and clan integration support.

---

## New in v1.2.0

### Statistics System
- **Player Statistics**: Track captures, kills, deaths, assists, time capturing, and KOTHs participated
- **KOTH Statistics**: Track total captures, kills in zone, deaths in zone, activations, and active time
- **Leaderboards**: Get top players by captures or kills
- **Persistent Storage**: All statistics are saved to database and survive restarts

### Clan Integration
- **Clan Rewards**: Distribute rewards to all online clan members when a player captures
- **Clan Tags**: Display clan tags in capture messages and placeholders
- **Configurable Format**: Customize how clan tags are displayed
- **Multi-Clan Support**: Works with any clan plugin that provides PlaceholderAPI placeholders

### New Events
- `onKothKill` - Detailed kill information with assists
- `onPlayerStatsUpdated` - Track when player statistics change
- `onKothStatsUpdated` - Track when KOTH statistics change

### New Placeholders
- `%kothwars_stats_captures%` - Player's total captures
- `%kothwars_stats_kills%` - Player's total kills
- `%kothwars_stats_deaths%` - Player's total deaths
- `%kothwars_stats_assists%` - Player's total assists
- `%kothwars_stats_kdr%` - Player's K/D ratio
- `%kothwars_stats_time%` - Player's total time capturing
- `%kothwars_stats_participated%` - KOTHs participated
- `%kothwars_clan%` - Player's clan tag (raw)
- `%kothwars_clan_format%` - Player's name with clan tag

---

## Installation

### Step 1 — Add the JitPack repository

**Maven** — add to `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

**Gradle** — add to `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2 — Add the dependency

**Maven:**

```xml
<dependency>
    <groupId>com.github.zcrys</groupId>
    <artifactId>KothWars-Api</artifactId>
    <version>1.2.0</version>
    <scope>provided</scope>
</dependency>
```

**Gradle:**

```gradle
dependencies {
    compileOnly 'com.github.zcrys:KothWars-Api:1.2.0'
}
```

> **Important:** Always use `provided` / `compileOnly`. Do not shade this API into your jar — it will cause class conflicts and version mismatches.

### Step 3 — Declare the dependency in plugin.yml

```yaml
depend: [KothWars]
```

---

## Getting Started

### Recommended: ServicesManager hook

```java
public class YourPlugin extends JavaPlugin {

    private KothAPI api;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<KothAPI> provider =
                getServer().getServicesManager().getRegistration(KothAPI.class);

        if (provider != null) {
            api = provider.getProvider();
            getLogger().info("Successfully hooked into KothWars API v1.2.0");
        } else {
            getLogger().warning("KothWars not found. Disabling integration.");
        }
    }
}
```

### Alternative: Reflection (optional fallback)

```java
private KothAPI getAPIByReflection() {
    try {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("KothWars");
        Method method = plugin.getClass().getMethod("getAPI");
        return (KothAPI) method.invoke(plugin);
    } catch (Exception ignored) {
        return null;
    }
}
```

---

## Events

Implement `KothListener` and override only the events you need. All methods have empty default implementations.

| Method | Trigger |
|---|---|
| `onKothStart(koth, player)` | KOTH started manually by a player |
| `onKothStartAuto(koth)` | KOTH started automatically via scheduler |
| `onKothStop(koth, reason, player)` | KOTH stopped (manual, auto-suspend, or error) |
| `onKothSuspend(koth)` | KOTH suspended due to inactivity |
| `onCaptureStart(koth, player)` | Player begins capturing the zone |
| `onCaptureProgress(koth, player, progress, total, percentage)` | Fired every second during capture |
| `onCaptureComplete(koth, player, rewardsCount)` | Player successfully captures the KOTH |
| `onPlayerEnter(koth, player)` | Player enters the active zone |
| `onPlayerLeave(koth, player)` | Player leaves the active zone |
| `onPlayerDeath(koth, player, killer)` | Player dies inside the zone |
| `onKothAlert(koth, minutesBefore, scheduledTime)` | Alert sent before capture or auto-start |
| `onKothCreated(koth, creator)` | A new KOTH is created |
| `onKothDeleted(koth, deleter)` | A KOTH is deleted |
| `onKothKill(koth, killer, victim, assistCount, assists)` | Detailed kill information inside zone |
| `onPlayerStatsUpdated(player, oldStats, newStats)` | Player statistics changed |
| `onKothStatsUpdated(koth, oldStats, newStats)` | KOTH statistics changed |

### Registering your listener

```java
api.registerListener(new MyKothListener());
```

### Example listener with new features

```java
public class MyKothListener implements KothListener {

    @Override
    public void onCaptureStart(Koth koth, Player player) {
        Bukkit.broadcastMessage(player.getName() + " is capturing " + koth.getName() + "!");
    }

    @Override
    public void onCaptureComplete(Koth koth, Player player, int rewardsCount) {
        // Get clan information
        String clanTag = api.getPlayerClanTag(player);
        String displayName = clanTag.isEmpty() ? 
            player.getName() : 
            "[" + clanTag + "] " + player.getName();
            
        Bukkit.broadcastMessage(displayName + " captured " + koth.getName() + "!");
        
        // Get statistics
        PlayerStats stats = api.getPlayerStats(player);
        player.sendMessage("You now have " + stats.getTotalCaptures() + " total captures!");
    }

    @Override
    public void onKothKill(Koth koth, Player killer, Player victim, int assistCount, List<Player> assists) {
        killer.sendMessage("You killed " + victim.getName() + " inside " + koth.getName() + "!");
        if (assistCount > 0) {
            killer.sendMessage(assistCount + " player(s) assisted you!");
        }
    }

    @Override
    public void onPlayerStatsUpdated(Player player, PlayerStats oldStats, PlayerStats newStats) {
        if (newStats.getTotalCaptures() > oldStats.getTotalCaptures()) {
            player.sendMessage("Congratulations on your " + newStats.getTotalCaptures() + "th capture!");
        }
    }
}
```

---

## Koth Interface

The `Koth` object exposes the full state of a KOTH instance, now including statistics methods.

### Statistics Methods (new in v1.2.0)

```java
// Get KOTH statistics
int totalCaptures = koth.getTotalCaptures();
int totalKills = koth.getTotalKills();
int totalDeaths = koth.getTotalDeaths();
int totalActivations = koth.getTotalActivations();
long totalActiveTime = koth.getTotalActiveTime();
```

### Clan Methods (new in v1.2.0)

```java
// Check if clan rewards are enabled for this KOTH
boolean clanEnabled = koth.isClanRewardsEnabled();

// Get current capturer's clan tag
Optional<String> clanTag = koth.getCurrentCapturerClan();

// Get formatted display name with clan
Optional<String> displayName = koth.getCurrentCapturerDisplayName();

// Get all players who will receive rewards
Collection<Player> recipients = koth.getRewardRecipients();
```

### Querying KOTHs

```java
// Get all KOTHs
Collection<Koth> koths = api.getKoths();

// Get by name (case-insensitive)
Optional<Koth> koth = api.getKoth("hillzone");

// Get by UUID
Optional<Koth> koth = api.getKoth(uuid);

// Check existence
boolean exists = api.hasKoth("hillzone");
```

---

## Statistics System

### Player Statistics

Access player statistics through the API:

```java
PlayerStats stats = api.getPlayerStats(player);

int captures = stats.getTotalCaptures();
int kills = stats.getTotalKills();
int deaths = stats.getTotalDeaths();
int assists = stats.getTotalAssists();
long time = stats.getTotalTimeCapturing();
int participated = stats.getTotalKothsParticipated();
double kdr = stats.getKDR();
String formattedTime = stats.getFormattedTime();
```

### KOTH Statistics

Access KOTH statistics through the Koth object:

```java
Koth koth = api.getKoth("hillzone").orElse(null);
if (koth != null) {
    int captures = koth.getTotalCaptures();
    int kills = koth.getTotalKills();
    int deaths = koth.getTotalDeaths();
    int activations = koth.getTotalActivations();
    long activeTime = koth.getTotalActiveTime();
}
```

### Leaderboards

Get top players by captures or kills:

```java
// Top 10 players by captures
List<PlayerStats> topCapturers = api.getTopPlayersByCaptures(10);

// Top 5 players by kills
List<PlayerStats> topKillers = api.getTopPlayersByKills(5);

for (PlayerStats stats : topCapturers) {
    System.out.println(stats.getPlayerName() + ": " + stats.getTotalCaptures() + " captures");
}
```

---

## Clan Integration

### Configuration

In `config.yml`:

```yaml
clan:
  enabled: true
  placeholder: "%uclans_tag_nocolor%"
  capturer-format: "[%clan%] %player%"
```

### API Methods

```java
// Check if clan integration is enabled
boolean enabled = api.isClanIntegrationEnabled();

// Get player's clan tag (empty string if none)
String tag = api.getPlayerClanTag(player);

// Get online clan members (includes the player)
List<Player> members = api.getClanMembers(player);

// Format player name with clan tag
String displayName = api.formatPlayerNameWithClan(player);
```

### Example: Clan-based Rewards

```java
@Override
public void onCaptureComplete(Koth koth, Player player, int rewardsCount) {
    if (api.isClanIntegrationEnabled()) {
        List<Player> clanMembers = api.getClanMembers(player);
        
        for (Player member : clanMembers) {
            if (!member.equals(player)) {
                member.sendMessage("Your clanmate " + player.getName() + 
                    " captured " + koth.getName() + "! You receive rewards!");
            }
        }
    }
}
```

---

## Examples

### Example 1: Custom Capture Message with Clan

```java
@Override
public void onCaptureComplete(Koth koth, Player player, int rewardsCount) {
    String displayName = api.formatPlayerNameWithClan(player);
    Bukkit.broadcastMessage("§6" + displayName + " §acaptured §e" + koth.getName() + "§a!");
}
```

### Example 2: Track Player Progress

```java
@Override
public void onCaptureComplete(Koth koth, Player player, int rewardsCount) {
    PlayerStats stats = api.getPlayerStats(player);
    
    if (stats.getTotalCaptures() == 10) {
        player.sendMessage("§6Congratulations! You've reached 10 total captures!");
        // Give special reward
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
            "give " + player.getName() + " diamond 10");
    }
}
```

### Example 3: KOTH Statistics Dashboard

```java
public void showKothStats(Player player, String kothName) {
    Optional<Koth> optional = api.getKoth(kothName);
    if (!optional.isPresent()) {
        player.sendMessage("KOTH not found!");
        return;
    }
    
    Koth koth = optional.get();
    player.sendMessage("§6=== " + koth.getName() + " Statistics ===");
    player.sendMessage("§eTotal Captures: §f" + koth.getTotalCaptures());
    player.sendMessage("§eTotal Kills: §f" + koth.getTotalKills());
    player.sendMessage("§eTotal Deaths: §f" + koth.getTotalDeaths());
    player.sendMessage("§eTotal Activations: §f" + koth.getTotalActivations());
    player.sendMessage("§eActive Time: §f" + formatTime(koth.getTotalActiveTime()));
}
```

### Example 4: Custom Leaderboard Command

```java
public void showLeaderboard(Player player) {
    List<PlayerStats> top = api.getTopPlayersByCaptures(10);
    
    player.sendMessage("§6=== Top Capturers ===");
    int rank = 1;
    for (PlayerStats stats : top) {
        player.sendMessage("§e#" + rank + " §f" + stats.getPlayerName() + 
            " §7- §a" + stats.getTotalCaptures() + " captures");
        rank++;
    }
}
```

### Example 5: Real-time Kill Tracking

```java
@Override
public void onKothKill(Koth koth, Player killer, Player victim, 
                       int assistCount, List<Player> assists) {
    
    // Update some custom tracking
    PlayerStats killerStats = api.getPlayerStats(killer);
    
    if (killerStats.getTotalKills() % 5 == 0) {
        killer.sendMessage("§6You've reached " + killerStats.getTotalKills() + 
            " kills inside KOTHs!");
    }
    
    // Show assists
    if (!assists.isEmpty()) {
        StringBuilder assistNames = new StringBuilder();
        for (Player assist : assists) {
            if (assistNames.length() > 0) assistNames.append(", ");
            assistNames.append(assist.getName());
        }
        killer.sendMessage("§7Assisted by: §f" + assistNames.toString());
    }
}
```

---

## Performance

**Do:**
- Cache the `KothAPI` instance on plugin enable — never fetch it per-tick
- Use async tasks (e.g. `runTaskAsynchronously`) for any database or network calls inside listeners
- Call `koth.isActive()` before processing events if your logic only applies to active KOTHs
- Avoid iterating `getKoths()` in high-frequency events like `onCaptureProgress`

**Avoid:**
- Calling `ServicesManager.getRegistration()` on every tick or event
- Heavy synchronous logic inside `onCaptureProgress` (fires every second per capturer)
- Unnecessary loops over all KOTHs when you already have a reference

The API maintains an internal cache that refreshes automatically every 5 seconds. If you need to force an immediate refresh (e.g. after a rename), call `api.refreshCache()`.

---

## FAQ

**Do I need to shade the API into my plugin?**

No. Use `provided` scope in Maven or `compileOnly` in Gradle. The API classes are loaded by KothWars at runtime. Shading them will cause `ClassCastException` errors and inflate your jar unnecessarily.

**Can I modify KOTH data through the API?**

The API is intentionally read-only. This ensures thread safety and prevents accidental config corruption. If you need to change KOTH settings, use in-game commands or request a write extension.

**What if KothWars is not installed?**

Always null-check the provider registration before using the API, and consider making KothWars a soft dependency if your plugin can run without it.

**How are statistics stored?**

Statistics are stored in the same database as KOTH configurations (SQLite by default, MySQL optional). They persist across server restarts and updates.

**Which clan plugins are supported?**

Any clan plugin that provides PlaceholderAPI placeholders. Configure the placeholder in `config.yml`. Tested with:
- uClans
- SaberFactions
- FactionsUUID (with PlaceholderAPI expansion)

**Do clan rewards work with all KOTHs?**

Yes, if `clan.enabled` is true in config, all KOTHs will distribute rewards to clan members. This can be overridden per-KOTH in future versions.

---

## Compatibility

| Platform | Status |
|---|---|
| Paper 1.21.4 | Supported |
| Paper 1.21.3 | Supported |
| Paper 1.21.x | Supported |
| Paper 1.20.x | Untested |
| Spigot | Not supported |

---

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
