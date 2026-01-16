package com.rzmao.rzmlib.client.render;

import com.rzmao.rzmlib.entity.RzmDisplayItemEntity;
import com.rzmao.rzmlib.entity.RzmOrbitEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RzmDisplayItemEntityRenderer extends EntityRenderer<RzmDisplayItemEntity> {

    private static final Vector3f MODEL_TIP = new Vector3f(1f, 1f, 0f).normalize();
    private final ItemRenderer itemRenderer;

    public RzmDisplayItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(RzmDisplayItemEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        ItemStack stack = entity.getStack();
        if (stack.isEmpty()) return;

        matrices.push();

        boolean useVelocityLook = (entity instanceof RzmOrbitEntity orbit) && !orbit.getOrbitRender();
        Vec3d velocity = entity.getVelocity();

        if (!useVelocityLook || velocity.lengthSquared() < 1.0e-6) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));
        } else {
            Vec3d dir = velocity.normalize();
            matrices.multiply(new Quaternionf().rotationTo(MODEL_TIP, new Vector3f((float) dir.x, (float) dir.y, (float) dir.z)));
        }

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getDataTracker().get(RzmDisplayItemEntity.ROT_X)));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getDataTracker().get(RzmDisplayItemEntity.ROT_Y)));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.getDataTracker().get(RzmDisplayItemEntity.ROT_Z)));

        itemRenderer.renderItem(stack, ModelTransformationMode.NONE, light, OverlayTexture.DEFAULT_UV,
                matrices, vertexConsumers, entity.getWorld(), entity.getId());

        matrices.pop();
    }


    @Override
    public Identifier getTexture(RzmDisplayItemEntity entity) {
        return null;
    }
}
