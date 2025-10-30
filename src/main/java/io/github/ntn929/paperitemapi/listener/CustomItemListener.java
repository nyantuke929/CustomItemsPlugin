package io.github.ntn929.paperitemapi.listener;

import io.github.ntn929.paperitemapi.PaperItemAPI;
import io.github.ntn929.paperitemapi.item.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

/**
 * カスタムアイテムのイベントリスナー
 * 
 * @author ntn929
 */
public class CustomItemListener implements Listener {

    private final PaperItemAPI plugin;

    public CustomItemListener(PaperItemAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * プレイヤーがアイテムを使用した時
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        CustomItem customItem = plugin.getItemRegistry().getFromItemStack(item);
        if (customItem == null) {
            return;
        }

        // デバッグモード時のログ
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Player " + event.getPlayer().getName() + 
                " used custom item: " + customItem.getCustomId());
        }

        // カスタムアイテム使用イベント（拡張ポイント）
        // 必要に応じてカスタムイベントを発火させる
    }

    /**
     * プレイヤーがアイテムを持ち替えた時
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item == null) {
            return;
        }

        CustomItem customItem = plugin.getItemRegistry().getFromItemStack(item);
        if (customItem == null) {
            return;
        }

        // デバッグモード時のログ
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Player " + event.getPlayer().getName() + 
                " held custom item: " + customItem.getCustomId());
        }
    }

    /**
     * インベントリでアイテムをクリックした時
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }

        CustomItem customItem = plugin.getItemRegistry().getFromItemStack(item);
        if (customItem == null) {
            return;
        }

        // デバッグモード時のログ
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Player " + event.getWhoClicked().getName() + 
                " clicked custom item: " + customItem.getCustomId() + 
                " (action: " + event.getAction() + ")");
        }
    }
}