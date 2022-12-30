package net.pvpmines.utils;

import java.math.BigInteger;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.storage.ProtocolStorage;
import protocolsupport.zplatform.ServerPlatform;

public class ProtocolSupportAPI {

    private ProtocolSupportAPI() {
    }

    private static final BigInteger apiversion = BigInteger.valueOf(15);
    /**
     * Returns ProtocolSupport API version <br>
     * This number is incremented every time API changes (behavior change, method/field added/removed)
     * @return API version
     */
    public static @Nonnull BigInteger getAPIVersion() {
        return apiversion;
    }

    /**
     * Returns player {@link ProtocolVersion} <br>
     * Returns {@link ProtocolVersion#UNKNOWN} if player is not online or is not a real player
     * @param player player
     * @return player protocol version or UNKNOWN
     */
    public static @Nonnull ProtocolVersion getProtocolVersion(Player player) {

        Connection connection = getConnection(player);
        return connection != null ? connection.getVersion() : ProtocolVersion.UNKNOWN;
    }

    /**
     * Returns {@link ProtocolVersion} of connection with specified address <br>
     * Returns {@link ProtocolVersion#UNKNOWN} if there is no connection with specified address
     * @param address address
     * @return connection protocol version or UNKNOWN
     */
    public static @Nonnull ProtocolVersion getProtocolVersion(SocketAddress address) {
        Connection connection = getConnection(address);
        return connection != null ? connection.getVersion() : ProtocolVersion.UNKNOWN;
    }

    /**
     * Returns all currently active connections
     * @return all currently active connections
     */
    public static @Nonnull List<Connection> getConnections() {
        return new ArrayList<>(ProtocolStorage.getConnections());
    }

    /**
     * Returns player {@link Connection} <br>
     * Returns null if player is not a real player
     * @param player player
     * @return player {@link Connection} or null
     */
    public static @Nullable Connection getConnection(@Nonnull Player player) {
        Validate.notNull(player, "Player can't be null");
        Connection connection = ServerPlatform.get().getMiscUtils().getConnection(player);
        return connection != null ? connection : getConnection(player.spigot().getRawAddress());
    }

    /**
     * Returns connection with specified address <br>
     * Returns null if there is no connection with specified address
     * @param address address
     * @return {@link Connection} with specified address
     */
    public static @Nullable Connection getConnection(@Nonnull SocketAddress address) {
        return ProtocolStorage.getConnection(address);
    }


    private static final Set<ProtocolVersion> enabledVersions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    static {
        enabledVersions.addAll(Arrays.asList(ProtocolVersion.getAllSupported()));
    }

    /**
     * Enables protocol version support if it was disabled previously <br>
     * By default all supported versions are enabled
     * @param version protocol version which support needs to be enabled
     */
    public static void enableProtocolVersion(@Nonnull ProtocolVersion version) {
        Validate.isTrue(version.isSupported(), "Can't enable support for version that is not supported at all");
        enabledVersions.add(version);
    }

    /**
     * Disables protocol version support, players with that version won't be able to login to server and ping will report server as unsupported version <br>
     * Disabling all versions before 1.9 will allow multiple passengers on entity
     * @param version protocol version which support needs to be disabled
     */
    public static void disableProtocolVersion(@Nonnull ProtocolVersion version) {
        Validate.isTrue(version.isSupported(), "Can't disable support for version that is not supported at all");
        enabledVersions.remove(version);
    }

    /**
     * Return true if protocol version support is enabled
     * @param version protocol version for which support enabled should be checked
     * @return true if protocol version support is enabled, false otherwise
     */
    public static boolean isProtocolVersionEnabled(@Nonnull ProtocolVersion version) {
        return enabledVersions.contains(version);
    }

    /**
     * Returns all currently enabled protocol versions
     * @return all currently enabled protocol versions
     */
    public static @Nonnull Collection<ProtocolVersion> getEnabledProtocolVersions() {
        return new ArrayList<>(enabledVersions);
    }

}