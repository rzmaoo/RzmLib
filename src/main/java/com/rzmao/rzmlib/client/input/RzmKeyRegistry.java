package com.rzmao.rzmlib.client.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class RzmKeyRegistry {
    private static final Map<Integer, Predicate<MinecraftClient>> MONITORED_KEYS = new HashMap<>();
    private static final Map<KeyBinding, Predicate<MinecraftClient>> MONITORED_BINDINGS = new HashMap<>();

    // 默认总是允许发包
    private static final Predicate<MinecraftClient> ALWAYS_TRUE = client -> true;

    // 带条件的发包
    public static void registerKey(int keyCode, Predicate<MinecraftClient> condition) {
        MONITORED_KEYS.put(keyCode, condition);
    }

    // 带条件的发包
    public static void registerBinding(KeyBinding binding, Predicate<MinecraftClient> condition) {
        MONITORED_BINDINGS.put(binding, condition);
    }

    public static void registerKey(int keyCode) {
        registerKey(keyCode, ALWAYS_TRUE);
    }

    public static void registerBinding(KeyBinding binding) {
        registerBinding(binding, ALWAYS_TRUE);
    }

    public static Map<Integer, Predicate<MinecraftClient>> getMonitoredKeys() {
        return MONITORED_KEYS;
    }

    public static Map<KeyBinding, Predicate<MinecraftClient>> getMonitoredBindings() {
        return MONITORED_BINDINGS;
    }
}