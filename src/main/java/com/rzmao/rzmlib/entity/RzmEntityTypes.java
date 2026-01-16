package com.rzmao.rzmlib.entity;

import com.rzmao.rzmlib.RzmLib;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RzmEntityTypes {

    public static final EntityType<RzmDisplayItemEntity> DISPLAY_ITEM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(RzmLib.MOD_ID, "display_item"),
            EntityType.Builder.create(RzmDisplayItemEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .maxTrackingRange(1024)
                    .build()
    );

    public static final EntityType<RzmOrbitEntity> ORBIT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(RzmLib.MOD_ID, "orbit"),
            EntityType.Builder.create(RzmOrbitEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .maxTrackingRange(1024)
                    .build()
    );

    public static void register() {}

}
