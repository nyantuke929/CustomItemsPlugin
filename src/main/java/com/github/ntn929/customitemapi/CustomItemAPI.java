package com.github.ntn929.customitemapi;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * カスタムアイテムAPIのメインクラス
 */
public class CustomItemAPI {
    
    private static final Map<String, CustomItem> registeredItems = new HashMap<>();
    private static JavaPlugin plugin;
    private static NamespacedKey customItemKey;
    
    /**
     * APIを初期化します
     * @param plugin プラグインインスタンス
     */
    public static void initialize(JavaPlugin plugin) {
        CustomItemAPI.plugin = plugin;
        customItemKey = new NamespacedKey(plugin, "custom_item_id");
        plugin.getLogger().info("CustomItemAPI initialized!");
    }
    
    /**
     * カスタムアイテムキーを取得します
     * @return NamespacedKey
     */
    public static NamespacedKey getCustomItemKey() {
        return customItemKey;
    }
    
    /**
     * プラグインインスタンスを取得します
     * @return JavaPlugin
     */
    public static JavaPlugin getPlugin() {
        return plugin;
    }
    
    /**
     * カスタムアイテムを登録します
     * @param item 登録するカスタムアイテム
     */
    public static void registerItem(CustomItem item) {
        registeredItems.put(item.getId(), item);
        plugin.getLogger().info("Registered custom item: " + item.getId());
    }
    
    /**
     * IDからカスタムアイテムを取得します
     * @param id アイテムID (例: "myplugin:chip")
     * @return カスタムアイテム、存在しない場合はnull
     */
    public static CustomItem getCustomItem(String id) {
        return registeredItems.get(id);
    }
    
    /**
     * ItemStackからカスタムアイテムを取得します
     * @param itemStack ItemStack
     * @return カスタムアイテム、カスタムアイテムでない場合はnull
     */
    public static CustomItem getCustomItem(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }
        
        ItemMeta meta = itemStack.getItemMeta();
        String id = meta.getPersistentDataContainer().get(customItemKey, PersistentDataType.STRING);
        
        if (id == null) {
            return null;
        }
        
        return registeredItems.get(id);
    }
    
    /**
     * ItemStackがカスタムアイテムかどうかを判定します
     * @param itemStack ItemStack
     * @return カスタムアイテムの場合true
     */
    public static boolean isCustomItem(ItemStack itemStack) {
        return getCustomItem(itemStack) != null;
    }
    
    /**
     * ItemStackが指定されたIDのカスタムアイテムかどうかを判定します
     * @param itemStack ItemStack
     * @param id アイテムID
     * @return 指定されたIDのカスタムアイテムの場合true
     */
    public static boolean isCustomItem(ItemStack itemStack, String id) {
        CustomItem item = getCustomItem(itemStack);
        return item != null && item.getId().equals(id);
    }
    
    /**
     * 登録されているすべてのカスタムアイテムIDを取得します
     * @return アイテムIDのセット
     */
    public static Set<String> getRegisteredItemIds() {
        return new HashSet<>(registeredItems.keySet());
    }
    
    /**
     * 登録されているすべてのカスタムアイテムを取得します
     * @return カスタムアイテムのコレクション
     */
    public static Collection<CustomItem> getRegisteredItems() {
        return new ArrayList<>(registeredItems.values());
    }
    
    /**
     * カスタムアイテムの登録を解除します
     * @param id アイテムID
     * @return 解除に成功した場合true
     */
    public static boolean unregisterItem(String id) {
        if (registeredItems.containsKey(id)) {
            registeredItems.remove(id);
            plugin.getLogger().info("Unregistered custom item: " + id);
            return true;
        }
        return false;
    }
    
    /**
     * すべてのカスタムアイテムの登録を解除します
     */
    public static void unregisterAllItems() {
        registeredItems.clear();
        plugin.getLogger().info("Unregistered all custom items");
    }
}