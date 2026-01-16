package com.rzmao.rzmlib.mixin;

import com.rzmao.rzmlib.particle.RzmParticles;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "sendToPlayerIfNearby", at = @At("HEAD"), cancellable = true)
    private void onCheckDistance(ServerPlayerEntity player, boolean force, double x, double y, double z, Packet<?> packet, CallbackInfoReturnable<Boolean> cir) {
        if (packet instanceof ParticleS2CPacket particlePacket) {
            // 提高我的自定义粒子发包距离，默认是32
            if (particlePacket.getParameters().getType() == RzmParticles.TRAIL_EFFECT) {

                if (player.getWorld() == (Object) this) {
                    if (player.getBlockPos().isWithinDistance(new Vec3d(x, y, z), 128.0)) {
                        player.networkHandler.sendPacket(packet);
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}