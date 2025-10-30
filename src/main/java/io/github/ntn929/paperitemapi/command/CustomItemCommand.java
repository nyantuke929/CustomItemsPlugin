package io.github.ntn929.paperitemapi.command;

import io.github.ntn929.paperitemapi.PaperItemAPI;
import io.github.ntn929.paperitemapi.item.CustomItem;
import io.github.ntn929.paperitemapi.util.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /customitem コマンドの実装
 * 
 * @author ntn929
 */
public class CustomItemCommand implements CommandExecutor, TabCompleter {

    private final PaperItemAPI plugin;

    public CustomItemCommand(PaperItemAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                            @NotNull String label, @NotNull String[] args) {
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "give":
                return handleGive(sender, args);
            case "list":
                return handleList(sender, args);
            case "info":
                return handleInfo(sender, args);
            case "reload":
                return handleReload(sender);
            default:
                sender.sendMessage(Component.text("Unknown subcommand: " + subCommand)
                    .color(NamedTextColor.RED));
                sendHelp(sender);
                return true;
        }
    }

    /**
     * /customitem give <player> <itemId> [amount]
     */
    private boolean handleGive(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /customitem give <player> <itemId> [amount]")
                .color(NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Component.text("Player not found: " + args[1])
                .color(NamedTextColor.RED));
            return true;
        }

        String itemId = args[2];
        CustomItem customItem = plugin.getItemRegistry().get(itemId);
        
        if (customItem == null) {
            sender.sendMessage(Component.text("Custom item not found: " + itemId)
                .color(NamedTextColor.RED));
            return true;
        }

        int amount = 1;
        if (args.length >= 4) {
            try {
                amount = Integer.parseInt(args[3]);
                if (amount < 1 || amount > 64) {
                    sender.sendMessage(Component.text("Amount must be between 1 and 64")
                        .color(NamedTextColor.RED));
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid amount: " + args[3])
                    .color(NamedTextColor.RED));
                return true;
            }
        }

        ItemStack item = customItem.toItemStack(amount);
        target.getInventory().addItem(item);

        sender.sendMessage(Component.text("Gave " + amount + "x ")
            .color(NamedTextColor.GREEN)
            .append(Component.text(itemId).color(NamedTextColor.YELLOW))
            .append(Component.text(" to ").color(NamedTextColor.GREEN))
            .append(Component.text(target.getName()).color(NamedTextColor.AQUA)));

        target.sendMessage(Component.text("You received ")
            .color(NamedTextColor.GREEN)
            .append(Component.text(amount + "x " + itemId).color(NamedTextColor.YELLOW)));

        return true;
    }

    /**
     * /customitem list [namespace]
     */
    private boolean handleList(CommandSender sender, String[] args) {
        String namespace = args.length >= 2 ? args[1] : null;
        
        List<CustomItem> items;
        if (namespace != null) {
            items = plugin.getItemRegistry().getByNamespace(namespace);
            sender.sendMessage(Component.text("=== Custom Items [" + namespace + "] ===")
                .color(NamedTextColor.GOLD));
        } else {
            items = new ArrayList<>(plugin.getItemRegistry().getAllItems());
            sender.sendMessage(Component.text("=== All Custom Items ===")
                .color(NamedTextColor.GOLD));
        }

        if (items.isEmpty()) {
            sender.sendMessage(Component.text("No items found")
                .color(NamedTextColor.GRAY));
            return true;
        }

        sender.sendMessage(Component.text("Total: " + items.size())
            .color(NamedTextColor.YELLOW));

        for (CustomItem item : items) {
            Component displayName = item.getDisplayName();
            String name = displayName != null ? 
                net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                    .serialize(displayName) : 
                item.getBaseMaterial().name();

            sender.sendMessage(Component.text("  - ")
                .color(NamedTextColor.GRAY)
                .append(Component.text(item.getCustomId()).color(NamedTextColor.AQUA))
                .append(Component.text(" (").color(NamedTextColor.GRAY))
                .append(Component.text(name).color(NamedTextColor.WHITE))
                .append(Component.text(")").color(NamedTextColor.GRAY)));
        }

        return true;
    }

    /**
     * /customitem info [itemId]
     * 手に持っているアイテムまたは指定したアイテムの情報を表示
     */
    private boolean handleInfo(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players")
                .color(NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;
        CustomItem customItem;

        if (args.length >= 2) {
            // アイテムIDが指定された場合
            String itemId = args[1];
            customItem = plugin.getItemRegistry().get(itemId);
            
            if (customItem == null) {
                sender.sendMessage(Component.text("Custom item not found: " + itemId)
                    .color(NamedTextColor.RED));
                return true;
            }
        } else {
            // 手に持っているアイテム
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            customItem = plugin.getItemRegistry().getFromItemStack(heldItem);
            
            if (customItem == null) {
                sender.sendMessage(Component.text("You are not holding a custom item")
                    .color(NamedTextColor.RED));
                return true;
            }
        }

        // 情報を表示
        sender.sendMessage(Component.text("=== Custom Item Info ===")
            .color(NamedTextColor.GOLD));
        
        String info = ItemUtils.getCustomItemInfo(customItem);
        for (String line : info.split("\n")) {
            sender.sendMessage(Component.text(line).color(NamedTextColor.GRAY));
        }

        return true;
    }

    /**
     * /customitem reload
     */
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("paperitemapi.admin")) {
            sender.sendMessage(Component.text("You don't have permission to reload")
                .color(NamedTextColor.RED));
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(Component.text("Configuration reloaded!")
            .color(NamedTextColor.GREEN));
        
        return true;
    }

    /**
     * ヘルプメッセージを送信
     */
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== PaperItemAPI Commands ===")
            .color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/customitem give <player> <itemId> [amount]")
            .color(NamedTextColor.YELLOW)
            .append(Component.text(" - Give a custom item").color(NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/customitem list [namespace]")
            .color(NamedTextColor.YELLOW)
            .append(Component.text(" - List custom items").color(NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/customitem info [itemId]")
            .color(NamedTextColor.YELLOW)
            .append(Component.text(" - Show item info").color(NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/customitem reload")
            .color(NamedTextColor.YELLOW)
            .append(Component.text(" - Reload configuration").color(NamedTextColor.GRAY)));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, 
                                                @NotNull Command command, 
                                                @NotNull String alias, 
                                                @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("give");
            completions.add("list");
            completions.add("info");
            completions.add("reload");
            return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("list")) {
                return new ArrayList<>(plugin.getItemRegistry().getNamespaces());
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return plugin.getItemRegistry().getRegisteredIds().stream()
                .filter(id -> id.toLowerCase().startsWith(args[2].toLowerCase()))
                .collect(Collectors.toList());
        }

        return completions;
    }
}