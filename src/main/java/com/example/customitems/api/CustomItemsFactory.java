package com.example.customitems.api;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * カスタムアイテムAPIのファクトリークラス (1.21.8専用)
 */
public class CustomItemsFactory {
    
    /**
     * 1.21.8用APIインスタンスを生成
     * @param plugin プラグインインスタンス
     * @return VersionedCustomItemAPI
     */
    public static VersionedCustomItemAPI createAPI(JavaPlugin plugin) {
        return VersionedCustomItemAPI.create(plugin);
    }
    
    /**
     * 1.21.8用アイテムビルダーを生成
     * @param baseMaterial ベースとなるマテリアル
     * @return ItemBuilder
     */
    public static VersionedCustomItemAPI.ItemBuilder createBuilder(Material baseMaterial) {
        return new NMS_1_21_8.CustomItemBuilderImpl(baseMaterial);
    }
    
    /**
     * 簡易的なヘルパーメソッド: アイテム作成と登録を一度に行う
     * @param api APIインスタンス
     * @param id アイテムID
     * @param material ベースマテリアル
     * @param displayName 表示名
     * @param lore 説明文
     */
    public static void quickRegister(VersionedCustomItemAPI api, String id, 
                                     Material material, String displayName, String... lore) {
        VersionedCustomItemAPI.ItemBuilder builder = createBuilder(material)
            .displayName(displayName)
            .lore(lore);
        api.registerItem(id, builder);
    }
}