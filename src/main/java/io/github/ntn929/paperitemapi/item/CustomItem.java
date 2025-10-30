package io.github.ntn929.paperitemapi.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import io.github.ntn929.paperitemapi.component.CustomDataComponent;
import net.kyori.adventure.text.Component;

/**
 * カスタムアイテムを表すクラス
 * F3+Hで表示される minecraft:diamond のようなカスタムアイテムを作成
 * 
 * Minecraft 1.21.xのData Componentsシステムに対応し、
 * PersistentDataContainerを使用してカスタムデータを管理します。
 * 
 * 使用例:
 * <pre>
 * CustomItem item = new CustomItem("myitem:custom_sword", Material.DIAMOND_SWORD);
 * item.setDisplayName(Component.text("カスタムソード"));
 * item.addComponent("attack_power", CustomDataComponent.ofInt(100));
 * ItemStack itemStack = item.toItemStack();
 * </pre>
 * 
 * @author ntn929
 */
public class CustomItem {

    private final String customId; // 例: "myitem:custom_sword"
    private final Material baseMaterial;
    private Component displayName;
    private List<Component> lore;
    private Integer customModelData;
    private final Map<String, CustomDataComponent<?>> components;
    private boolean unbreakable;
    private Integer maxStackSize;

    /**
     * カスタムアイテムを作成
     * 
     * @param customId カスタムアイテムID（namespace:key形式を推奨）
     * @param baseMaterial ベースとなるマテリアル
     * @throws IllegalArgumentException customIdまたはbaseMaterialがnullの場合
     */
    public CustomItem(String customId, Material baseMaterial) {
        if (customId == null || customId.isEmpty()) {
            throw new IllegalArgumentException("CustomId cannot be null or empty");
        }
        if (baseMaterial == null) {
            throw new IllegalArgumentException("BaseMaterial cannot be null");
        }
        
        this.customId = customId;
        this.baseMaterial = baseMaterial;
        this.lore = new ArrayList<>();
        this.components = new HashMap<>();
        this.unbreakable = false;
        this.maxStackSize = null;
    }

    /**
     * ItemStackに変換（個数1）
     * 
     * @return 生成されたItemStack
     */
    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    /**
     * ItemStackに変換（個数指定）
     * 
     * @param amount アイテムの個数（1-64）
     * @return 生成されたItemStack
     * @throws IllegalArgumentException amountが無効な場合
     */
    public ItemStack toItemStack(int amount) {
        if (amount < 1 || amount > 64) {
            throw new IllegalArgumentException("Amount must be between 1 and 64");
        }
        
        ItemStack item = new ItemStack(baseMaterial, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta == null) {
            return item;
        }

        // カスタムIDをPersistentDataContainerに保存
        NamespacedKey key = parseNamespacedKey(customId);
        meta.getPersistentDataContainer().set(
            key,
            PersistentDataType.STRING,
            customId
        );

        // 表示名の設定
        if (displayName != null) {
            meta.displayName(displayName);
        }

        // Loreの設定
        if (!lore.isEmpty()) {
            meta.lore(lore);
        }

        // カスタムモデルデータの設定
        if (customModelData != null) {
            meta.setCustomModelData(customModelData);
        }

        // 耐久無限の設定
        if (unbreakable) {
            meta.setUnbreakable(true);
        }

        // カスタムコンポーネントの適用
        applyComponents(meta);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * カスタムコンポーネントをItemMetaに適用
     * 
     * @param meta 適用先のItemMeta
     */
    private void applyComponents(ItemMeta meta) {
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        for (Map.Entry<String, CustomDataComponent<?>> entry : components.entrySet()) {
            String componentKey = entry.getKey();
            CustomDataComponent<?> component = entry.getValue();
            
            // コンポーネントキーをNamespacedKeyに変換
            NamespacedKey key = parseNamespacedKey(customId + ":" + componentKey);
            component.apply(container, key);
        }
    }

    /**
     * 文字列からNamespacedKeyを生成
     * 
     * @param id namespace:key形式の文字列
     * @return NamespacedKey
     */
    private NamespacedKey parseNamespacedKey(String id) {
        String[] parts = id.split(":", 2);
        if (parts.length == 2) {
            return new NamespacedKey(parts[0], parts[1]);
        }
        // namespaceがない場合はminecraftとして扱う
        return NamespacedKey.minecraft(id);
    }

    /**
     * ItemStackがこのカスタムアイテムかチェック
     * 
     * @param item チェックするItemStack
     * @return このカスタムアイテムの場合true
     */
    public boolean isCustomItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = parseNamespacedKey(customId);
        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.has(key, PersistentDataType.STRING) &&
               customId.equals(container.get(key, PersistentDataType.STRING));
    }

    /**
     * ItemStackからコンポーネントの値を取得
     * 
     * @param item ItemStack
     * @param componentKey コンポーネントキー
     * @return コンポーネントの値（存在しない場合null）
     */
    public Object getComponentValue(ItemStack item, String componentKey) {
        if (!isCustomItem(item)) {
            return null;
        }

        CustomDataComponent<?> component = components.get(componentKey);
        if (component == null) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        NamespacedKey key = parseNamespacedKey(customId + ":" + componentKey);
        return component.get(meta.getPersistentDataContainer(), key);
    }

    // Getters and Setters

    /**
     * カスタムアイテムIDを取得
     * 
     * @return カスタムアイテムID
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * ベースマテリアルを取得
     * 
     * @return Material
     */
    public Material getBaseMaterial() {
        return baseMaterial;
    }

    /**
     * 表示名を取得
     * 
     * @return 表示名（未設定の場合null）
     */
    public Component getDisplayName() {
        return displayName;
    }

    /**
     * 表示名を設定
     * 
     * @param displayName 表示名
     */
    public void setDisplayName(Component displayName) {
        this.displayName = displayName;
    }

    /**
     * Loreを取得（コピー）
     * 
     * @return Loreのリスト
     */
    public List<Component> getLore() {
        return new ArrayList<>(lore);
    }

    /**
     * Loreを設定
     * 
     * @param lore Loreのリスト
     */
    public void setLore(List<Component> lore) {
        this.lore = new ArrayList<>(lore);
    }

    /**
     * Lore行を追加
     * 
     * @param line 追加する行
     */
    public void addLore(Component line) {
        this.lore.add(line);
    }

    /**
     * カスタムモデルデータを取得
     * 
     * @return カスタムモデルデータ（未設定の場合null）
     */
    public Integer getCustomModelData() {
        return customModelData;
    }

    /**
     * カスタムモデルデータを設定
     * 
     * @param customModelData カスタムモデルデータ
     */
    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    /**
     * 耐久無限かどうかを取得
     * 
     * @return 耐久無限の場合true
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * 耐久無限を設定
     * 
     * @param unbreakable 耐久無限にする場合true
     */
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    /**
     * 最大スタック数を取得
     * 
     * @return 最大スタック数（未設定の場合null）
     */
    public Integer getMaxStackSize() {
        return maxStackSize;
    }

    /**
     * 最大スタック数を設定
     * 
     * @param maxStackSize 最大スタック数（1-64）
     */
    public void setMaxStackSize(Integer maxStackSize) {
        if (maxStackSize != null && (maxStackSize < 1 || maxStackSize > 64)) {
            throw new IllegalArgumentException("MaxStackSize must be between 1 and 64");
        }
        this.maxStackSize = maxStackSize;
    }

    /**
     * すべてのコンポーネントを取得（コピー）
     * 
     * @return コンポーネントのマップ
     */
    public Map<String, CustomDataComponent<?>> getComponents() {
        return new HashMap<>(components);
    }

    /**
     * コンポーネントを追加
     * 
     * @param key コンポーネントキー
     * @param component コンポーネント
     */
    public void addComponent(String key, CustomDataComponent<?> component) {
        this.components.put(key, component);
    }

    /**
     * コンポーネントを取得
     * 
     * @param key コンポーネントキー
     * @return コンポーネント（存在しない場合null）
     */
    public CustomDataComponent<?> getComponent(String key) {
        return components.get(key);
    }

    /**
     * コンポーネントを削除
     * 
     * @param key コンポーネントキー
     */
    public void removeComponent(String key) {
        this.components.remove(key);
    }

    /**
     * コンポーネントが存在するかチェック
     * 
     * @param key コンポーネントキー
     * @return 存在する場合true
     */
    public boolean hasComponent(String key) {
        return components.containsKey(key);
    }

    @Override
    public String toString() {
        return "CustomItem{" +
                "customId='" + customId + '\'' +
                ", baseMaterial=" + baseMaterial +
                ", displayName=" + displayName +
                ", componentsCount=" + components.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomItem that = (CustomItem) o;
        return Objects.equals(customId, that.customId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customId);
    }
}