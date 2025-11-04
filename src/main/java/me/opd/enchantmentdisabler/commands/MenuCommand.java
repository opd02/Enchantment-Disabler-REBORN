package me.opd.enchantmentdisabler.commands;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MenuCommand implements CommandExecutor {
    EnchantmentDisablerPlugin plugin;

    public MenuCommand(EnchantmentDisablerPlugin plugin){
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
        //need 5 rows for inv
        if(cmd.getLabel().equalsIgnoreCase("ed")){
            if(args != null && args.length > 0 && args[0].equalsIgnoreCase("reload")){
                if(!sender.hasPermission("enchantmentdisabler.reload")){
                    sender.sendMessage(ChatColor.RED + "You do not have permission to reload this plugin.");
                    return true;
                }
                plugin.reloadPluginConfigAndCaches();
                sender.sendMessage(ChatColor.GREEN + "EnchantmentDisabler config reloaded.");
                return true;
            }

            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "This command is for players only.");
                return true;
            }

            Player player = (Player) sender;

            if(!player.hasPermission("enchantmentdisabler.admincommand")){

                player.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
                return true;

            }

            Inventory inv = Bukkit.getServer().createInventory(null, 45, ChatColor.GRAY + "" + ChatColor.BOLD + "Select Enchants");
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

            for(Enchantment en : EnchantmentDisablerPlugin.blockedEnchants.keySet()){
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
                bookMeta.setDisplayName("§r§f§l" + en.getName());

                ArrayList<String> lore = new ArrayList<String>();
                lore.add("");
                if(Boolean.TRUE.equals(EnchantmentDisablerPlugin.blockedEnchants.get(en))){
                    lore.add("§c§lDISABLED");
                }else{
                    lore.add("§a§lENABLED");
                }
                String line = Boolean.TRUE.equals(EnchantmentDisablerPlugin.blockedEnchants.get(en)) ? ChatColor.GRAY + "§7Click to §aEnable" : "§7Click to §cDisable";
                lore.add(line);

                bookMeta.setLore(lore);

                bookMeta.addStoredEnchant(en, en.getMaxLevel(), true);

                book.setItemMeta(bookMeta);
                inv.addItem(book);
            }

            ItemStack help = new ItemStack(Material.PAPER, 1);
            ItemMeta helpMeta = help.getItemMeta();
            helpMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "-=-HELP-=-");
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GRAY + "Enchantments that are labeled as ENABLED");
            lore.add(ChatColor.GRAY + "are able to be obtained by survival players");
            lore.add(ChatColor.GRAY + "whereas enchantments that are labeled as DISABLED");
            lore.add(ChatColor.GRAY + "are not obtainable for survival players");
            helpMeta.setLore(lore);
            help.setItemMeta(helpMeta);
            inv.setItem(44, help);


            player.openInventory(inv);
        }
        return false;
    }
}
