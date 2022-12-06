package com.lypaka.betterpokeitem.Commands;

import com.lypaka.betterpokeitem.BetterPokeItem;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = BetterPokeItem.MOD_ID)
public class BetterPokeItemCommand {

    public static List<String> ALIASES = Arrays.asList("betterpokeitem", "pokeitem", "bpi");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new ConvertCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
