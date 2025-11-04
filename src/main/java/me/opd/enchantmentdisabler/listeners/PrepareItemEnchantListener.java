package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.bukkit.enchantments.Enchantment.*;

public class PrepareItemEnchantListener implements Listener {

    @EventHandler
    public void onPlayerPrepEnchant(PrepareItemEnchantEvent e){

        Player p = e.getEnchanter();
        final long seed = (long) p.getLevel() * 3469;

        for(EnchantmentOffer eo : e.getOffers()){
            if(eo == null){
                continue;
            }

            long altSeed = seed + eo.getCost();

            if(Boolean.TRUE.equals(EnchantmentDisablerPlugin.blockedEnchants.get(eo.getEnchantment()))){
                Enchantment replacementEnchant = EnchantmentDisablerPlugin.enchantUtils.newChosenEnchantment(e.getItem(), altSeed);

                int enchantLevel = ((eo.getCost() /30) * replacementEnchant.getMaxLevel());

                if(enchantLevel<1 || replacementEnchant.getMaxLevel() == 1){
                    enchantLevel = 1;
                }else if(enchantLevel == replacementEnchant.getMaxLevel()){
                    enchantLevel = enchantLevel - 1;
                } else if (enchantLevel > replacementEnchant.getMaxLevel()){
                    enchantLevel = replacementEnchant.getMaxLevel();
                }

                        eo.setEnchantment(replacementEnchant);
                        eo.setEnchantmentLevel(enchantLevel);
                        e.getEnchanter().updateInventory();
            }
        }
    }

}
