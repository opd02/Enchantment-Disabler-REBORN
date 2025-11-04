package me.opd.enchantmentdisabler.utils;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.configuration.ConfigurationSection;

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
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("enchants");
        if(section == null){
            section = plugin.getConfig().createSection("enchants");
        }

        // Load existing configured enchants first
        for(String path : section.getKeys(true)){
            Enchantment ench = Enchantment.getByName(path);
            if(ench != null){
                EnchantmentDisablerPlugin.blockedEnchants.put(ench, plugin.getConfig().getBoolean("enchants." + path));
            }
        }

        // Auto-populate any missing/new enchantments with default 'false' (allowed)
        for(Enchantment ench : Enchantment.values()){
            if(!EnchantmentDisablerPlugin.blockedEnchants.containsKey(ench)){
                EnchantmentDisablerPlugin.blockedEnchants.put(ench, false);
                plugin.getConfig().set("enchants." + ench.getName(), false);
            }
        }

        plugin.saveConfig();
    }
}
