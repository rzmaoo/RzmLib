package com.rzmao.rzmlib.morph;

import com.rzmao.rzmlib.RzmLib;
import com.rzmao.rzmlib.network.s2c.PlayerMorphSyncPayload;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务端玩家变形状态管理器
 */
public final class RzmPlayerMorphManager {
    private static final ConcurrentHashMap<UUID, Identifier> ACTIVE_MORPHS = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<RzmPlayerMorphListener> LISTENERS = new CopyOnWriteArrayList<>();
    private static final AtomicBoolean CALLBACKS_REGISTERED = new AtomicBoolean(false);

    private RzmPlayerMorphManager() {
    }

    public static void registerLifecycleCallbacks() {
        if (!CALLBACKS_REGISTERED.compareAndSet(false, true)) {
            return;
        }

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity joiningPlayer = handler.getPlayer();
            ACTIVE_MORPHS.forEach((playerId, morphId) -> sendSync(joiningPlayer, playerId, morphId));
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            Identifier removedMorph = ACTIVE_MORPHS.remove(player.getUuid());

            if (removedMorph != null) {
                broadcastSync(server, player.getUuid(), null);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> ACTIVE_MORPHS.clear());
    }

    public static void registerListener(RzmPlayerMorphListener listener) {
        LISTENERS.add(listener);
    }

    public static void unregisterListener(RzmPlayerMorphListener listener) {
        LISTENERS.remove(listener);
    }

    public static Optional<Identifier> getMorph(UUID playerId) {
        return Optional.ofNullable(ACTIVE_MORPHS.get(playerId));
    }

    public static boolean isMorphedAs(UUID playerId, Identifier morphId) {
        Identifier activeMorph = ACTIVE_MORPHS.get(playerId);
        return morphId.equals(activeMorph);
    }

    public static void setMorph(ServerPlayerEntity player, Identifier morphId) {
        UUID playerId = player.getUuid();
        Identifier previousMorph = ACTIVE_MORPHS.put(playerId, morphId);

        if (morphId.equals(previousMorph)) {
            return;
        }

        MinecraftServer server = player.getServer();
        if (server != null) {
            broadcastSync(server, playerId, morphId);
        }

        fireMorphChanged(player, previousMorph, morphId);
    }

    public static void clearMorph(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        Identifier previousMorph = ACTIVE_MORPHS.remove(playerId);

        if (previousMorph == null) {
            return;
        }

        MinecraftServer server = player.getServer();
        if (server != null) {
            broadcastSync(server, playerId, null);
        }

        fireMorphChanged(player, previousMorph, null);
    }

    public static boolean toggleMorph(ServerPlayerEntity player, Identifier morphId) {
        if (isMorphedAs(player.getUuid(), morphId)) {
            clearMorph(player);
            return false;
        }

        setMorph(player, morphId);
        return true;
    }

    private static void fireMorphChanged(ServerPlayerEntity player, @Nullable Identifier previousMorph, @Nullable Identifier currentMorph) {
        for (RzmPlayerMorphListener listener : LISTENERS) {
            try {
                listener.onMorphChanged(player, previousMorph, currentMorph);
            } catch (Exception exception) {
                RzmLib.LOGGER.error("Morph listener failed.", exception);
            }
        }
    }

    private static void broadcastSync(MinecraftServer server, UUID playerId, @Nullable Identifier morphId) {
        for (ServerPlayerEntity target : server.getPlayerManager().getPlayerList()) {
            sendSync(target, playerId, morphId);
        }
    }

    private static void sendSync(ServerPlayerEntity target, UUID playerId, @Nullable Identifier morphId) {
        boolean active = morphId != null;
        String morphIdString = active ? morphId.toString() : "";
        ServerPlayNetworking.send(target, new PlayerMorphSyncPayload(playerId, active, morphIdString));
    }
}
