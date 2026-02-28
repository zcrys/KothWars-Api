# 🏔 KothWars API

<div align="center">

**A powerful and lightweight API for integrating KOTH systems into Paper plugins**

[![Java](https://img.shields.io/badge/Java-21-orange)]()
[![Paper](https://img.shields.io/badge/Paper-1.21.3-blue)]()
[![License](https://img.shields.io/badge/License-MIT-green)]()
[![JitPack](https://img.shields.io/badge/Repository-JitPack-purple)]()

</div>

---

## 📚 Table of Contents

* [📌 Overview](#-overview)
* [📦 Installation](#-installation)
* [🚀 Getting Started](#-getting-started)
* [📡 Events](#-events)
* [💡 Examples](#-examples)
* [❓ FAQ](#-faq)
* [⚡ Performance](#-performance)
* [🔒 Design Philosophy](#-design-philosophy)
* [🗺 Roadmap](#-roadmap)
* [🤝 Contributing](#-contributing)

---

# 📌 Overview

**KothWars API** provides a clean and developer-friendly way to interact with **King of the Hill (KOTH)** events inside your Paper server.

### ✨ Designed to be:

| Feature              | Description                            |
| -------------------- | -------------------------------------- |
| ⚡ **Lightning Fast** | Optimized for high-player environments |
| 🔌 **Plug & Play**   | Just add dependency and hook into it   |
| 📡 **Event-Driven**  | 13+ built-in events                    |
| 🎯 **Minimalist**    | Default listener methods               |
| 🧠 **Intuitive**     | Familiar Bukkit/Paper patterns         |

---

# 📦 Installation

## 1️⃣ Add JitPack Repository

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

### Gradle

```gradle
repositories {
  maven { url 'https://jitpack.io' }
}
```

---

## 2️⃣ Add Dependency

### Maven

```xml
<dependency>
  <groupId>com.github.zcrys</groupId>
  <artifactId>KothWars-Api</artifactId>
  <scope>provided</scope>
</dependency>
```

### Gradle

```gradle
dependencies {
}
```

---

## 3️⃣ plugin.yml

```yaml
depend: [KothWars]
# or
softdepend: [KothWars]
```

---

# 🚀 Getting Started

## ✅ Recommended: ServicesManager Hook

```java
public class YourPlugin extends JavaPlugin {

    private KothAPI api;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<KothAPI> provider =
                getServer().getServicesManager().getRegistration(KothAPI.class);

        if (provider != null) {
            api = provider.getProvider();
            getLogger().info("Hooked into KothWars API.");
        } else {
            getLogger().warning("KothWars not found.");
        }
    }
}
```

---

## 🔄 Fallback: Reflection (Optional)

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

# 📡 Events

All methods in `KothListener` are **default methods** — override only what you need.

| Event               | Description                    |
| `onKothStart`       | Manual start                   |
| `onKothStartAuto`   | Auto scheduled start           |
| `onKothStop`        | KOTH stopped                   |
| `onCaptureStart`    | Player starts capturing        |
| `onCaptureComplete` | Player wins                    |
| `onPlayerEnter`     | Player enters zone             |
| `onPlayerLeave`     | Player leaves zone             |
| `onPlayerDeath`     | Death inside zone              |
| `onKothCreated`     | New KOTH created               |
| `onKothDeleted`     | KOTH deleted                   |

---

## Example Listener

```java
public class MyListener implements KothListener {

    @Override
    public void onCaptureComplete(Koth koth, Player player, int rewards) {
        player.sendMessage("§a§l✦ Congratulations! ✦");
        player.sendMessage("§fYou captured §e" + koth.getName());
    }
}
```

---

# 💡 Examples

## 💰 Economy Integration

```java
@Override
public void onCaptureComplete(Koth koth, Player player, int rewards) {
    economy.depositPlayer(player, 250);
}
```

## 📊 Statistics

```java
captureCount.merge(player.getUniqueId(), 1, Integer::sum);
```

## 🤖 Discord Webhook

```java
discord.sendMessage("KOTH Captured: " + koth.getName());
```

---

# ❓ FAQ

### 🔹 Do I need to shade the API?

No. Always use:

```
<scope>provided</scope>
```

Shading may cause:

* Class conflicts
* Larger jar size
* Version mismatches

---

### 🔹 Supported Versions

| Version      | Support     |
| ------------ | ----------- |
| Paper 1.21.4 | ✅           |
| Paper 1.21.3 | ✅           |
| Paper 1.21.x | ✅           |
| Paper 1.20.x | ⚠️ Untested |

---

# ⚡ Performance

## ✅ Best Practices

* Cache the API instance
* Avoid heavy logic inside `onCaptureProgress`
* Use async tasks for DB/network
* Check `koth.isActive()` before processing

## ❌ Avoid

* Calling `ServicesManager` every tick
* Looping all KOTHs unnecessarily

---

# 🔒 Design Philosophy


### Why?

* 🔐 Stability
* 🧵 Thread safety
* 🚫 No config corruption
* ⚡ Zero locking overhead

If you need write access:

* Use commands
* Request API extension
* Avoid reflection

---

# 🗺 Roadmap


---

# 🤝 Contributing

Pull Requests are welcome!

If you want to contribute:

1. Fork the repository
2. Create a feature branch
3. Submit a PR

---

<div align="center">

### ⭐ If this API helped you, consider starring the repository!

</div>
