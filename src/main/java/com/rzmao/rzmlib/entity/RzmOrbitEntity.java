package com.rzmao.rzmlib.entity;

import com.rzmao.rzmlib.util.MathUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 使 RzmOrbitEntity 绕着 Owner 旋转。
 */
@Getter
@Setter
public class RzmOrbitEntity extends RzmDisplayItemEntity {

    // 使用 DataTracker 存储主人的 UUID
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(RzmOrbitEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Float> ORBIT_SPEED = DataTracker.registerData(RzmOrbitEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> START_ANGLE_OFFSET = DataTracker.registerData(RzmOrbitEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> ORBIT_RENDER = DataTracker.registerData(RzmOrbitEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private Entity owner; // 缓存 OWNER_UUID 的字段
    private double radius = 1.5;
    private double yOffset = 1.0;


    public RzmOrbitEntity(EntityType<? extends RzmOrbitEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ORBIT_SPEED, 0.1f);
        builder.add(OWNER_UUID, Optional.empty());
        builder.add(START_ANGLE_OFFSET, 0.0f);
        builder.add(ORBIT_RENDER, true);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("OwnerUUID")) this.getDataTracker().set(OWNER_UUID, Optional.of(nbt.getUuid("OwnerUUID")));
        if (nbt.contains("Radius")) this.radius = nbt.getDouble("Radius");
        if (nbt.contains("YOffset")) this.yOffset = nbt.getDouble("YOffset");
        if (nbt.contains("OrbitSpeed")) this.setOrbitSpeed(nbt.getFloat("OrbitSpeed"));
        if (nbt.contains("StartAngle")) this.setStartAngleOffset(nbt.getFloat("StartAngle"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.getDataTracker().get(OWNER_UUID).ifPresent(uuid -> nbt.putUuid("OwnerUUID", uuid));
        nbt.putDouble("Radius", this.radius);
        nbt.putDouble("YOffset", this.yOffset);
        nbt.putFloat("OrbitSpeed", this.getOrbitSpeed());
        nbt.putFloat("StartAngle", this.getStartAngleOffset());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) return;
        if (this.getOrbitRender()) updateOrbitPosition(1.0f);
        checkCollision();
    }

    public void updateOrbitPosition(float tickDelta) {
        Entity currentOwner = getOwner();
        if (currentOwner == null) return;

        float speed = getOrbitSpeed();
        double time = (double) this.getWorld().getTime() + tickDelta;
        float currentAngleRad = (float) (time * speed + (double) this.getStartAngleOffset());

        Vec3d ownerPos = currentOwner.getPos();
        Vec3d targetPos = MathUtils.getOrbitPosition(ownerPos, this.getRadius(), currentAngleRad, this.getYOffset());

        this.setYaw((float) Math.toDegrees(currentAngleRad));
        this.refreshPositionAndAngles(targetPos.x, targetPos.y, targetPos.z, this.getYaw(), 0.0f);
    }

    private void checkCollision() {
        Box hitBox = this.getBoundingBox().expand(0.4);
        List<LivingEntity> livingTargets = this.getWorld().getEntitiesByClass(
                LivingEntity.class, hitBox, (e) -> e != getOwner() && e.isAlive()
        );

        for (LivingEntity target : livingTargets) this.onEntityHit(target);

        BlockPos currentPos = this.getBlockPos();
        BlockState state = this.getWorld().getBlockState(currentPos);

        if (state.isFullCube(this.getWorld(), currentPos)) this.onBlockHit(currentPos, state);
    }

    protected void onEntityHit(LivingEntity target) {
    }

    protected void onBlockHit(BlockPos pos, BlockState state) {
    }

    public float getStartAngleOffset() {
        return this.getDataTracker().get(START_ANGLE_OFFSET);
    }

    public void setStartAngleOffset(float offset) {
        this.getDataTracker().set(START_ANGLE_OFFSET, offset);
    }

    public boolean getOrbitRender() {
        return this.getDataTracker().get(ORBIT_RENDER);
    }

    public void setOrbitRender(boolean state) {
        this.getDataTracker().set(ORBIT_RENDER, state);
    }

    /* 获取Owner，如果缓存为空，通过 UUID 查找 */
    public Entity getOwner() {
        if (this.owner != null && this.owner.isAlive()) return this.owner;

        UUID uuid = this.getDataTracker().get(OWNER_UUID).orElse(null);
        if (uuid == null) return null;

        World world = this.getWorld();
        Entity found = null;

        if (world instanceof ServerWorld serverWorld) {
            found = serverWorld.getEntity(uuid);
        } else if (world.isClient) {
            found = world.getPlayerByUuid(uuid);
        }

        this.owner = found;
        return found;
    }

    public void setOwner(Entity owner) {
        if (owner == null) return;

        this.owner = owner;
        this.getDataTracker().set(OWNER_UUID, Optional.of(owner.getUuid()));
    }

    public Optional<UUID> getOwnerUuid() {
        return this.getDataTracker().get(OWNER_UUID);
    }

    public float getOrbitSpeed() {
        return this.getDataTracker().get(ORBIT_SPEED);
    }

    public void setOrbitSpeed(float speed) {
        this.getDataTracker().set(ORBIT_SPEED, speed);
    }

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance) || distance < 64 * 64;
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return this.getBoundingBox().expand(5.0);
    }
}