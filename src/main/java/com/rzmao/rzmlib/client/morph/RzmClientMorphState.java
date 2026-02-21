package com.rzmao.rzmlib.client.morph;

import com.rzmao.rzmlib.RzmLib;
import com.rzmao.rzmlib.network.s2c.PlayerMorphSyncPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端玩家变形状态缓存，供渲染层查询当前玩家形态
 */
public final class RzmClientMorphState {
    private static final ConcurrentHashMap<UUID, Identifier> ACTIVE_MORPHS = new ConcurrentHashMap<>();

    private RzmClientMorphState() {
    }

    public static void applySync(PlayerMorphSyncPayload payload) {
        if (!payload.active()) {
            ACTIVE_MORPHS.remove(payload.playerId());
            return;
        }

        Identifier morphId = Identifier.tryParse(payload.morphId());
        if (morphId == null) {
            ACTIVE_MORPHS.remove(payload.playerId());
            RzmLib.LOGGER.warn("Received invalid morph id from server: " + payload.morphId());
            return;
        }

        ACTIVE_MORPHS.put(payload.playerId(), morphId);
    }

    @Nullable
    public static Identifier getMorph(UUID playerId) {
        return ACTIVE_MORPHS.get(playerId);
    }

    public static boolean isMorphedAs(UUID playerId, Identifier morphId) {
        Identifier activeMorph = ACTIVE_MORPHS.get(playerId);
        return morphId.equals(activeMorph);
    }

    public static void clear() {
        ACTIVE_MORPHS.clear();
    }
}
