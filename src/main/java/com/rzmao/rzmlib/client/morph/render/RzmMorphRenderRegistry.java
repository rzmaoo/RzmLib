package com.rzmao.rzmlib.client.morph.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 变形渲染器注册表，维护 morphId 到 renderer 工厂与实例缓存的映射
 * 子模组在这里注册自定义变形模型
 */
public final class RzmMorphRenderRegistry {
    private static final ConcurrentHashMap<Identifier, RzmMorphPlayerRendererFactory> RENDERER_FACTORIES = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Identifier, RzmMorphPlayerRenderer> RENDERER_CACHE = new ConcurrentHashMap<>();

    private RzmMorphRenderRegistry() {
    }

    public static void register(Identifier morphId, RzmMorphPlayerRendererFactory rendererFactory) {
        RENDERER_FACTORIES.put(morphId, rendererFactory);
        RENDERER_CACHE.remove(morphId);
    }

    @Nullable
    public static RzmMorphPlayerRenderer getRenderer(Identifier morphId, EntityRendererFactory.Context context) {
        RzmMorphPlayerRenderer cachedRenderer = RENDERER_CACHE.get(morphId);
        if (cachedRenderer != null) {
            return cachedRenderer;
        }

        RzmMorphPlayerRendererFactory rendererFactory = RENDERER_FACTORIES.get(morphId);
        if (rendererFactory == null) {
            return null;
        }

        RzmMorphPlayerRenderer createdRenderer = rendererFactory.create(context);
        RzmMorphPlayerRenderer existingRenderer = RENDERER_CACHE.putIfAbsent(morphId, createdRenderer);
        return existingRenderer != null ? existingRenderer : createdRenderer;
    }

    public static void clearRendererCache() {
        RENDERER_CACHE.clear();
    }
}
