package com.rzmao.rzmlib.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class EntityUtils {
    /**
     * 寻找最近的实体
     *
     * @param origin 搜索的中心实体（通常是搜索者自己）
     * @param radius 搜索半径
     * @param filter 过滤条件（决定哪些实体符合要求）
     * @return 最近的符合条件的实体
     */
    @Nullable
    public static Entity findNearestEntity(Entity origin, double radius, Predicate<Entity> filter) {
        World world = origin.getWorld();

        List<Entity> entities = world.getOtherEntities(origin, origin.getBoundingBox().expand(radius));

        return entities.stream()
                .filter(filter)
                .min(Comparator.comparingDouble(origin::squaredDistanceTo))
                .orElse(null);
    }
}
