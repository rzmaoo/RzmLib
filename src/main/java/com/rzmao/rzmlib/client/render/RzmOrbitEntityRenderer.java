package com.rzmao.rzmlib.client.render;

import com.rzmao.rzmlib.entity.RzmDisplayItemEntity;
import com.rzmao.rzmlib.entity.RzmOrbitEntity;
import com.rzmao.rzmlib.util.MathUtils;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RzmOrbitEntityRenderer extends RzmDisplayItemEntityRenderer {
    public RzmOrbitEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(RzmDisplayItemEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        if (!(entity instanceof RzmOrbitEntity orbit)) return;

        var owner = orbit.getOwner();
        if (owner == null) return;

        matrices.push();
        Vec3d visualPos = entity.getLerpedPos(tickDelta);

        if (orbit.getOrbitRender()) {
            double renderTime = (double) entity.getWorld().getTime() + tickDelta;
            float angle = (float) (renderTime * orbit.getOrbitSpeed() + orbit.getStartAngleOffset());

            Vec3d ownerPos = owner.getLerpedPos(tickDelta);
            Vec3d targetPos = MathUtils.getOrbitPosition(ownerPos, orbit.getRadius(), angle, orbit.getYOffset());

            matrices.translate(targetPos.x - visualPos.x, targetPos.y - visualPos.y, targetPos.z - visualPos.z);
            yaw = (float) Math.toDegrees(angle);
        } else {
            yaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.getYaw());
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }

    @Override
    public boolean shouldRender(RzmDisplayItemEntity entity, Frustum frustum, double x, double y, double z) {
        if (entity instanceof RzmOrbitEntity orbit && orbit.getOwner() != null) return true;
        return super.shouldRender(entity, frustum, x, y, z);
    }
}