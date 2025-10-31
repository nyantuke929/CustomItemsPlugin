package com.github.ntn929.customitemapi;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * カスタムアイテムを表すクラス
 */
public class CustomItem {
    
    private final String id;
    private final Material baseMaterial;
    private String displayName;
    private List<String> lore;
    private int customModelData;
    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;
    private boolean unbreakable;
    private int amount;
    
    /**
     * カスタムアイテムを作成します
     * @param id アイテムID (例: "myplugin:chip")
     * @param baseMaterial ベースとなるMinecraftのマテリアル
     */
    public CustomItem(String id, Material baseMaterial) {
        this.id = id;
        this.baseMaterial = baseMaterial;
        this.displayName = id;
        this.lore = new ArrayList<>();
        this.customModelData = 0;
        this.enchantments = new HashMap<>();
        this.itemFlags = new ArrayList<>();
        this.unbreakable = false;
        this.amount = 1;
    }
    
    /**
     * アイテムIDを取得します
     * @return アイテムID
     */
    public String getId() {
        return id;
    }
    
    /**
     * ベースマテリアルを取得します
     * @return ベースマテリアル
     */
    public Material getBaseMaterial() {
        return baseMaterial;
    }
    
    /**
     * 表示名を設定します
     * @param displayName 表示名
     * @return このインスタンス
     */
    public CustomItem setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
    
    /**
     * 表示名を取得します
     * @return 表示名
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 説明文を設定します
     * @param lore 説明文のリスト
     * @return このインスタンス
     */
    public CustomItem setLore(List<String> lore) {
        this.lore = new ArrayList<>(lore);
        return this;
    }
    
    /**
     * 説明文を追加します
     * @param line 追加する行
     * @return このインスタンス
     */
    public CustomItem addLoreLine(String line) {
        this.lore.add(line);
        return this;
    }
    
    /**
     * 説明文を取得します
     * @return 説明文のリスト
     */
    public List<String> getLore() {
        return new ArrayList<>(lore);
    }
    
    /**
     * カスタムモデルデータを設定します（テクスチャ変更用）
     * @param customModelData カスタムモデルデータ
     * @return このインスタンス
     */
    public CustomItem setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }
    
    /**
     * カスタムモデルデータを取得します
     * @return カスタムモデルデータ
     */
    public int getCustomModelData() {
        return customModelData;
    }
    
    /**
     * エンチャントを追加します
     * @param enchantment エンチャント
     * @param level レベル
     * @return このインスタンス
     */
    public CustomItem addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }
    
    /**
     * エンチャントを取得します
     * @return エンチャントのマップ
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return new HashMap<>(enchantments);
    }
    
    /**
     * アイテムフラグを追加します
     * @param flag アイテムフラグ
     * @return このインスタンス
     */
    public CustomItem addItemFlag(ItemFlag flag) {
        this.itemFlags.add(flag);
        return this;
    }
    
    /**
     * アイテムフラグを取得します
     * @return アイテムフラグのリスト
     */
    public List<ItemFlag> getItemFlags() {
        return new ArrayList<>(itemFlags);
    }
    
    /**
     * 破壊不可能に設定します
     * @param unbreakable 破壊不可能かどうか
     * @return このインスタンス
     */
    public CustomItem setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }
    
    /**
     * 破壊不可能かどうかを取得します
     * @return 破壊不可能かどうか
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }
    
    /**
     * アイテムの数量を設定します
     * @param amount 数量
     * @return このインスタンス
     */
    public CustomItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    
    /**
     * アイテムの数量を取得します
     * @return 数量
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * ItemStackを生成します
     * @return 生成されたItemStack
     */
    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(baseMaterial, amount);
        ItemMeta meta = itemStack.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(displayName);
            
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }
            
            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }
            
            if (!itemFlags.isEmpty()) {
                meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
            }
            
            meta.setUnbreakable(unbreakable);
            
            // カスタムアイテムIDを保存
            meta.getPersistentDataContainer().set(
                CustomItemAPI.getCustomItemKey(),
                org.bukkit.persistence.PersistentDataType.STRING,
                id
            );
            
            itemStack.setItemMeta(meta);
            
            // エンチャントを追加
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                itemStack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
        }
        
        return itemStack;
    }
}