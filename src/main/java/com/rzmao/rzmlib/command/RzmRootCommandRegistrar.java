package com.rzmao.rzmlib.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

@FunctionalInterface
public interface RzmRootCommandRegistrar {
    void register(
            LiteralArgumentBuilder<ServerCommandSource> root,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment
    );
}
