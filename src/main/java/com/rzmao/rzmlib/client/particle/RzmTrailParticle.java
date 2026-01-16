package com.rzmao.rzmlib.client.particle;

import com.rzmao.rzmlib.particle.RzmTrailEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
public class RzmTrailParticle extends AnimatedParticle {

    private static final float INITIAL_ALPHA = 0.8F;

    RzmTrailParticle(ClientWorld world, double x, double y, double z,
                     double vx, double vy, double vz,
                     RzmTrailEffect effect, SpriteProvider spriteProvider) {

        super(world, x, y, z, spriteProvider, 0.0F);

        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;

        this.maxAge = effect.age();
        this.scale *= 0.75F * effect.scale();

        this.setColor(effect.color().x, effect.color().y, effect.color().z);
        this.alpha = INITIAL_ALPHA;

        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();

        float lifeRatio = (float) this.age / (float) this.maxAge;
        this.alpha = INITIAL_ALPHA * (1.0F - lifeRatio);

        if (this.alpha < 0f) this.alpha = 0f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<RzmTrailEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(RzmTrailEffect effect, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
            return new RzmTrailParticle(world, x, y, z, vx, vy, vz, effect, this.spriteProvider);
        }
    }
}