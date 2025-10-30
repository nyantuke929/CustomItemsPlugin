package com.example.customitems.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.example.customitems.api.VersionedCustomItemAPI;

/**
 * カスタムアイテムコマンドの実装
 */
public class CustomItemCommand implements CommandExecutor, TabCompleter {
    
    private final VersionedCustomItemAPI api;
    
    public CustomItemCommand(VersionedCustomItemAPI api) {
        this.api = api;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cこのコマンドはプレイヤーのみ実行できます");
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "give" -> {
                if (args.length < 2) {
                    player.sendMessage("§c使用法: /customitem give <アイテムID> [数量]");
                    return true;
                }
                
                String itemId = args[1];
                int amount = 1;
                
                if (args.length >= 3) {
                    try {
                        amount = Integer.parseInt(args[2]);
                        if (amount < 1 || amount > 64) {
                            player.sendMessage("§c数量は1から64の間で指定してください");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§c数量は数字で指定してください");
                        return true;
                    }
                }
                
                ItemStack item = api.getCustomItem(itemId, amount);
                if (item == null) {
                    player.sendMessage("§cアイテム '" + itemId + "' は存在しません");
                    return true;
                }
                
                player.getInventory().addItem(item);
                player.sendMessage("§aカスタムアイテム '" + itemId + "' を " + amount + " 個付与しました");
                return true;
            }
            
            case "list" -> {
                Set<String> itemIds = api.getRegisteredItemIds();
                player.sendMessage("§6§l=== カスタムアイテム一覧 ===");
                player.sendMessage("§e登録されているアイテム数: §f" + itemIds.size());
                player.sendMessage("§7--------------------------");
                
                for (String id : itemIds) {
                    player.sendMessage("§b• §f" + id);
                }
                
                return true;
            }
            
            case "check" -> {
                ItemStack handItem = player.getInventory().getItemInMainHand();
                
                if (handItem.getType().isAir()) {
                    player.sendMessage("§c手にアイテムを持ってください");
                    return true;
                }
                
                if (api.isCustomItem(handItem)) {
                    String itemId = api.getCustomItemId(handItem);
                    player.sendMessage("§aこのアイテムはカスタムアイテムです");
                    player.sendMessage("§eアイテムID: §f" + itemId);
                } else {
                    player.sendMessage("§cこのアイテムはカスタムアイテムではありません");
                }
                
                return true;
            }
            
            case "version" -> {
                player.sendMessage("§6§l=== API情報 ===");
                player.sendMessage("§eサポートバージョン: §f" + api.getSupportedVersion());
                player.sendMessage("§e登録アイテム数: §f" + api.getRegisteredItemIds().size());
                return true;
            }
            
            default -> {
                sendHelp(player);
                return true;
            }
        }
    }
    
    private void sendHelp(Player player) {
        player.sendMessage("§6§l=== カスタムアイテムコマンド ===");
        player.sendMessage("§e/customitem give <ID> [数量] §7- アイテムを入手");
        player.sendMessage("§e/customitem list §7- 登録されているアイテム一覧");
        player.sendMessage("§e/customitem check §7- 手持ちアイテムを確認");
        player.sendMessage("§e/customitem version §7- API情報を表示");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.add("give");
            completions.add("list");
            completions.add("check");
            completions.add("version");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(api.getRegisteredItemIds());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.add("1");
            completions.add("8");
            completions.add("16");
            completions.add("32");
            completions.add("64");
        }
        
        return completions;
    }
}