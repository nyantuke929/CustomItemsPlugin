package io.github.ntn929.paperitemapi.util;

import io.github.ntn929.paperitemapi.item.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * アイテム関連のユーティリティクラス
 * 
 * @author ntn929
 */
public class ItemUtils {

    private ItemUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * ItemStackが空かチェック
     * 
     * @param item ItemStack
     * @return 空の場合true
     */
    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR || item.getAmount() <= 0;
    }

    /**
     * 2つのItemStackが同じアイテムかチェック（個数を除く）
     * 
     * @param item1 ItemStack1
     * @param item2 ItemStack2
     * @return 同じアイテムの場合true
     */
    public static boolean isSimilar(ItemStack item1, ItemStack item2) {
        if (isEmpty(item1) && isEmpty(item2)) {
            return true;
        }
        if (isEmpty(item1) || isEmpty(item2)) {
            return false;
        }
        return item1.isSimilar(item2);
    }

    /**
     * ItemStackをクローン
     * 
     * @param item ItemStack
     * @return クローンされたItemStack
     */
    public static ItemStack clone(ItemStack item) {
        if (isEmpty(item)) {
            return null;
        }
        return item.clone();
    }

    /**
     * 表示名を取得（プレーンテキスト）
     * 
     * @param item ItemStack
     * @return 表示名（設定されていない場合はマテリアル名）
     */
    public static String getDisplayName(ItemStack item) {
        if (isEmpty(item) || !item.hasItemMeta()) {
            return item != null ? item.getType().name() : "AIR";
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            Component displayName = meta.displayName();
            if (displayName != null) {
                return PlainTextComponentSerializer.plainText().serialize(displayName);
            }
        }

        return item.getType().name();
    }

    /**
     * Loreを取得（プレーンテキスト）
     * 
     * @param item ItemStack
     * @return Loreのリスト
     */
    public static List<String> getLore(ItemStack item) {
        if (isEmpty(item) || !item.hasItemMeta()) {
            return List.of();
        }

        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        
        if (lore == null || lore.isEmpty()) {
            return List.of();
        }

        return lore.stream()
            .map(component -> PlainTextComponentSerializer.plainText().serialize(component))
            .collect(Collectors.toList());
    }

    /**
     * カスタムモデルデータを取得
     * 
     * @param item ItemStack
     * @return カスタムモデルデータ（設定されていない場合null）
     */
    public static Integer getCustomModelData(ItemStack item) {
        if (isEmpty(item) || !item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() ? meta.getCustomModelData() : null;
    }

    /**
     * 耐久無限かチェック
     * 
     * @param item ItemStack
     * @return 耐久無限の場合true
     */
    public static boolean isUnbreakable(ItemStack item) {
        if (isEmpty(item) || !item.hasItemMeta()) {
            return false;
        }

        return item.getItemMeta().isUnbreakable();
    }

    /**
     * ItemStackのサイズを調整
     * 
     * @param item ItemStack
     * @param amount 新しい個数
     * @return 個数が調整されたItemStack
     */
    public static ItemStack withAmount(ItemStack item, int amount) {
        if (isEmpty(item)) {
            return null;
        }

        ItemStack newItem = item.clone();
        newItem.setAmount(Math.max(1, Math.min(amount, item.getMaxStackSize())));
        return newItem;
    }

    /**
     * アイテムの詳細情報を文字列として取得
     * 
     * @param item ItemStack
     * @return 詳細情報
     */
    public static String getItemInfo(ItemStack item) {
        if (isEmpty(item)) {
            return "Empty";
        }

        StringBuilder info = new StringBuilder();
        info.append("Material: ").append(item.getType()).append("\n");
        info.append("Amount: ").append(item.getAmount()).append("\n");
        info.append("DisplayName: ").append(getDisplayName(item)).append("\n");
        
        List<String> lore = getLore(item);
        if (!lore.isEmpty()) {
            info.append("Lore:\n");
            lore.forEach(line -> info.append("  - ").append(line).append("\n"));
        }

        Integer customModelData = getCustomModelData(item);
        if (customModelData != null) {
            info.append("CustomModelData: ").append(customModelData).append("\n");
        }

        info.append("Unbreakable: ").append(isUnbreakable(item));

        return info.toString();
    }

    /**
     * CustomItemの情報を文字列として取得
     * 
     * @param customItem CustomItem
     * @return 情報文字列
     */
    public static String getCustomItemInfo(CustomItem customItem) {
        if (customItem == null) {
            return "null";
        }

        StringBuilder info = new StringBuilder();
        info.append("CustomID: ").append(customItem.getCustomId()).append("\n");
        info.append("Material: ").append(customItem.getBaseMaterial()).append("\n");
        
        Component displayName = customItem.getDisplayName();
        if (displayName != null) {
            info.append("DisplayName: ")
                .append(PlainTextComponentSerializer.plainText().serialize(displayName))
                .append("\n");
        }

        List<Component> lore = customItem.getLore();
        if (!lore.isEmpty()) {
            info.append("Lore:\n");
            lore.forEach(line -> info.append("  - ")
                .append(PlainTextComponentSerializer.plainText().serialize(line))
                .append("\n"));
        }

        Integer cmd = customItem.getCustomModelData();
        if (cmd != null) {
            info.append("CustomModelData: ").append(cmd).append("\n");
        }

        info.append("Unbreakable: ").append(customItem.isUnbreakable()).append("\n");
        info.append("Components: ").append(customItem.getComponents().size());

        return info.toString();
    }
}