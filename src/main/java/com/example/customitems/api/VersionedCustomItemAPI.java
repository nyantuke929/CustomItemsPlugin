package com.example.customitems.api;

import java.util.Set;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * カスタムアイテムAPI (1.21.8専用)
 */
public interface VersionedCustomItemAPI {
    
    /**
     * カスタムアイテムを登録
     * @param id アイテムID
     * @param builder アイテムビルダー
     */
    void registerItem(String id, ItemBuilder builder);
    
    /**
     * カスタムアイテムを取得
     * @param id アイテムID
     * @return ItemStack
     */
    ItemStack getCustomItem(String id);
    
    /**
     * カスタムアイテムを指定数量で取得
     * @param id アイテムID
     * @param amount 数量
     * @return ItemStack
     */
    ItemStack getCustomItem(String id, int amount);
    
    /**
     * アイテムがカスタムアイテムかどうかを判定
     * @param item アイテム
     * @return カスタムアイテムならtrue
     */
    boolean isCustomItem(ItemStack item);
    
    /**
     * カスタムアイテムのIDを取得
     * @param item アイテム
     * @return アイテムID
     */
    String getCustomItemId(ItemStack item);
    
    /**
     * 登録されているアイテムIDの一覧
     * @return アイテムID一覧
     */
    Set<String> getRegisteredItemIds();
    
    /**
     * このAPIがサポートしているMinecraftバージョン
     * @return バージョン文字列（例: "1.21.8"）
     */
    String getSupportedVersion();
    
    /**
     * アイテムビルダーインターフェース
     */
    interface ItemBuilder {
        ItemBuilder displayName(String name);
        ItemBuilder lore(String... lines);
        ItemBuilder maxStackSize(int size);
        ItemBuilder fireResistant(boolean resistant);
        ItemBuilder rarity(ItemRarity rarity);
    }
    
    /**
     * レアリティ列挙型
     */
    enum ItemRarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC
    }
    
    /**
     * ファクトリーメソッド: 1.21.8専用実装を返す
     * @param plugin プラグイン
     * @return API実装
     */
    static VersionedCustomItemAPI create(JavaPlugin plugin) {
        String version = getMinecraftVersion();
        plugin.getLogger().info("Detected Minecraft version: " + version);
        
        if (!version.startsWith("1.21.8")) {
            throw new UnsupportedOperationException(
                "This plugin only supports Minecraft 1.21.8. Current version: " + version);
        }
        
        return new NMS_1_21_8(plugin);
    }
    
    /**
     * Minecraftバージョンを取得
     */
    static String getMinecraftVersion() {
        String bukkitVersion = org.bukkit.Bukkit.getVersion();
        // "git-Paper-XXX (MC: 1.21.8)" のような形式から抽出
        if (bukkitVersion.contains("MC: ")) {
            int start = bukkitVersion.indexOf("MC: ") + 4;
            int end = bukkitVersion.indexOf(")", start);
            if (end > start) {
                return bukkitVersion.substring(start, end);
            }
        }
        
        // フォールバック: Bukkitバージョンから推測
        String[] parts = org.bukkit.Bukkit.getBukkitVersion().split("-");
        if (parts.length > 0) {
            return parts[0];
        }
        
        return "unknown";
    }
}