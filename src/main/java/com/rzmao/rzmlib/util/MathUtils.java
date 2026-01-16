package com.rzmao.rzmlib.util;

import net.minecraft.util.math.Vec3d;

public class MathUtils {
    /**
     * 计算环绕坐标
     *
     * @param center       中心点坐标
     * @param radius       半径
     * @param angle        当前弧度
     * @param heightOffset 高度偏移
     * @return 计算后的三维坐标点
     */
    public static Vec3d getOrbitPosition(Vec3d center, double radius, float angle, double heightOffset) {
        double x = center.x + Math.cos(angle) * radius;
        double z = center.z + Math.sin(angle) * radius;
        double y = center.y + heightOffset;
        return new Vec3d(x, y, z);
    }
}
