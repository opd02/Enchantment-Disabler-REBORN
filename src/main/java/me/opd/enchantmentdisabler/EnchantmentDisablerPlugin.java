package me.opd.enchantmentdisabler;

import me.opd.enchantmentdisabler.commands.MenuCommand;
import me.opd.enchantmentdisabler.listeners.*;
import me.opd.enchantmentdisabler.utils.ConfigUtilsEN;
import me.opd.enchantmentdisabler.utils.EnchantUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class EnchantmentDisablerPlugin extends JavaPlugin {

    public static HashMap<Enchantment, Boolean> blockedEnchants;
    public static ArrayList<Enchantment> allowedEnchant;
    public static EnchantUtils enchantUtils;

    public void onEnable(){

        Bukkit.getServer().getPluginManager().registerEvents(new PrepareItemEnchantListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemClickListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EnchantItemListener(), this);

        if(this.getConfig().getBoolean("PurgeExistingDisabledEnchantedItemsToo")) {
            Bukkit.getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new EntityPickupItemListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerConsumeItemListener(), this);
        }


        EnchantmentDisablerPlugin.blockedEnchants = new HashMap<>();
        EnchantmentDisablerPlugin.enchantUtils = new EnchantUtils();

        Bukkit.getServer().getPluginCommand("ed").setExecutor(new MenuCommand(this));

        getConfig().options().copyDefaults(true);
        saveConfig();

        ConfigUtilsEN.loadHashMapFromConfig(this);
        //System.out.println(EnchantmentDisablerPlugin.blockedEnchants.toString());
        rebuildAllowedEnchant();
    }

    public void onDisable(){
        ConfigUtilsEN.syncHashMapWithConfig(this);
    }

    public void reloadPluginConfigAndCaches(){
        reloadConfig();
        EnchantmentDisablerPlugin.blockedEnchants.clear();
        EnchantmentDisablerPlugin.allowedEnchant.clear();
        ConfigUtilsEN.loadHashMapFromConfig(this);
        rebuildAllowedEnchant();
    }

    private void rebuildAllowedEnchant(){
        allowedEnchant = new ArrayList<>();
        for(Enchantment en : EnchantmentDisablerPlugin.blockedEnchants.keySet()){
            if(Boolean.FALSE.equals(EnchantmentDisablerPlugin.blockedEnchants.get(en))){
                EnchantmentDisablerPlugin.allowedEnchant.add(en);
            }
        }
    }
}
