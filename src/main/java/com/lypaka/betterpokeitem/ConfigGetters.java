package com.lypaka.betterpokeitem;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class ConfigGetters {

    public static String commandPermission;
    public static List<String> itemStackLore;
    public static List<String> pokemonBlacklist;

    public static void load() throws ObjectMappingException {

        commandPermission = BetterPokeItem.configManager.getConfigNode(0, "Command-Permission").getString();
        itemStackLore = BetterPokeItem.configManager.getConfigNode(0, "Lore").getList(TypeToken.of(String.class));
        pokemonBlacklist = BetterPokeItem.configManager.getConfigNode(0, "Pokemon-Blacklist").getList(TypeToken.of(String.class));

    }

}
