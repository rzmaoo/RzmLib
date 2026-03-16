package com.rzmao.rzmlib.client.input;

import com.rzmao.rzmlib.network.c2s.KeyInputPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class RzmInputHandler {
    private static final Map<String, Boolean> lastKeyState = new HashMap<>();

    public static void onClientTick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null) {
            releaseTrackedKeys();
            return;
        }

        long handle = client.getWindow().getHandle();

        RzmKeyRegistry.getMonitoredKeys().forEach((keyCode, condition) -> {
            // 只有当自定义条件满足时，才去检测按键状态，然后发包
            if (condition.test(client)) {
                detectAndSend(handle, keyCode, keyCode >= 0 && keyCode <= 7, "");
            } else {
                resetKeyState(keyCode, "");
            }
        });

        RzmKeyRegistry.getMonitoredBindings().forEach((binding, condition) -> {
            if (condition.test(client)) {
                InputUtil.Key boundKey = InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey());
                int keyCode = boundKey.getCode();
                boolean isMouse = boundKey.getCategory() == InputUtil.Type.MOUSE;
                detectAndSend(handle, keyCode, isMouse, binding.getTranslationKey());
            } else {
                int keyCode = InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey()).getCode();
                resetKeyState(keyCode, binding.getTranslationKey());
            }
        });
    }

    private static void detectAndSend(long handle, int keyCode, boolean isMouse, String action) {
        if (keyCode == -1) return;

        boolean isPressed = isMouse ?
                GLFW.glfwGetMouseButton(handle, keyCode) == GLFW.GLFW_PRESS :
                InputUtil.isKeyPressed(handle, keyCode);

        String stateKey = getStateKey(keyCode, action);
        boolean wasPressed = lastKeyState.getOrDefault(stateKey, false);

        if (isPressed != wasPressed) {
            ClientPlayNetworking.send(new KeyInputPayload(keyCode, action, isPressed));
        }

        lastKeyState.put(stateKey, isPressed);
    }

    private static void resetKeyState(int keyCode, String action) {
        if (keyCode == -1) return;

        String stateKey = getStateKey(keyCode, action);
        boolean wasPressed = lastKeyState.getOrDefault(stateKey, false);
        if (wasPressed) {
            ClientPlayNetworking.send(new KeyInputPayload(keyCode, action, false));
        }
        lastKeyState.put(stateKey, false);
    }

    private static void releaseTrackedKeys() {
        RzmKeyRegistry.getMonitoredKeys().forEach((keyCode, condition) -> resetKeyState(keyCode, ""));
        RzmKeyRegistry.getMonitoredBindings().forEach((binding, condition) -> {
            int keyCode = InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey()).getCode();
            resetKeyState(keyCode, binding.getTranslationKey());
        });
    }

    private static String getStateKey(int keyCode, String action) {
        // 自定义绑定优先按 action 追踪，避免不同功能绑到同一键位时互相覆盖状态
        return action.isEmpty() ? "#" + keyCode : action;
    }
}
