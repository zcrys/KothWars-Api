package zcrys.api.koth;

import org.bukkit.plugin.Plugin;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Main interface of the KothWars API.
 * <p>
 * Provides access to all KOTHs registered in the plugin and allows
 * registering listeners to receive events related to KOTHs.
 * This API is designed for other plugins that want to integrate
 * with KothWars.
 * </p>
 *
 * <h2>Basic usage:</h2>
 * <pre>
 * // Get the API instance
 * KothAPI api = (KothAPI) Bukkit.getServicesManager().load(KothAPI.class);
 *
 * // Get a KOTH by name
 * Optional<Koth> koth = api.getKoth("myKoth");
 *
 * // Register a listener
 * api.registerListener(new MyKothListener());
 * </pre>
 *
 * @author zcrys
 * @version 1.1.0
 * @see Koth
 * @see KothListener
 */
public interface KothAPI {

    /**
     * Gets a collection containing all KOTHs registered in the plugin.
     * <p>
     * The returned collection is a read-only view of the currently
     * loaded KOTHs. Any modification to the collection will not
     * affect the actual KOTH objects.
     * </p>
     *
     * @return Immutable collection of all KOTHs
     */
    Collection<Koth> getKoths();

    /**
     * Searches for a KOTH by its name.
     * <p>
     * The name is the main identifier of the KOTH and must be unique.
     * The search is case-insensitive.
     * </p>
     *
     * @param name Name of the KOTH to search
     * @return Optional containing the KOTH if found, or empty if not
     * @throws IllegalArgumentException if the name is null
     */
    Optional<Koth> getKoth(String name);

    /**
     * Searches for a KOTH by its unique UUID.
     * <p>
     * Unlike the name, the UUID is internally generated and never
     * changes, even if the KOTH is renamed. This makes it ideal
     * for persistent references.
     * </p>
     *
     * @param id UUID of the KOTH to search
     * @return Optional containing the KOTH if found, or empty if not
     * @throws IllegalArgumentException if the UUID is null
     */
    Optional<Koth> getKoth(UUID id);

    /**
     * Checks whether a KOTH exists with the specified name.
     * <p>
     * Utility method to quickly verify whether a name
     * is already in use without retrieving the Koth object.
     * </p>
     *
     * @param name Name of the KOTH to check
     * @return true if a KOTH with that name exists, false otherwise
     * @throws IllegalArgumentException if the name is null
     */
    boolean hasKoth(String name);

    /**
     * Registers a listener to receive KOTH events.
     * <p>
     * Registered listeners will receive notifications when events
     * occur such as KOTH start, capture progress, players entering
     * or leaving the area, etc.
     * </p>
     *
     * <h3>Example usage:</h3>
     * <pre>
     * api.registerListener(new KothListener() {
     *     &#64;Override
     *     public void onCaptureStart(Koth koth, Player player) {
     *         System.out.println(player.getName() + " started capturing " + koth.getName());
     *     }
     * });
     * </pre>
     *
     * @param listener The listener to register
     * @throws IllegalArgumentException if the listener is null
     * @see KothListener
     */
    void registerListener(KothListener listener);

    /**
     * Unregisters a previously registered listener.
     * <p>
     * Once unregistered, the listener will stop receiving events.
     * If the listener was not registered, this method does nothing.
     * </p>
     *
     * @param listener The listener to unregister
     * @throws IllegalArgumentException if the listener is null
     */
    void unregisterListener(KothListener listener);

    /**
     * Gets the KothWars plugin instance.
     * <p>
     * Useful for accessing the plugin directly from the API,
     * for example to schedule tasks or access the configuration.
     * </p>
     *
     * @return The KothWars plugin instance
     */
    Plugin getPlugin();

    /**
     * Refreshes the internal KOTH cache.
     * <p>
     * For performance reasons, the API maintains an internal cache of
     * KOTHs that is automatically updated every 5 seconds. This method
     * allows forcing an immediate cache refresh.
     * </p>
     *
     * <h3>When should you use it?</h3>
     * <ul>
     *   <li>After renaming a KOTH (so the changes apply immediately)</li>
     *   <li>After creating or deleting a KOTH using external commands</li>
     *   <li>When you need to ensure you have the most up-to-date data</li>
     * </ul>
     *
     * <p>
     * <strong>Note:</strong> Under normal conditions it is not necessary
     * to call this method, since the cache refreshes automatically.
     * </p>
     *
     * @since 1.1.0
     */
    void refreshCache();
}