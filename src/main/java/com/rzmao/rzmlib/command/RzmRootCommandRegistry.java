package com.rzmao.rzmlib.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.rzmao.rzmlib.RzmLib;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RzmRootCommandRegistry {
    private static final CopyOnWriteArrayList<RzmRootCommandRegistrar> REGISTRARS = new CopyOnWriteArrayList<>();
    private static final AtomicBoolean REGISTERED = new AtomicBoolean(false);

    private RzmRootCommandRegistry() {
    }

    public static void registerRootCommand() {
        if (!REGISTERED.compareAndSet(false, true)) {
            return;
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("rzm");

            for (RzmRootCommandRegistrar registrar : REGISTRARS) {
                try {
                    registrar.register(root, registryAccess, environment);
                } catch (Exception exception) {
                    RzmLib.LOGGER.error("Failed to register /rzm subcommand.", exception);
                }
            }

            dispatcher.register(root);
        });
    }

    public static void registerSubcommand(RzmRootCommandRegistrar registrar) {
        if (registrar == null) {
            return;
        }

        REGISTRARS.addIfAbsent(registrar);
    }

    public static void unregisterSubcommand(RzmRootCommandRegistrar registrar) {
        if (registrar == null) {
            return;
        }

        REGISTRARS.remove(registrar);
    }
}
