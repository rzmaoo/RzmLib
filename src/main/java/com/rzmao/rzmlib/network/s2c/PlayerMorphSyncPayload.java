package com.rzmao.rzmlib.network.s2c;

import com.rzmao.rzmlib.RzmLib;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record PlayerMorphSyncPayload(UUID playerId, boolean active, String morphId) implements CustomPayload {
    public static final Id<PlayerMorphSyncPayload> ID = new Id<>(Identifier.of(RzmLib.MOD_ID, "player_morph_sync"));

    public static final PacketCodec<RegistryByteBuf, PlayerMorphSyncPayload> CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, PlayerMorphSyncPayload::playerId,
            PacketCodecs.BOOL, PlayerMorphSyncPayload::active,
            PacketCodecs.STRING, PlayerMorphSyncPayload::morphId,
            PlayerMorphSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}