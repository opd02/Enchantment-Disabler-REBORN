package me.opd.enchantmentdisabler.utils;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.enchantments.Enchantment;

public class ConfigUtilsEN {
    @SuppressWarnings("deprecation")
    public static void syncHashMapWithConfig(EnchantmentDisablerPlugin plugin){

        for(Enchantment en : EnchantmentDisablerPlugin.blockedEnchants.keySet()){
            plugin.getConfig().set("enchants." + en.getName(), EnchantmentDisablerPlugin.blockedEnchants.get(en));
        }
        plugin.saveConfig();
    }

    @SuppressWarnings("deprecation")
    public static void loadHashMapFromConfig(EnchantmentDisablerPlugin plugin){
        for(String path : plugin.getConfig().getConfigurationSection("enchants").getKeys(true)){
            EnchantmentDisablerPlugin.blockedEnchants.put(Enchantment.getByName(path), (Boolean)plugin.getConfig().getBoolean("enchants." + path));

        }
    }
}
