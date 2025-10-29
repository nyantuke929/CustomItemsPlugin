package com.example.customitems.commands;

import com.example.customitems.api.NMSCustomItemAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomItemCommand implements CommandExecutor, TabCompleter {
    
    private final NMSCustomItemAPI itemAPI;
    
    public CustomItemCommand(NMSCustomItemAPI itemAPI) {
        this.itemAPI = itemAPI;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cプレイヤーのみ実行可能です");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 1) {
            player.sendMessage("§c使い方: /customitem <item_id> [amount]");
            player.sendMessage("§7利用可能なアイテム: " + String.join(", ", itemAPI.getRegisteredItemIds()));
            return true;
        }
        
        String itemId = args[0];
        int amount = 1;
        
        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount < 1 || amount > 64) {
                    player.sendMessage("§c個数は1~64の範囲で指定してください");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage("§c個数は数字で指定してください");
                return true;
            }
        }
        
        ItemStack item = itemAPI.getCustomItem(itemId, amount);
        if (item == null) {
            player.sendMessage("§cアイテムが見つかりません: " + itemId);
            player.sendMessage("§7利用可能なアイテム: " + String.join(", ", itemAPI.getRegisteredItemIds()));
            return true;
        }
        
        player.getInventory().addItem(item);
        player.sendMessage("§aカスタムアイテムを付与しました: §f" + itemId + " §7x" + amount);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // アイテムID補完
            for (String itemId : itemAPI.getRegisteredItemIds()) {
                if (itemId.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(itemId);
                }
            }
        } else if (args.length == 2) {
            // 個数補完
            completions.add("1");
            completions.add("16");
            completions.add("32");
            completions.add("64");
        }
        
        return completions;
    }
}