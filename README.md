 KothWars API – Developer Documentation
Version Java 21 Paper 1.21.3 License Contributions Welcome

A powerful, lightweight API for integrating KOTH systems into your Paper plugins.

Overview • Installation • Getting Started • Events • FAQ • Contributing
📌 Overview
KothWars API provides a clean and developer-friendly way to interact with KOTHs (King of the Hill events) inside your Paper server.

It's designed to be:

Feature	Description
⚡ Lightning Fast	Optimized for high-player environments with minimal overhead
🔌 Plug & Play	Simple integration – add dependency, implement listener, you're done
📡 Event-Driven	React to everything with 13+ built-in events
🎯 Minimalist	Default methods mean you only implement what you need
🧠 Intuitive	Familiar Bukkit/Paper patterns – zero learning curve
📦 Installation
📋 Click to expand installation instructions

1️⃣ Add JitPack Repository
Maven
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
Gradle
repositories {
    maven { url 'https://jitpack.io' }
}
2️⃣ Add Dependency
Maven
<dependency>
    <groupId>com.github.zcrys</groupId>
    <artifactId>KothWars-Api</artifactId>
    <version>1.0.1</version>
    <scope>provided</scope>
</dependency>
Gradle
dependencies {
    compileOnly 'com.github.zcrys:KothWars-Api:1.0.1'
}
3️⃣ Configure plugin.yml
name: YourPlugin
version: 1.0.0
main: com.yourplugin.YourPlugin
api-version: '1.21'

# Required if your plugin depends on KothWars
depend: [KothWars]

# Use softdepend instead if integration is optional
# softdepend: [KothWars]
🚀 Getting Started
🔧 Click to see basic setup examples

Recommended: Use the ServicesManager
import zcrys.api.koth.KothAPI;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {

    private KothAPI kothAPI;

    @Override
    public void onEnable() {
        setupAPI();
    }

    private void setupAPI() {
        RegisteredServiceProvider<KothAPI> provider =
                getServer().getServicesManager().getRegistration(KothAPI.class);

        if (provider != null) {
            kothAPI = provider.getProvider();
            getLogger().info("✅ Successfully hooked into KothWars API.");
            getLogger().info("   Found " + kothAPI.getKoths().size() + " KOTH(s)");
        } else {
            getLogger().warning("⚠ KothWars API not found. Is the plugin installed?");
        }
    }
}
Alternative: Direct Access via Reflection
private KothAPI getAPIByReflection() {
    try {
        Plugin kothWars = Bukkit.getPluginManager().getPlugin("KothWars");
        if (kothWars == null) return null;
        
        Method method = kothWars.getClass().getMethod("getAPI");
        return (KothAPI) method.invoke(kothWars);
    } catch (Exception e) {
        return null;
    }
}
📡 Events
🎯 Click to expand event list

All methods in KothListener are default methods – override only what you need.

Event	Parameters	Description
onKothStart	Koth koth, Player startedBy	Fired when a KOTH is manually started
onKothStartAuto	Koth koth	Fired when a KOTH auto-starts via schedule
onKothStop	Koth koth, String reason, Player stoppedBy	KOTH stopped (reason: "manual" or "auto")
onKothSuspend	Koth koth	KOTH suspended due to inactivity
onCaptureStart	Koth koth, Player player	Player begins capturing
onCaptureProgress	Koth koth, Player player, int progress, int total, int percentage	Progress update (every 5 seconds)
onCaptureComplete	Koth koth, Player player, int rewardsCount	Player successfully captures KOTH
onPlayerEnter	Koth koth, Player player	Player enters KOTH zone
onPlayerLeave	Koth koth, Player player	Player leaves KOTH zone
onPlayerDeath	Koth koth, Player player, Player killer	Player dies inside KOTH area
onKothAlert	Koth koth, int minutesBefore, String scheduledTime	Scheduled alert triggered
onKothCreated	Koth koth, Player creator	New KOTH created
onKothDeleted	Koth koth, Player deleter	KOTH deleted
Example Implementation
public class MyListener implements KothListener {
    
    @Override
    public void onCaptureComplete(Koth koth, Player player, int rewards) {
        // Your custom reward logic
        player.sendMessage("§a§l✦ Congratulations! ✦");
        player.sendMessage("§fYou captured §e" + koth.getName() + "§f!");
        
        // Execute console commands
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
            "give " + player.getName() + " diamond 5");
    }
    
    @Override
    public void onPlayerEnter(Koth koth, Player player) {
        // Optional: track player entries
        getLogger().info(player.getName() + " entered " + koth.getName());
    }
}
🎮 Real-World Examples
💰 Economy Integration

public class EconomyPlugin extends JavaPlugin implements KothListener {
    
    private KothAPI api;
    private VaultEconomy economy;
    
    @Override
    public void onEnable() {
        // Setup economy
        if (!setupEconomy()) {
            getLogger().severe("Vault not found!");
            return;
        }
        
        // Hook into API
        var provider = getServer().getServicesManager()
                .getRegistration(KothAPI.class);
        
        if (provider != null) {
            api = provider.getProvider();
            api.registerListener(this);
            getLogger().info("✅ Economy plugin ready!");
        }
    }
    
    @Override
    public void onCaptureComplete(Koth koth, Player player, int rewards) {
        // Give money based on KOTH difficulty
        int reward = switch(koth.getName().toLowerCase()) {
            case "easy" -> 100;
            case "medium" -> 250;
            case "hard" -> 500;
            default -> 150;
        };
        
        economy.depositPlayer(player, reward);
        player.sendMessage("§a+ $" + reward + " for capturing " + koth.getName());
    }
}
📊 Statistics & Leaderboards

public class StatsPlugin implements KothListener {
    
    private Map<UUID, Integer> captureCount = new HashMap<>();
    private Map<UUID, Integer> timeInZone = new HashMap<>();
    
    @Override
    public void onCaptureComplete(Koth koth, Player player, int rewards) {
        // Increment capture counter
        captureCount.merge(player.getUniqueId(), 1, Integer::sum);
        
        // Calculate time spent capturing
        int timeSpent = koth.getCaptureTime() - koth.getRemainingTime();
        timeInZone.merge(player.getUniqueId(), timeSpent, Integer::sum);
        
        // Save to database
        saveStats(player);
    }
    
    public String getStats(Player player) {
        return String.format(
            "§e%s's Stats:\n" +
            "§7Captures: §f%d\n" +
            "§7Time in zone: §f%ds",
            player.getName(),
            captureCount.getOrDefault(player.getUniqueId(), 0),
            timeInZone.getOrDefault(player.getUniqueId(), 0)
        );
    }
}
🤖 Discord Integration

public class DiscordPlugin implements KothListener {
    
    private WebhookClient discord;
    
    @Override
    public void onKothStart(Koth koth, Player startedBy) {
        discord.sendMessage(new WebhookMessage()
            .setContent("🎯 **KOTH Started!**")
            .addEmbed(new EmbedBuilder()
                .setTitle(koth.getName())
                .setDescription("Started by: " + startedBy.getName())
                .setColor(0x00FF00)
                .build()
            )
        );
    }
    
    @Override
    public void onCaptureComplete(Koth koth, Player player, int rewards) {
        discord.sendMessage(new WebhookMessage()
            .setContent("🏆 **KOTH Captured!**")
            .addEmbed(new EmbedBuilder()
                .setTitle(player.getName())
                .setDescription("Captured: " + koth.getName())
                .setColor(0xFFD700)
                .build()
            )
        );
    }
}
❓ FAQ
⬇️ Click on any question to reveal the answer ⬇️

🔹 Why can't my plugin find the API service?

This is the most common issue! Here's what to check:

✅ KothWars is installed and enabled
Run /plugins – you should see KothWars in the list.

✅ Correct version
You need KothWars v1.10.1 or higher that supports ServicesManager.

✅ plugin.yml configuration
Make sure you have either:

depend: [KothWars]     # If KothWars is required
# or
softdepend: [KothWars] # If integration is optional
✅ Plugin loading order
KothWars must load BEFORE your plugin. Using depend: guarantees this.

If you've checked all these and it still doesn't work, KothWars might not have implemented ServicesManager yet. In that case, use the reflection fallback method shown above.

🔹 Do I need to shade the API in my plugin?

No! Always use scope: provided (Maven) or compileOnly (Gradle).

<scope>provided</scope>  <!-- ✅ Correct -->
This means:

The API is only needed at compile time
At runtime, your plugin uses the actual API from KothWars
No conflicts, no duplication, smaller plugin file
If you shade the API, you risk:

❌ Class conflicts
❌ Larger plugin file
❌ Version mismatches
❌ KothWars updates breaking your plugin
🔹 What Paper/Spigot versions are supported?

Version	Support
Paper 1.21.4	✅ Full
Paper 1.21.3	✅ Full
Paper 1.21.2	✅ Full
Paper 1.21.1	✅ Full
Paper 1.21	✅ Full
Paper 1.20.x	⚠️ Untested
Spigot 1.21+	✅ Should work
The API is built against Paper 1.21.3 API, but it should work on any modern Paper/Spigot version due to minimal version-specific features.

Note: Some features might not work on older versions if they rely on newer Minecraft mechanics.

🔹 How do I get the player who started/stopped a KOTH?

The API provides this directly in events:

@Override
public void onKothStart(Koth koth, Player startedBy) {
    // 'startedBy' is the player who executed the command
    getLogger().info(startedBy.getName() + " started " + koth.getName());
}

@Override
public void onKothStop(Koth koth, String reason, Player stoppedBy) {
    if (stoppedBy != null) {
        // Manual stop by player
        getLogger().info(stoppedBy.getName() + " stopped " + koth.getName());
    } else {
        // Auto-stop (schedule ended, etc.)
        getLogger().info(koth.getName() + " stopped automatically: " + reason);
    }
}
For auto-started KOTHs, use onKothStartAuto – there's no player in that case.

🔹 Can I get a list of all players currently in a KOTH zone?

The API doesn't provide this directly (for performance reasons), but you can easily implement it:

public List<Player> getPlayersInKoth(Koth koth) {
    return Bukkit.getOnlinePlayers().stream()
        .filter(player -> koth.contains(player.getLocation()))
        .collect(Collectors.toList());
}

// Optional: Cache for better performance
private Map<String, List<Player>> zoneCache = new HashMap<>();
private long lastCacheUpdate = 0;

public List<Player> getPlayersInKothCached(Koth koth) {
    long now = System.currentTimeMillis();
    if (now - lastCacheUpdate > 1000) { // Update every second
        zoneCache.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Koth k : api.getKoths()) {
                if (k.isActive() && k.contains(player.getLocation())) {
                    zoneCache.computeIfAbsent(k.getName(), 
                        k -> new ArrayList<>()).add(player);
                }
            }
        }
        lastCacheUpdate = now;
    }
    return zoneCache.getOrDefault(koth.getName(), Collections.emptyList());
}
🔹 What's the difference between capture progress and remaining time?

Good question! Here's the breakdown:

int getCaptureProgress();  // Seconds elapsed since capture started
int getCaptureTime();      // Total seconds needed to capture
int getRemainingTime();    // Seconds left = captureTime - progress
int getCapturePercentage(); // Percentage complete = (progress / captureTime) * 100
Example:

If captureTime = 60 (1 minute)
And progress = 45
Then:
remainingTime = 15 (15 seconds left)
percentage = 75 (75% complete)
Use cases:

progress: Show capture bars, track time elapsed
remainingTime: Countdown timers, urgency warnings
percentage: Progress indicators, boss bars
🔹 How do I listen to events only for specific KOTHs?

Easy – just check the KOTH name or properties in your listener:

@Override
public void onCaptureStart(Koth koth, Player player) {
    switch(koth.getName().toLowerCase()) {
        case "spawn":
            // Special handling for spawn KOTH
            player.sendMessage("§cYou can't capture spawn KOTH!");
            break;
            
        case "nether":
            // Nether KOTH has different rules
            if (!player.getWorld().getName().equals("world_nether")) {
                return;
            }
            break;
            
        default:
            // Regular KOTH logic
            handleRegularKOTH(koth, player);
    }
}

// Or filter by properties
@Override
public void onCaptureProgress(Koth koth, Player player, int progress, int total, int percentage) {
    if (koth.getOutlineColor().equals("RED")) {
        // Red-outline KOTHs are "high priority"
        announceHighPriority(koth, player, percentage);
    }
}
🔹 My plugin can't find KothAPI class – what's wrong?

This is a compile-time error, not a runtime one. Check:

1️⃣ Maven/Gradle configuration
Maven:

<!-- Make sure JitPack is in repositories -->
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<!-- And dependency is correct -->
<dependency>
    <groupId>com.github.zcrys</groupId>
    <artifactId>KothWars-Api</artifactId>
    <version>1.0.1</version>
    <scope>provided</scope>
</dependency>
Gradle:

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.zcrys:KothWars-Api:1.0.1'
}
2️⃣ IDE refresh
IntelliJ: Click the Maven refresh button (🔄)
Eclipse: Project → Clean → Clean all projects
VS Code: Run maven clean then maven install
3️⃣ Internet connection
JitPack needs to download the API. Make sure you're online when building.

🔹 Are there any performance considerations?

Yes! Here's what you should know:

✅ Good practices
// ✅ Cache API instance (don't call ServicesManager repeatedly)
private KothAPI api; // Store once in onEnable()

// ✅ Use bulk operations when possible
Collection<Koth> allKoths = api.getKoths(); // Get all at once

// ✅ Check active status before expensive operations
if (koth.isActive()) {
    // Only process active KOTHs
}
❌ Bad practices
// ❌ DON'T call ServicesManager every tick
public void onTick() {
    var provider = getServer().getServicesManager()
        .getRegistration(KothAPI.class); // ⚠️ Expensive!
}

// ❌ DON'T loop through all KOTHs unnecessarily
for (Koth k : api.getKoths()) {
    if (!k.isActive()) continue; // Wasteful if many inactive KOTHs
}
📊 Performance tips
Events fire every 5 seconds for progress updates – don't do heavy work here
Player enter/leave events fire immediately – keep logic lightweight
Use async tasks for database/network calls
Cache results when possible (player lists, etc.)
🔹 Can I modify KOTH data through the API?

No – and that's by design! The API is read-only for a reason:

🔒 Why read-only?
Stability: Prevents plugins from corrupting KOTH configurations
Consistency: Ensures all changes go through KothWars' validation
Thread safety: Avoids concurrent modification issues
Performance: No locking overhead
🔧 How to modify KOTHs
If you need to modify KOTHs, you should:

Use KothWars' commands (if available)

Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
    "koth start " + kothName);
Request features
Ask the KothWars developer to add modification endpoints to the API

Use reflection (NOT recommended)

// ⚠️ This is fragile and might break with updates!
// Don't do this unless you really know what you're doing
📝 What you CAN do:
✅ Read all KOTH data
✅ Listen to events
✅ React to changes
✅ Build external features around KOTHs
❌ What you CAN'T do:
❌ Change capture time
❌ Modify rewards
❌ Alter schedules
❌ Start/stop KOTHs programmatically
🔹 How do I handle multiple servers (bungee/velocity)?

The API works on a per-server basis. For multi-server networks:

📡 Option 1: Plugin Messaging Channel
// Send KOTH events to proxy
@Override
public void onCaptureComplete(Koth koth, Player player, int rewards) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Capture");
    out.writeUTF(koth.getName());
    out.writeUTF(player.getName());
    out.writeInt(rewards);
    
    player.sendPluginMessage(this, "kothwars:event", out.toByteArray());
}
🗄️ Option 2: Shared Database
@Override
public void onCaptureComplete(Koth koth, Player player, int rewards) {
    // Store in database that all servers can access
    database.saveCapture(
        koth.getName(),
        player.getUniqueId(),
        player.getName(),
        System.currentTimeMillis()
    );
}
🔄 Option 3: Redis Pub/Sub
@Override
public void onKothStart(Koth koth, Player startedBy) {
    // Publish to Redis
    redis.publish("koth-events", 
        String.format("START:%s:%s", 
            koth.getName(), 
            startedBy.getName()
        )
    );
}
💡 Best Practice
Use Option 1 for real-time events, Option 2 for persistent stats, and Option 3 for high-scale networks.

Note: The API itself doesn't provide cross-server features – you need to implement this yourself.

🔹 What's coming in future versions?

🚧 Planned features
Version	Feature	Status
1.1.0	Write operations API	🔜 Planned
1.1.0	More detailed player stats	🔜 Planned
1.2.0	Async event support	📝 Proposed
1.2.0	Custom event creation	📝 Proposed
2.0.0	Cross-server events	🤔 Under discussion
💡 Request a feature
Have an idea? Open an issue on GitHub with:

Clear description of what you need
Use case / example
Why the current API doesn't work for you
🤝 Contribute
Want to help? We welcome PRs! Check the Contributing section.

🛠 Troubleshooting
❓ API not found at runtime

Symptom: getServicesManager().getRegistration(KothAPI.class) returns null

Checklist:

 Is KothWars installed? (/plugins)
 Is KothWars enabled? (no errors in console)
 Is your plugin loading after KothWars? (depend: [KothWars])
 Are you using the correct API version? (1.0.1+)
 Did you try the reflection fallback method?
Quick test:

// Add this to verify KothWars is present
if (Bukkit.getPluginManager().getPlugin("KothWars") == null) {
    getLogger().severe("KothWars not installed!");
}
❓ ClassNotFoundException during compilation

Symptom: IDE can't find zcrys.api.koth.* classes

Solutions:

Refresh Maven/Gradle

mvn clean install
# or
gradle clean build
Check internet connection
JitPack needs to download the dependency

Verify repository configuration
Make sure JitPack is in the correct section (not in pluginRepositories)

Try direct dependency (temporary fix)

<dependency>
    <groupId>com.github.zcrys</groupId>
    <artifactId>KothWars-Api</artifactId>
    <version>1.0.1</version>
    <scope>compile</scope> <!-- Temporarily change scope -->
</dependency>
(Remember to change back to provided before final build!)

