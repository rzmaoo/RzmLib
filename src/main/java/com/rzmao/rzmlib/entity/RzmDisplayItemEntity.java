package com.rzmao.rzmlib.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class RzmDisplayItemEntity extends Entity {

    public static final TrackedData<Float> ROT_X = DataTracker.registerData(RzmDisplayItemEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Float> ROT_Y = DataTracker.registerData(RzmDisplayItemEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Float> ROT_Z = DataTracker.registerData(RzmDisplayItemEntity.class, TrackedDataHandlerRegistry.FLOAT);
    // 渲染的Item
    private static final TrackedData<ItemStack> ITEM_STACK = DataTracker.registerData(RzmDisplayItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public RzmDisplayItemEntity(EntityType<? extends RzmDisplayItemEntity> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    public ItemStack getStack() {
        return this.getDataTracker().get(ITEM_STACK);
    }

    public void setStack(ItemStack stack) {
        this.getDataTracker().set(ITEM_STACK, stack.copy());
    }

    public void setRotationOffset(float x, float y, float z) {
        this.getDataTracker().set(ROT_X, x);
        this.getDataTracker().set(ROT_Y, y);
        this.getDataTracker().set(ROT_Z, z);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(ITEM_STACK, new ItemStack(Items.STICK));
        builder.add(ROT_X, 0f);
        builder.add(ROT_Y, 0f);
        builder.add(ROT_Z, 0f);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Item", 10))
            this.setStack(ItemStack.fromNbt(this.getRegistryManager(), nbt.getCompound("Item")).orElse(ItemStack.EMPTY));

        if (nbt.contains("RotX")) this.getDataTracker().set(ROT_X, nbt.getFloat("RotX"));
        if (nbt.contains("RotY")) this.getDataTracker().set(ROT_Y, nbt.getFloat("RotY"));
        if (nbt.contains("RotZ")) this.getDataTracker().set(ROT_Z, nbt.getFloat("RotZ"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (!this.getStack().isEmpty()) nbt.put("Item", this.getStack().encode(this.getRegistryManager()));

        nbt.putFloat("RotX", this.getDataTracker().get(ROT_X));
        nbt.putFloat("RotY", this.getDataTracker().get(ROT_Y));
        nbt.putFloat("RotZ", this.getDataTracker().get(ROT_Z));
    }
}
