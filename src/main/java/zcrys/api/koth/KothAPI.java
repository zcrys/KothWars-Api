package zcrys.api.koth;

import org.bukkit.plugin.Plugin;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface KothAPI {
    Collection<Koth> getKoths();
    Optional<Koth> getKoth(String name);
    Optional<Koth> getKoth(UUID id);
    boolean hasKoth(String name);
    void registerListener(KothListener listener);
    void unregisterListener(KothListener listener);
    Plugin getPlugin();
}