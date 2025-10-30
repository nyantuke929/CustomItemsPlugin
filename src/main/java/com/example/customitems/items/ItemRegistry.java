package com.example.customitems.items;

import org.bukkit.Material;

import com.example.customitems.api.CustomItemsFactory;
import com.example.customitems.api.VersionedCustomItemAPI;
import com.example.customitems.api.VersionedCustomItemAPI.ItemRarity;

/**
 * カスタムアイテムの登録を管理するクラス
 */
public class ItemRegistry {
    
    private final VersionedCustomItemAPI api;
    
    public ItemRegistry(VersionedCustomItemAPI api) {
        this.api = api;
    }
    
    /**
     * デフォルトのカスタムアイテムを登録
     */
    public void registerDefaultItems() {
        // 炎の剣
        api.registerItem("flame_sword",
            CustomItemsFactory.createBuilder(Material.DIAMOND_SWORD)
                .displayName("§c§l炎の剣")
                .lore(
                    "§7伝説の炎を宿した剣",
                    "§e右クリックで火の玉を発射"
                )
                .maxStackSize(1)
                .fireResistant(true)
                .rarity(ItemRarity.EPIC)
        );
        
        // 雷のハンマー
        api.registerItem("thunder_hammer",
            CustomItemsFactory.createBuilder(Material.IRON_AXE)
                .displayName("§b§l雷のハンマー")
                .lore(
                    "§7雷神の力を持つハンマー",
                    "§e右クリックで雷を召喚"
                )
                .maxStackSize(1)
                .fireResistant(true)
                .rarity(ItemRarity.EPIC)
        );
        
        // 癒しのポーション
        api.registerItem("healing_potion",
            CustomItemsFactory.createBuilder(Material.POTION)
                .displayName("§a§l癒しのポーション")
                .lore(
                    "§7強力な回復効果を持つポーション",
                    "§e右クリックで体力全回復"
                )
                .maxStackSize(16)
                .rarity(ItemRarity.RARE)
        );
        
        // 魔法の杖
        api.registerItem("magic_wand",
            CustomItemsFactory.createBuilder(Material.STICK)
                .displayName("§5§l魔法の杖")
                .lore(
                    "§7様々な魔法を使える杖",
                    "§e右クリックで魔法を発動"
                )
                .maxStackSize(1)
                .rarity(ItemRarity.UNCOMMON)
        );
        
        // 幸運のコイン
        api.registerItem("lucky_coin",
            CustomItemsFactory.createBuilder(Material.GOLD_NUGGET)
                .displayName("§6§l幸運のコイン")
                .lore(
                    "§7持っているだけで運が上がる",
                    "§eレアドロップ率アップ"
                )
                .maxStackSize(64)
                .rarity(ItemRarity.RARE)
        );
        
        // テレポートストーン
        api.registerItem("teleport_stone",
            CustomItemsFactory.createBuilder(Material.ENDER_PEARL)
                .displayName("§d§lテレポートストーン")
                .lore(
                    "§7任意の場所にテレポート",
                    "§e右クリックで座標設定",
                    "§eシフト+右クリックでテレポート"
                )
                .maxStackSize(1)
                .fireResistant(true)
                .rarity(ItemRarity.EPIC)
        );
    }
    
    /**
     * カスタムアイテムを動的に登録
     * @param id アイテムID
     * @param baseMaterial ベースマテリアル
     * @param displayName 表示名
     * @param lore 説明文
     */
    public void registerCustomItem(String id, Material baseMaterial, 
                                   String displayName, String... lore) {
        api.registerItem(id,
            CustomItemsFactory.createBuilder(baseMaterial)
                .displayName(displayName)
                .lore(lore)
        );
    }
    
    /**
     * APIインスタンスを取得
     * @return VersionedCustomItemAPI
     */
    public VersionedCustomItemAPI getAPI() {
        return api;
    }
}