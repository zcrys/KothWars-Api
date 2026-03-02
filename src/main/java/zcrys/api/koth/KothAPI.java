package zcrys.api.koth;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

/**
 * Main interface of the KothWars API.
 * Provides access to all KOTHs registered in the plugin and allows
 * registering listeners to receive events related to KOTHs.
 * This API is designed for other plugins that want to integrate
 * with KothWars.
 *
 * <h2>Basic usage:</h2>
 * 
 * // Get the API instance
 * KothAPI api = (KothAPI) Bukkit.getServicesManager().load(KothAPI.class);
 *
 * // Get a KOTH by name
 * Optional<Koth> koth = api.getKoth("myKoth");
 *
 * // Register a listener
 * api.registerListener(new MyKothListener());
 * 
 *
 */
public interface KothAPI {

    /**
     * Gets a collection containing all KOTHs registered in the plugin.
     * The returned collection is a read-only view of the currently
     * loaded KOTHs. Any modification to the collection will not
     * affect the actual KOTH objects.
     *
     * @return Immutable collection of all KOTHs
     */
    Collection<Koth> getKoths();

    /**
     * Searches for a KOTH by its name.
     * The name is the main identifier of the KOTH and must be unique.
     * The search is case-insensitive.
     *
     * @param name Name of the KOTH to search
     * @return Optional containing the KOTH if found, or empty if not
     * @throws IllegalArgumentException if the name is null
     */
    Optional<Koth> getKoth(String name);

    /**
     * Searches for a KOTH by its unique UUID.
     * Unlike the name, the UUID is internally generated and never
     * changes, even if the KOTH is renamed. This makes it ideal
     * for persistent references.
     *
     * @param id UUID of the KOTH to search
     * @return Optional containing the KOTH if found, or empty if not
     * @throws IllegalArgumentException if the UUID is null
     */
    Optional<Koth> getKoth(UUID id);

    /**
     * Checks whether a KOTH exists with the specified name.
     * Utility method to quickly verify whether a name
     * is already in use without retrieving the Koth object.
     *
     * @param name Name of the KOTH to check
     * @return true if a KOTH with that name exists, false otherwise
     * @throws IllegalArgumentException if the name is null
     */
    boolean hasKoth(String name);

    /**
     * Registers a listener to receive KOTH events.
     * Registered listeners will receive notifications when events
     * occur such as KOTH start, capture progress, players entering
     * or leaving the area, etc.
     *
     * Example usage:
     * 
     * api.registerListener(new KothListener() {
     *     &#64;Override
     *     public void onCaptureStart(Koth koth, Player player) {
     *         System.out.println(player.getName() + " started capturing " + koth.getName());
     *     }
     * });
     * 
     *
     * @param listener The listener to register
     * @throws IllegalArgumentException if the listener is null
     * @see KothListener
     */
    void registerListener(KothListener listener);

    /**
     * Unregisters a previously registered listener.
     * Once unregistered, the listener will stop receiving events.
     * If the listener was not registered, this method does nothing.
     *
     * @param listener The listener to unregister
     * @throws IllegalArgumentException if the listener is null
     */
    void unregisterListener(KothListener listener);

    /**
     * Gets the KothWars plugin instance.
     * Useful for accessing the plugin directly from the API,
     * for example to schedule tasks or access the configuration.
     *
     * @return The KothWars plugin instance
     */
    Plugin getPlugin();

    /**
     * Refreshes the internal KOTH cache.
     * For performance reasons, the API maintains an internal cache of
     * KOTHs that is automatically updated every 5 seconds. This method
     * allows forcing an immediate cache refresh.
     *
     * When should you use it?
     * <ul>
     *   <li>After renaming a KOTH (so the changes apply immediately)</li>
     *   <li>After creating or deleting a KOTH using external commands</li>
     *   <li>When you need to ensure you have the most up-to-date data</li>
     *
     * <strong>Note:</strong> Under normal conditions it is not necessary
     * to call this method, since the cache refreshes automatically.
     *
     * @since 1.1.0
     */
    void refreshCache();

    // ========== NEW METHODS (v1.2.0) ==========

    /**
     * Gets statistics for a specific player.
     * Returns comprehensive statistics about the player's performance
     * in all KOTH events across the server.
     *
     * @param playerUuid UUID of the player
     * @param playerName Current name of the player (used for display purposes)
     * @return Player statistics object
     * @since 1.2.0
     */
    PlayerStats getPlayerStats(UUID playerUuid, String playerName);

    /**
     * Gets statistics for a specific player.
     * Convenience method that uses the player's current name from the Player object.
     *
     * @param player The player
     * @return Player statistics object
     * @since 1.2.0
     */
    PlayerStats getPlayerStats(Player player);

    /**
     * Gets the top players by captures.
     * Useful for leaderboards and ranking systems.
     *
     * @param limit Maximum number of players to return
     * @return List of top players sorted by capture count (descending)
     * @since 1.2.0
     */
    List<PlayerStats> getTopPlayersByCaptures(int limit);

    /**
     * Gets the top players by kills.
     * Useful for leaderboards and ranking systems.
     *
     * @param limit Maximum number of players to return
     * @return List of top players sorted by kill count (descending)
     * @since 1.2.0
     */
    List<PlayerStats> getTopPlayersByKills(int limit);

    /**
     * Checks if clan integration is enabled.
     * When enabled, rewards are distributed to all online clan members
     * of the capturing player.
     *
     * @return true if clan integration is enabled
     * @since 1.2.0
     */
    boolean isClanIntegrationEnabled();

    /**
     * Gets the clan tag of a player.
     * Requires a compatible clan plugin and PlaceholderAPI.
     *
     * @param player The player
     * @return The clan tag, or empty string if no clan or integration disabled
     * @since 1.2.0
     */
    String getPlayerClanTag(Player player);

    /**
     * Gets all online members of a player's clan.
     * If clan integration is disabled or the player has no clan,
     * returns a list containing only the player.
     *
     * @param player The player
     * @return List of online clan members
     * @since 1.2.0
     */
    List<Player> getClanMembers(Player player);

    /**
     * Formats a player's name with their clan tag.
     * Uses the format defined in config.yml (capturer-format).
     * If clan integration is disabled or player has no clan,
     * returns just the player's name.
     *
     * @param player The player
     * @return Formatted display name with clan tag
     * @since 1.2.0
     */
    String formatPlayerNameWithClan(Player player);
}