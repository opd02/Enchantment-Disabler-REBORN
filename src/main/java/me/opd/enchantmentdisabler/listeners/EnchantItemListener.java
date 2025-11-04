package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static org.bukkit.enchantments.Enchantment.*;
import static org.bukkit.enchantments.Enchantment.SWIFT_SNEAK;

public class EnchantItemListener implements Listener {
    @EventHandler
    public void onItemEnchant(EnchantItemEvent e){
        ArrayList<Enchantment> toChange = new ArrayList<>();

        if(!e.getEnchantsToAdd().keySet().contains(e.getEnchantmentHint())){

            int enchantLevel = ((e.getExpLevelCost() /30) * e.getEnchantmentHint().getMaxLevel());

            if(enchantLevel <1 || e.getEnchantmentHint().getMaxLevel() == 1){
                enchantLevel = 1;
            }else if(enchantLevel == e.getEnchantmentHint().getMaxLevel()){
                enchantLevel = enchantLevel - 1;
            } else if (enchantLevel > e.getEnchantmentHint().getMaxLevel()){
                enchantLevel = e.getEnchantmentHint().getMaxLevel();
            }

            e.getEnchantsToAdd().put(e.getEnchantmentHint(),enchantLevel);
        }

        for(Map.Entry<Enchantment, Integer> entry : e.getEnchantsToAdd().entrySet()){

            if(entry.getKey() == null){
                continue;
            }

            if(!Boolean.TRUE.equals(EnchantmentDisablerPlugin.blockedEnchants.get(entry.getKey()))){
                continue;
            }
            toChange.add(entry.getKey());
        }

        for(Enchantment en : toChange){
            e.getEnchantsToAdd().remove(en, e.getEnchantsToAdd().get(en));

//            int enchantLevel = ((e.getExpLevelCost()/30) * (replacementEnchant.getMaxLevel()));
//
//            if(enchantLevel < 1 || replacementEnchant.getMaxLevel() == 1){
//                enchantLevel=1;
//            }else if(enchantLevel == replacementEnchant.getMaxLevel()){
//                enchantLevel = enchantLevel - 1;
//            } else if (enchantLevel > replacementEnchant.getMaxLevel()){
//                enchantLevel = replacementEnchant.getMaxLevel();
//            }
//            e.getEnchantsToAdd().put(replacementEnchant, enchantLevel);
        }

        Enchantment conflictedOne = null;

        for(Enchantment ent : e.getEnchantsToAdd().keySet()){
            if(ent == e.getEnchantmentHint()){
                continue;
            }
            if(ent.conflictsWith(e.getEnchantmentHint())){
                //e.getEnchantsToAdd().remove(ent);
                conflictedOne = ent;
            }
        }
        if(conflictedOne != null){
            e.getEnchantsToAdd().remove(conflictedOne);
        }
    }

}
