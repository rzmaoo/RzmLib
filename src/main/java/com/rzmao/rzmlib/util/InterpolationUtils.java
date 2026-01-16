package com.rzmao.rzmlib.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class InterpolationUtils {

    /**
     * 二次贝塞尔曲线插值 (P0: 起点, C: 控制点, P2: 终点)
     */
    public static Vec3d bezier2(Vec3d p0, Vec3d c, Vec3d p2, double t) {
        double u = 1.0 - t;
        return new Vec3d(
                u * u * p0.x + 2 * u * t * c.x + t * t * p2.x,
                u * u * p0.y + 2 * u * t * c.y + t * t * p2.y,
                u * u * p0.z + 2 * u * t * c.z + t * t * p2.z
        );
    }

    /**
     * 二次贝塞尔一阶导数 (用于计算运动中的切线方向/实体朝向)
     */
    public static Vec3d bezier2Deriv(Vec3d p0, Vec3d c, Vec3d p2, double t) {
        return c.subtract(p0).multiply(2.0 * (1.0 - t)).add(p2.subtract(c).multiply(2.0 * t));
    }

    /**
     * 对 Vec3d 进行线性插值 (比手动算更简洁)
     */
    public static Vec3d lerpVec3d(Vec3d start, Vec3d end, double t) {
        return new Vec3d(
                MathHelper.lerp(t, start.x, end.x),
                MathHelper.lerp(t, start.y, end.y),
                MathHelper.lerp(t, start.z, end.z)
        );
    }

    public static double getProgress(long startTime, long currentTime, int durationTicks) {
        // 将 tick 转换为毫秒进行更高精度的比较 (1 tick = 50ms)
        double elapsed = currentTime - startTime;
        double durationMs = durationTicks * 50.0;
        return Math.min(1.0, elapsed / durationMs);
    }
}