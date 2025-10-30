package io.github.ntn929.paperitemapi.command;

import io.github.ntn929.paperitemapi.PaperItemAPI;
import io.github.ntn929.paperitemapi.item.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * /listitems コマンドの実装
 * 
 * @author ntn929
 */
public class ListItemsCommand implements CommandExecutor, TabCompleter {

    private final PaperItemAPI plugin;

    public ListItemsCommand(PaperItemAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                            @NotNull String label, @NotNull String[] args) {
        
        // namespace指定
        String namespace = args.length > 0 ? args[0] : null;
        
        if (namespace != null) {
            listByNamespace(sender, namespace);
        } else {
            listAll(sender);
        }
        
        return true;
    }

    /**
     * すべてのカスタムアイテムを一覧表示
     */
    private void listAll(CommandSender sender) {
        Collection<CustomItem> items = plugin.getItemRegistry().getAllItems();
        
        sender.sendMessage(Component.text("╔════════════════════════════════════════╗")
            .color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("║ ")
            .color(NamedTextColor.GOLD)
            .append(Component.text("   All Custom Items").color(NamedTextColor.YELLOW))
            .append(Component.text("              ║").color(NamedTextColor.GOLD)));
        sender.sendMessage(Component.text("╚════════════════════════════════════════╝")
            .color(NamedTextColor.GOLD));
        
        if (items.isEmpty()) {
            sender.sendMessage(Component.text("  No custom items registered")
                .color(NamedTextColor.GRAY));
            return;
        }

        sender.sendMessage(Component.text("Total: " + items.size() + " items")
            .color(NamedTextColor.AQUA));
        sender.sendMessage(Component.empty());

        // namespaceごとにグループ化
        Map<String, List<CustomItem>> grouped = items.stream()
            .collect(Collectors.groupingBy(
                item -> plugin.getItemRegistry().extractNamespace(item.getCustomId())
            ));

        for (Map.Entry<String, List<CustomItem>> entry : grouped.entrySet()) {
            String ns = entry.getKey();
            List<CustomItem> namespaceItems = entry.getValue();

            sender.sendMessage(Component.text("▼ " + ns + " (" + namespaceItems.size() + ")")
                .color(NamedTextColor.GOLD));

            for (CustomItem item : namespaceItems) {
                displayItem(sender, item);
            }
            
            sender.sendMessage(Component.empty());
        }
    }

    /**
     * 特定のnamespaceのアイテムを一覧表示
     */
    private void listByNamespace(CommandSender sender, String namespace) {
        List<CustomItem> items = plugin.getItemRegistry().getByNamespace(namespace);
        
        sender.sendMessage(Component.text("╔════════════════════════════════════════╗")
            .color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("║ ")
            .color(NamedTextColor.GOLD)
            .append(Component.text("   Custom Items [" + namespace + "]").color(NamedTextColor.YELLOW))
            .append(Component.text("      ║").color(NamedTextColor.GOLD)));
        sender.sendMessage(Component.text("╚════════════════════════════════════════╝")
            .color(NamedTextColor.GOLD));
        
        if (items.isEmpty()) {
            sender.sendMessage(Component.text("  No items found in namespace: " + namespace)
                .color(NamedTextColor.GRAY));
            return;
        }

        sender.sendMessage(Component.text("Found: " + items.size() + " items")
            .color(NamedTextColor.AQUA));
        sender.sendMessage(Component.empty());

        for (CustomItem item : items) {
            displayItem(sender, item);
        }
    }

    /**
     * アイテム情報を表示
     */
    private void displayItem(CommandSender sender, CustomItem item) {
        Component displayName = item.getDisplayName();
        String name = displayName != null ? 
            net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                .serialize(displayName) : 
            item.getBaseMaterial().name();

        Material material = item.getBaseMaterial();
        int componentCount = item.getComponents().size();

        sender.sendMessage(Component.text("  • ")
            .color(NamedTextColor.GRAY)
            .append(Component.text(item.getCustomId()).color(NamedTextColor.AQUA))
            .append(Component.text(" - ").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(name).color(NamedTextColor.WHITE)));

        sender.sendMessage(Component.text("    Material: ")
            .color(NamedTextColor.GRAY)
            .append(Component.text(material.name()).color(NamedTextColor.GREEN))
            .append(Component.text(" | Components: ").color(NamedTextColor.GRAY))
            .append(Component.text(String.valueOf(componentCount)).color(NamedTextColor.YELLOW)));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, 
                                                @NotNull Command command, 
                                                @NotNull String alias, 
                                                @NotNull String[] args) {
        if (args.length == 1) {
            Set<String> namespaces = plugin.getItemRegistry().getNamespaces();
            return new ArrayList<>(namespaces).stream()
                .filter(ns -> ns.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return Collections.emptyList();
    }
}