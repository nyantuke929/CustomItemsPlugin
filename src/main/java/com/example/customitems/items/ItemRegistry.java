package com.example.customitems.items;

import org.bukkit.Material;

import com.example.customitems.api.NMSCustomItemAPI;

import net.minecraft.world.item.Rarity;

public class ItemRegistry {
    
    public static void registerItems(NMSCustomItemAPI api) {
        // 炎の剣
        api.registerItem("flame_sword",
            new NMSCustomItemAPI.CustomItemBuilder(Material.DIAMOND_SWORD)
                .displayName("§c§l炎の剣")
                .lore(
                    "§7伝説の炎を宿した剣",
                    "§7敵を焼き尽くす力を持つ",
                    "",
                    "§e右クリックで特殊能力発動"
                )
                .rarity(Rarity.EPIC)
                .fireResistant(true)
                .maxStackSize(1)
        );
        
        // 氷の剣
        api.registerItem("ice_sword",
            new NMSCustomItemAPI.CustomItemBuilder(Material.DIAMOND_SWORD)
                .displayName("§b§l氷の剣")
                .lore(
                    "§7氷の力を秘めた剣",
                    "§7敵を凍らせる",
                    "",
                    "§e右クリックで特殊能力発動"
                )
                .rarity(Rarity.EPIC)
                .maxStackSize(1)
        );
        
        // 魔法の杖
        api.registerItem("magic_wand",
            new NMSCustomItemAPI.CustomItemBuilder(Material.STICK)
                .displayName("§d§l魔法の杖")
                .lore(
                    "§7古代の魔法使いが使用していた杖",
                    "§7様々な魔法を発動できる",
                    "",
                    "§bマナ: §f100/100"
                )
                .rarity(Rarity.RARE)
                .maxStackSize(1)
        );
        
        // 金貨
        api.registerItem("gold_coin",
            new NMSCustomItemAPI.CustomItemBuilder(Material.GOLD_NUGGET)
                .displayName("§6金貨")
                .lore("§7通貨として使用できる")
                .maxStackSize(99)
        );
        
        // 超回復ポーション
        api.registerItem("super_heal_potion",
            new NMSCustomItemAPI.CustomItemBuilder(Material.POTION)
                .displayName("§a§l超回復のポーション")
                .lore(
                    "§7飲むと体力が全回復する",
                    "§7さらに再生効果が付与される"
                )
                .rarity(Rarity.RARE)
                .maxStackSize(16)
        );
        
        // 魔法の書
        api.registerItem("magic_book",
            new NMSCustomItemAPI.CustomItemBuilder(Material.BOOK)
                .displayName("§5§l魔法の書")
                .lore(
                    "§7失われた魔法の知識が記されている",
                    "§7右クリックで読むことができる"
                )
                .rarity(Rarity.EPIC)
                .maxStackSize(1)
        );
    }
}