package com.lypaka.betterpokeitem;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Utils {

    public static Pokemon rebuildPokemon (String species, List<String> specs) {

        Pokemon pokemon = PokemonBuilder.builder()
                .species(species)
                .build();

        for (String s : specs) {

            // This is incredibly fucking stupid
            s = s.replace("§0", "").replace("§1", "").replace("§2", "").replace("§3", "")
                    .replace("§4", "").replace("§5", "").replace("§6", "").replace("§7", "")
                    .replace("§8", "").replace("§9", "").replace("§a", "").replace("§b", "")
                    .replace("§c", "").replace("§d", "").replace("§e", "").replace("§f", "")
                    .replace("§k", "").replace("§l", "").replace("§m", "").replace("§n", "")
                    .replace("§o", "").replace("§r", "");
            if (s.contains(", ")) {

                String[] secondarySplit = s.split(": ");
                String pokemonSpec = secondarySplit[0].replace(":", "");
                String[] valueSplit = secondarySplit[1].split(", ");
                switch (pokemonSpec.toLowerCase()) {

                    case "moves":
                        pokemon.getMoveset().clear();
                        for (String move : valueSplit) {

                            Attack attack = new Attack(move);
                            if (!move.equalsIgnoreCase("none")) {

                                pokemon.getMoveset().add(attack);

                            }

                        }
                        break;

                    case "ivs":
                        int[] ivs = new int[6];
                        for (int i = 0; i < valueSplit.length; i++) {

                            ivs[i] = Integer.parseInt(valueSplit[i]);

                        }
                        pokemon.getIVs().fillFromArray(ivs);
                        break;

                    case "evs":
                        int[] evs = new int[6];
                        for (int e = 0; e < valueSplit.length; e++) {

                            evs[e] = Integer.parseInt(valueSplit[e]);

                        }
                        pokemon.getEVs().fillFromArray(evs);
                        break;

                }

            } else {

                try {

                    String[] split = s.split(": ");
                    String pokemonSpec = split[0];
                    String value = split[1];

                    switch (pokemonSpec.toLowerCase()) {

                        case "shiny":
                            pokemon.setShiny(Boolean.parseBoolean(value));
                            break;

                        case "level":
                            pokemon.setLevelNum(Integer.parseInt(value));
                            break;

                        case "nature":
                            pokemon.setNature(Nature.natureFromString(value));
                            break;

                        case "ability":
                            pokemon.setAbility(AbilityRegistry.getAbility(value));
                            break;

                        case "growth":
                            pokemon.setGrowth(EnumGrowth.getGrowthFromString(value));
                            break;

                        case "gender":
                            pokemon.setGender(Gender.getGender(value));
                            break;

                        case "form":
                            pokemon.setForm(value);
                            break;

                        case "palette":
                            if (!value.equalsIgnoreCase("none")) {

                                pokemon.setPalette(value);

                            }
                            break;

                        case "nickname":
                            if (!value.equalsIgnoreCase("none")) {

                                pokemon.setNickname(FancyText.getFormattedText(value));

                            }
                            break;

                        case "friendship":
                            pokemon.setFriendship(Integer.parseInt(value));
                            break;

                        case "egg":
                            if (Boolean.parseBoolean(value)) {

                                pokemon.makeEgg();

                            }
                            break;

                        case "item":
                            if (!value.equalsIgnoreCase("minecraft:air")) {

                                ItemStack item = ItemStackBuilder.buildFromStringID(value);
                                item.setCount(1);
                                pokemon.setHeldItem(item);

                            }
                            break;

                        case "dynamax level":
                            pokemon.setGigantamaxFactor(Boolean.parseBoolean(value));
                            break;

                        case "mint nature":
                            if (!value.equalsIgnoreCase("none")) {

                                pokemon.setMintNature(Nature.natureFromString(value));

                            }
                            break;

                    }

                } catch (ArrayIndexOutOfBoundsException er) {

                    // do nothing, will error out with nickname if none present

                }

            }

        }

        String ot = null;
        UUID otUUID = null;
        for (String s : specs) {

            if (s.contains("OT: ")) {

                ot = s.replace("OT: ", "");

            }
            if (s.contains("OTUUID: ")) {

                String value = s.replace("OTUUID: ", "");
                try {

                    otUUID = UUID.fromString(value);

                } catch (IllegalArgumentException er) {

                    BetterPokeItem.logger.error("Detected an issue with owner UUID on the Pokemon, setting to null");

                }

            }

        }

        if (specs.contains("Health")) {

            for (String s : specs) {

                if (s.contains("Health")) {

                    String[] split1 = s.split(": ");
                    String fullHealth = split1[1];
                    if (fullHealth.contains("/")) {

                        String current = fullHealth.split("/")[0];
                        pokemon.setHealth(Integer.parseInt(current));
                        break;

                    }

                }

            }

        }
        if (specs.contains("EXP")) {

            for (String s : specs) {

                if (s.contains("EXP")) {

                    String[] split1 = s.split(": ");
                    String exp = split1[1];
                    if (exp.contains("/")) {

                        String current = exp.split("/")[0];
                        pokemon.setExperience(Integer.parseInt(current));

                    }

                }

            }

        }

        pokemon.setOriginalTrainer(otUUID, ot);
        return pokemon;

    }

}
