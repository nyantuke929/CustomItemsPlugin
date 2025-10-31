package com.github.ntn929.customitemapi;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.List;

/**
 * カスタムアイテムを簡単に作成するためのビルダークラス
 */
public class CustomItemBuilder {
    
    private final CustomItem item;
    
    /**
     * ビルダーを作成します
     * @param id アイテムID (例: "myplugin:chip")
     * @param baseMaterial ベースマテリアル
     */
    public CustomItemBuilder(String id, Material baseMaterial) {
        this.item = new CustomItem(id, baseMaterial);
    }
    
    /**
     * 既存のCustomItemからビルダーを作成します
     * @param item カスタムアイテム
     */
    public CustomItemBuilder(CustomItem item) {
        this.item = item;
    }
    
    /**
     * 表示名を設定します
     * @param displayName 表示名
     * @return このビルダー
     */
    public CustomItemBuilder displayName(String displayName) {
        item.setDisplayName(displayName);
        return this;
    }
    
    /**
     * 説明文を設定します
     * @param lore 説明文の行（可変長引数）
     * @return このビルダー
     */
    public CustomItemBuilder lore(String... lore) {
        item.setLore(Arrays.asList(lore));
        return this;
    }
    
    /**
     * 説明文を設定します
     * @param lore 説明文のリスト
     * @return このビルダー
     */
    public CustomItemBuilder lore(List<String> lore) {
        item.setLore(lore);
        return this;
    }
    
    /**
     * 説明文の行を追加します
     * @param line 追加する行
     * @return このビルダー
     */
    public CustomItemBuilder addLoreLine(String line) {
        item.addLoreLine(line);
        return this;
    }
    
    /**
     * カスタムモデルデータを設定します
     * @param customModelData カスタムモデルデータ
     * @return このビルダー
     */
    public CustomItemBuilder customModelData(int customModelData) {
        item.setCustomModelData(customModelData);
        return this;
    }
    
    /**
     * エンチャントを追加します
     * @param enchantment エンチャント
     * @param level レベル
     * @return このビルダー
     */
    public CustomItemBuilder enchant(Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
        return this;
    }
    
    /**
     * アイテムフラグを追加します
     * @param flags アイテムフラグ（可変長引数）
     * @return このビルダー
     */
    public CustomItemBuilder flags(ItemFlag... flags) {
        for (ItemFlag flag : flags) {
            item.addItemFlag(flag);
        }
        return this;
    }
    
    /**
     * 破壊不可能に設定します
     * @return このビルダー
     */
    public CustomItemBuilder unbreakable() {
        item.setUnbreakable(true);
        return this;
    }
    
    /**
     * 破壊不可能設定を変更します
     * @param unbreakable 破壊不可能かどうか
     * @return このビルダー
     */
    public CustomItemBuilder unbreakable(boolean unbreakable) {
        item.setUnbreakable(unbreakable);
        return this;
    }
    
    /**
     * アイテムの数量を設定します
     * @param amount 数量
     * @return このビルダー
     */
    public CustomItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    /**
     * カスタムアイテムを構築します
     * @return 構築されたカスタムアイテム
     */
    public CustomItem build() {
        return item;
    }
    
    /**
     * カスタムアイテムを構築して登録します
     * @return 構築されたカスタムアイテム
     */
    public CustomItem buildAndRegister() {
        CustomItemAPI.registerItem(item);
        return item;
    }
}