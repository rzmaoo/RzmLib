package com.rzmao.rzmlib.morph;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RzmPlayerMorphListener {
    void onMorphChanged(ServerPlayerEntity player, @Nullable Identifier previousMorph, @Nullable Identifier currentMorph);
}