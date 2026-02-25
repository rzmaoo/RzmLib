package com.rzmao.rzmlib.network.c2s;

import com.rzmao.rzmlib.RzmLib;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * 负责订阅按键点击的 Payload
 *
 * @param keyCode 按下的键位代码 (e.g. GLFW.GLFW_KEY_X)
 * @param action  按下的动作 (e.g. key.forward / key.attack)
 */
public record KeyInputPayload(int keyCode, String action, boolean pressed) implements CustomPayload {
    public static final Id<KeyInputPayload> ID = new Id<>(Identifier.of(RzmLib.MOD_ID, "key_input"));

    public static final PacketCodec<RegistryByteBuf, KeyInputPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, KeyInputPayload::keyCode,
            PacketCodecs.STRING, KeyInputPayload::action,
            PacketCodecs.BOOL, KeyInputPayload::pressed,
            KeyInputPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}