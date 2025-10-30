package io.github.ntn929.paperitemapi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * NBT/PersistentDataContainer操作のユーティリティクラス
 * 
 * @author ntn929
 */
public class NBTUtils {

    private NBTUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * ItemStackからPersistentDataContainerを取得
     * 
     * @param item ItemStack
     * @return PersistentDataContainer（取得できない場合null）
     */
    public static PersistentDataContainer getContainer(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer();
    }

    /**
     * 整数値を設定
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @param value 値
     * @return 成功した場合true
     */
    public static boolean setInt(ItemStack item, NamespacedKey key, int value) {
        if (item == null || key == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * 整数値を取得
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @return 値（存在しない場合null）
     */
    public static Integer getInt(ItemStack item, NamespacedKey key) {
        PersistentDataContainer container = getContainer(item);
        if (container == null || key == null) {
            return null;
        }

        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER);
        }
        return null;
    }

    /**
     * 文字列を設定
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @param value 値
     * @return 成功した場合true
     */
    public static boolean setString(ItemStack item, NamespacedKey key, String value) {
        if (item == null || key == null || value == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * 文字列を取得
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @return 値（存在しない場合null）
     */
    public static String getString(ItemStack item, NamespacedKey key) {
        PersistentDataContainer container = getContainer(item);
        if (container == null || key == null) {
            return null;
        }

        if (container.has(key, PersistentDataType.STRING)) {
            return container.get(key, PersistentDataType.STRING);
        }
        return null;
    }

    /**
     * Double値を設定
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @param value 値
     * @return 成功した場合true
     */
    public static boolean setDouble(ItemStack item, NamespacedKey key, double value) {
        if (item == null || key == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * Double値を取得
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @return 値（存在しない場合null）
     */
    public static Double getDouble(ItemStack item, NamespacedKey key) {
        PersistentDataContainer container = getContainer(item);
        if (container == null || key == null) {
            return null;
        }

        if (container.has(key, PersistentDataType.DOUBLE)) {
            return container.get(key, PersistentDataType.DOUBLE);
        }
        return null;
    }

    /**
     * Boolean値を設定（内部的にByteで保存）
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @param value 値
     * @return 成功した場合true
     */
    public static boolean setBoolean(ItemStack item, NamespacedKey key, boolean value) {
        return setByte(item, key, value ? (byte) 1 : (byte) 0);
    }

    /**
     * Boolean値を取得
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @return 値（存在しない場合null）
     */
    public static Boolean getBoolean(ItemStack item, NamespacedKey key) {
        Byte value = getByte(item, key);
        return value != null ? value == 1 : null;
    }

    /**
     * Byte値を設定
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @param value 値
     * @return 成功した場合true
     */
    public static boolean setByte(ItemStack item, NamespacedKey key, byte value) {
        if (item == null || key == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, value);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * Byte値を取得
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @return 値（存在しない場合null）
     */
    public static Byte getByte(ItemStack item, NamespacedKey key) {
        PersistentDataContainer container = getContainer(item);
        if (container == null || key == null) {
            return null;
        }

        if (container.has(key, PersistentDataType.BYTE)) {
            return container.get(key, PersistentDataType.BYTE);
        }
        return null;
    }

    /**
     * キーが存在するかチェック
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @param type PersistentDataType
     * @return 存在する場合true
     */
    public static boolean has(ItemStack item, NamespacedKey key, PersistentDataType<?, ?> type) {
        PersistentDataContainer container = getContainer(item);
        if (container == null || key == null || type == null) {
            return false;
        }
        return container.has(key, type);
    }

    /**
     * キーを削除
     * 
     * @param item ItemStack
     * @param key NamespacedKey
     * @return 成功した場合true
     */
    public static boolean remove(ItemStack item, NamespacedKey key) {
        if (item == null || key == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * すべてのキーを取得
     * 
     * @param item ItemStack
     * @return キーのセット
     */
    public static Set<NamespacedKey> getKeys(ItemStack item) {
        PersistentDataContainer container = getContainer(item);
        if (container == null) {
            return Set.of();
        }
        return container.getKeys();
    }

    /**
     * すべてのデータを取得（デバッグ用）
     * 
     * @param item ItemStack
     * @return キーと値のマップ
     */
    public static Map<String, Object> getAllData(ItemStack item) {
        Map<String, Object> data = new HashMap<>();
        PersistentDataContainer container = getContainer(item);
        
        if (container == null) {
            return data;
        }

        for (NamespacedKey key : container.getKeys()) {
            // 各データ型を試して取得
            if (container.has(key, PersistentDataType.STRING)) {
                data.put(key.toString(), container.get(key, PersistentDataType.STRING));
            } else if (container.has(key, PersistentDataType.INTEGER)) {
                data.put(key.toString(), container.get(key, PersistentDataType.INTEGER));
            } else if (container.has(key, PersistentDataType.DOUBLE)) {
                data.put(key.toString(), container.get(key, PersistentDataType.DOUBLE));
            } else if (container.has(key, PersistentDataType.BYTE)) {
                data.put(key.toString(), container.get(key, PersistentDataType.BYTE));
            } else if (container.has(key, PersistentDataType.LONG)) {
                data.put(key.toString(), container.get(key, PersistentDataType.LONG));
            } else if (container.has(key, PersistentDataType.FLOAT)) {
                data.put(key.toString(), container.get(key, PersistentDataType.FLOAT));
            }
        }

        return data;
    }

    /**
     * PersistentDataContainerの内容をコピー
     * 
     * @param from コピー元
     * @param to コピー先
     */
    public static void copyContainer(ItemStack from, ItemStack to) {
        if (from == null || to == null) {
            return;
        }

        ItemMeta fromMeta = from.getItemMeta();
        ItemMeta toMeta = to.getItemMeta();
        
        if (fromMeta == null || toMeta == null) {
            return;
        }

        PersistentDataContainer fromContainer = fromMeta.getPersistentDataContainer();
        PersistentDataContainer toContainer = toMeta.getPersistentDataContainer();

        for (NamespacedKey key : fromContainer.getKeys()) {
            // 各型をコピー
            if (fromContainer.has(key, PersistentDataType.STRING)) {
                toContainer.set(key, PersistentDataType.STRING, 
                    fromContainer.get(key, PersistentDataType.STRING));
            } else if (fromContainer.has(key, PersistentDataType.INTEGER)) {
                toContainer.set(key, PersistentDataType.INTEGER, 
                    fromContainer.get(key, PersistentDataType.INTEGER));
            } else if (fromContainer.has(key, PersistentDataType.DOUBLE)) {
                toContainer.set(key, PersistentDataType.DOUBLE, 
                    fromContainer.get(key, PersistentDataType.DOUBLE));
            }
            // 他の型も同様に...
        }

        to.setItemMeta(toMeta);
    }
}