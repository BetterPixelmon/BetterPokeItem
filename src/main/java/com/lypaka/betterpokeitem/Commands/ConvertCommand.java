package com.lypaka.betterpokeitem.Commands;

import com.lypaka.betterpokeitem.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;

public class ConvertCommand {

    public ConvertCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterPokeItemCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("convert")
                                            .then(
                                                    Commands.argument("slot", IntegerArgumentType.integer(1))
                                                            .then(
                                                                    Commands.argument("lock", StringArgumentType.word())
                                                                            .executes(c -> {

                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                    if (!ConfigGetters.commandPermission.equalsIgnoreCase("")) {

                                                                                        if (!PermissionHandler.hasPermission(player, ConfigGetters.commandPermission)) {

                                                                                            player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                            return 0;

                                                                                        }

                                                                                    }

                                                                                    int slot = IntegerArgumentType.getInteger(c, "slot");
                                                                                    boolean lock = Boolean.parseBoolean(StringArgumentType.getString(c, "lock"));
                                                                                    slot = slot - 1;
                                                                                    PlayerPartyStorage storage = StorageProxy.getParty(player);
                                                                                    if (storage.countAblePokemon() == 1) {

                                                                                        player.sendMessage(FancyText.getFormattedText("&cCan't convert your only Pokemon!"), player.getUniqueID());
                                                                                        return 0;

                                                                                    }
                                                                                    Pokemon pokemon = storage.get(slot);
                                                                                    if (pokemon == null) {

                                                                                        player.sendMessage(FancyText.getFormattedText("&cNo Pokemon in this slot!"), player.getUniqueID());
                                                                                        return 0;

                                                                                    }

                                                                                    // I hate doing shit this way because of lazy typers, lol
                                                                                    for (String s : ConfigGetters.pokemonBlacklist) {

                                                                                        if (pokemon.getLocalizedName().equalsIgnoreCase(s)) {

                                                                                            player.sendMessage(FancyText.getFormattedText("&cPokemon is blacklisted from conversion!"), player.getUniqueID());
                                                                                            return 0;

                                                                                        }

                                                                                    }

                                                                                    ItemStack sprite = SpriteItemHelper.getPhoto(pokemon);
                                                                                    sprite.setDisplayName(FancyText.getFormattedText("&e" + pokemon.getLocalizedName()));

                                                                                    // pre-making Pokemon data, lol
                                                                                    Attack a1 = pokemon.getMoveset().get(0);
                                                                                    Attack a2 = pokemon.getMoveset().get(1);
                                                                                    Attack a3 = pokemon.getMoveset().get(2);
                                                                                    Attack a4 = pokemon.getMoveset().get(3);
                                                                                    String attack1 = a1 == null ? "None" : a1.getActualMove().getAttackName();
                                                                                    String attack2 = a2 == null ? "None" : a2.getActualMove().getAttackName();
                                                                                    String attack3 = a3 == null ? "None" : a3.getActualMove().getAttackName();
                                                                                    String attack4 = a4 == null ? "None" : a4.getActualMove().getAttackName();
                                                                                    String moves = attack1 + ", " + attack2 + ", " + attack3 + ", " + attack4;

                                                                                    int ivHP = pokemon.getIVs().getArray()[0];
                                                                                    int ivAtk = pokemon.getIVs().getArray()[1];
                                                                                    int ivDef = pokemon.getIVs().getArray()[2];
                                                                                    int ivSpAtk = pokemon.getIVs().getArray()[3];
                                                                                    int ivSpDef = pokemon.getIVs().getArray()[4];
                                                                                    int ivSpeed = pokemon.getIVs().getArray()[5];
                                                                                    String ivs = ivHP + ", " + ivAtk + ", " + ivDef + ", " + ivSpAtk + ", " + ivSpDef + ", " + ivSpeed;
                                                                                    String ivPercent = pokemon.getIVs().getPercentageString(2);

                                                                                    int evHP = pokemon.getEVs().getArray()[0];
                                                                                    int evAtk = pokemon.getEVs().getArray()[1];
                                                                                    int evDef = pokemon.getEVs().getArray()[2];
                                                                                    int evSpAtk = pokemon.getEVs().getArray()[3];
                                                                                    int evSpDef = pokemon.getEVs().getArray()[4];
                                                                                    int evSpeed = pokemon.getEVs().getArray()[5];
                                                                                    String evs = evHP + ", " + evAtk + ", " + evDef + ", " + evSpAtk + ", " + evSpDef + ", " + evSpeed;

                                                                                    String form = pokemon.getForm().getLocalizedName();
                                                                                    String palette = pokemon.getPalette().getName();
                                                                                    String nickname = pokemon.getFormattedNickname().getString();
                                                                                    String ot = pokemon.getOriginalTrainer() == null ? "None" : pokemon.getOriginalTrainer();
                                                                                    String otUUID = pokemon.getOriginalTrainerUUID() == null ? "None" : pokemon.getOriginalTrainerUUID().toString();

                                                                                    String currentHP = String.valueOf(pokemon.getHealth());
                                                                                    String maxHP = String.valueOf(pokemon.getMaxHealth());
                                                                                    String heldItem = pokemon.getHeldItem().getItem().getRegistryName().toString();
                                                                                    if (heldItem.equalsIgnoreCase("minecraft:air")) {

                                                                                        heldItem = "none";

                                                                                    }

                                                                                    String mintNature = pokemon.getMintNature() == null ? "none" : pokemon.getMintNature().getString();
                                                                                    String gmax = String.valueOf(pokemon.hasGigantamaxFactor());
                                                                                    String currentEXP = String.valueOf(pokemon.getExperience());
                                                                                    String neededEXP = String.valueOf(pokemon.getExperienceToLevelUp());
                                                                                    String dynamaxLevel = String.valueOf(pokemon.getDynamaxLevel());

                                                                                    ListNBT lore = new ListNBT();
                                                                                    for (String s : ConfigGetters.itemStackLore) {

                                                                                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s
                                                                                                .replace("%shiny%", String.valueOf(pokemon.isShiny()))
                                                                                                .replace("%level%", String.valueOf(pokemon.getPokemonLevel()))
                                                                                                .replace("%nature%", pokemon.getNature().getLocalizedName())
                                                                                                .replace("%ability%", pokemon.getAbilityName())
                                                                                                .replace("%growth%", pokemon.getGrowth().getLocalizedName())
                                                                                                .replace("%gender%", pokemon.getGender().getLocalizedName())
                                                                                                .replace("%moves%", moves)
                                                                                                .replace("%ivs%", ivs)
                                                                                                .replace("%ivPercent%", ivPercent)
                                                                                                .replace("%evs%", evs)
                                                                                                .replace("%form%", form)
                                                                                                .replace("%palette%", palette)
                                                                                                .replace("%nickname%", nickname)
                                                                                                .replace("%ot%", ot)
                                                                                                .replace("%otuuid%", otUUID)
                                                                                                .replace("%isEgg%", String.valueOf(pokemon.isEgg()))
                                                                                                .replace("%friendship%", String.valueOf(pokemon.getFriendship()))
                                                                                                .replace("%current%", currentHP)
                                                                                                .replace("%max%", maxHP)
                                                                                                .replace("%heldItem%", heldItem)
                                                                                                .replace("%gmax%", gmax)
                                                                                                .replace("%currentEXP%", currentEXP)
                                                                                                .replace("%neededEXP%", neededEXP)
                                                                                                .replace("%mintNature%", mintNature)
                                                                                                .replace("%dynamaxLevel%", dynamaxLevel)
                                                                                        ))));

                                                                                    }

                                                                                    if (lock) {

                                                                                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(""))));
                                                                                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&dLocked By: &e" + player.getName().getString() + " / " + player.getUniqueID()))));

                                                                                    }

                                                                                    sprite.getOrCreateChildTag("display").put("Lore", lore);
                                                                                    player.addItemStackToInventory(sprite);
                                                                                    storage.set(slot, null);
                                                                                    player.sendMessage(FancyText.getFormattedText("&aSuccessfully itemized your " + pokemon.getLocalizedName() + "!"), player.getUniqueID());
                                                                                    return 1;

                                                                                }

                                                                                return 0;

                                                                            })
                                                            )
                                                            .executes(c -> {

                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                    if (!ConfigGetters.commandPermission.equalsIgnoreCase("")) {

                                                                        if (!PermissionHandler.hasPermission(player, ConfigGetters.commandPermission)) {

                                                                            player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                            return 0;

                                                                        }

                                                                    }

                                                                    int slot = IntegerArgumentType.getInteger(c, "slot");
                                                                    boolean lock = false;
                                                                    slot = slot - 1;
                                                                    PlayerPartyStorage storage = StorageProxy.getParty(player);
                                                                    if (storage.countAblePokemon() == 1) {

                                                                        player.sendMessage(FancyText.getFormattedText("&cCan't convert your only Pokemon!"), player.getUniqueID());
                                                                        return 0;

                                                                    }
                                                                    Pokemon pokemon = storage.get(slot);
                                                                    if (pokemon == null) {

                                                                        player.sendMessage(FancyText.getFormattedText("&cNo Pokemon in this slot!"), player.getUniqueID());
                                                                        return 0;

                                                                    }

                                                                    // I hate doing shit this way because of lazy typers, lol
                                                                    for (String s : ConfigGetters.pokemonBlacklist) {

                                                                        if (pokemon.getLocalizedName().equalsIgnoreCase(s)) {

                                                                            player.sendMessage(FancyText.getFormattedText("&cPokemon is blacklisted from conversion!"), player.getUniqueID());
                                                                            return 0;

                                                                        }

                                                                    }

                                                                    ItemStack sprite = SpriteItemHelper.getPhoto(pokemon);
                                                                    sprite.setDisplayName(FancyText.getFormattedText("&e" + pokemon.getLocalizedName()));

                                                                    // pre-making Pokemon data, lol
                                                                    Attack a1 = pokemon.getMoveset().get(0);
                                                                    Attack a2 = pokemon.getMoveset().get(1);
                                                                    Attack a3 = pokemon.getMoveset().get(2);
                                                                    Attack a4 = pokemon.getMoveset().get(3);
                                                                    String attack1 = a1 == null ? "None" : a1.getActualMove().getAttackName();
                                                                    String attack2 = a2 == null ? "None" : a2.getActualMove().getAttackName();
                                                                    String attack3 = a3 == null ? "None" : a3.getActualMove().getAttackName();
                                                                    String attack4 = a4 == null ? "None" : a4.getActualMove().getAttackName();
                                                                    String moves = attack1 + ", " + attack2 + ", " + attack3 + ", " + attack4;

                                                                    int ivHP = pokemon.getIVs().getArray()[0];
                                                                    int ivAtk = pokemon.getIVs().getArray()[1];
                                                                    int ivDef = pokemon.getIVs().getArray()[2];
                                                                    int ivSpAtk = pokemon.getIVs().getArray()[3];
                                                                    int ivSpDef = pokemon.getIVs().getArray()[4];
                                                                    int ivSpeed = pokemon.getIVs().getArray()[5];
                                                                    String ivs = ivHP + ", " + ivAtk + ", " + ivDef + ", " + ivSpAtk + ", " + ivSpDef + ", " + ivSpeed;
                                                                    String ivPercent = pokemon.getIVs().getPercentageString(2);

                                                                    int evHP = pokemon.getEVs().getArray()[0];
                                                                    int evAtk = pokemon.getEVs().getArray()[1];
                                                                    int evDef = pokemon.getEVs().getArray()[2];
                                                                    int evSpAtk = pokemon.getEVs().getArray()[3];
                                                                    int evSpDef = pokemon.getEVs().getArray()[4];
                                                                    int evSpeed = pokemon.getEVs().getArray()[5];
                                                                    String evs = evHP + ", " + evAtk + ", " + evDef + ", " + evSpAtk + ", " + evSpDef + ", " + evSpeed;

                                                                    String form = pokemon.getForm().getLocalizedName();
                                                                    String palette = pokemon.getPalette().getName();
                                                                    String nickname = pokemon.getFormattedNickname().getString();
                                                                    String ot = pokemon.getOriginalTrainer() == null ? "None" : pokemon.getOriginalTrainer();
                                                                    String otUUID = pokemon.getOriginalTrainerUUID() == null ? "None" : pokemon.getOriginalTrainerUUID().toString();

                                                                    String currentHP = String.valueOf(pokemon.getHealth());
                                                                    String maxHP = String.valueOf(pokemon.getMaxHealth());
                                                                    String heldItem = pokemon.getHeldItem().getItem().getRegistryName().toString();
                                                                    if (heldItem.equalsIgnoreCase("minecraft:air")) {

                                                                        heldItem = "none";

                                                                    }

                                                                    String mintNature = pokemon.getMintNature() == null ? "none" : pokemon.getMintNature().getString();
                                                                    String gmax = String.valueOf(pokemon.hasGigantamaxFactor());
                                                                    String currentEXP = String.valueOf(pokemon.getExperience());
                                                                    String neededEXP = String.valueOf(pokemon.getExperienceToLevelUp());
                                                                    String dynamaxLevel = String.valueOf(pokemon.getDynamaxLevel());

                                                                    ListNBT lore = new ListNBT();
                                                                    for (String s : ConfigGetters.itemStackLore) {

                                                                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s
                                                                                .replace("%shiny%", String.valueOf(pokemon.isShiny()))
                                                                                .replace("%level%", String.valueOf(pokemon.getPokemonLevel()))
                                                                                .replace("%nature%", pokemon.getNature().getLocalizedName())
                                                                                .replace("%ability%", pokemon.getAbilityName())
                                                                                .replace("%growth%", pokemon.getGrowth().getLocalizedName())
                                                                                .replace("%gender%", pokemon.getGender().getLocalizedName())
                                                                                .replace("%moves%", moves)
                                                                                .replace("%ivs%", ivs)
                                                                                .replace("%ivPercent%", ivPercent)
                                                                                .replace("%evs%", evs)
                                                                                .replace("%form%", form)
                                                                                .replace("%palette%", palette)
                                                                                .replace("%nickname%", nickname)
                                                                                .replace("%ot%", ot)
                                                                                .replace("%otuuid%", otUUID)
                                                                                .replace("%isEgg%", String.valueOf(pokemon.isEgg()))
                                                                                .replace("%friendship%", String.valueOf(pokemon.getFriendship()))
                                                                                .replace("%current%", currentHP)
                                                                                .replace("%max%", maxHP)
                                                                                .replace("%heldItem%", heldItem)
                                                                                .replace("%gmax%", gmax)
                                                                                .replace("%currentEXP%", currentEXP)
                                                                                .replace("%neededEXP%", neededEXP)
                                                                                .replace("%mintNature%", mintNature)
                                                                                .replace("%dynamaxLevel%", dynamaxLevel)
                                                                        ))));

                                                                    }

                                                                    if (lock) {

                                                                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(""))));
                                                                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&dLocked By: &e" + player.getName().getString() + " / " + player.getUniqueID()))));

                                                                    }

                                                                    sprite.getOrCreateChildTag("display").put("Lore", lore);
                                                                    player.addItemStackToInventory(sprite);
                                                                    storage.set(slot, null);
                                                                    player.sendMessage(FancyText.getFormattedText("&aSuccessfully itemized your " + pokemon.getLocalizedName() + "!"), player.getUniqueID());
                                                                    return 1;

                                                                }

                                                                return 0;

                                                            })
                                            )
                            )
            );

        }

    }

}
