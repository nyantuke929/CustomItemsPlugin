package io.github.ntn929.paperitemapi.item;

import io.github.ntn929.paperitemapi.component.CustomDataComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

/**
 * CustomItemをビルダーパターンで作成するクラス
 * 
 * 使用例:
 * <pre>
 * CustomItem item = new CustomItemBuilder("myitem:custom_sword")
 *     .material(Material.DIAMOND_SWORD)
 *     .displayName(Component.text("カスタムソード").color(NamedTextColor.GOLD))
 *     .lore("強力な武器", "攻撃力: +100")
 *     .customModelData(1001)
 *     .addComponent("attack_power", 100)
 *     .unbreakable(true)
 *     .build();
 * </pre>
 * 
 * @author ntn929
 */
public class CustomItemBuilder {

    private final String customId;
    private Material baseMaterial;
    private Component displayName;
    private Integer customModelData;
    private boolean unbreakable;
    private Integer maxStackSize;
    private final CustomItem item;

    /**
     * CustomItemBuilderを作成（デフォルトマテリアル: STONE）
     * 
     * @param customId カスタムアイテムID（例: "myitem:custom_sword"）
     */
    public CustomItemBuilder(String customId) {
        this(customId, Material.STONE);
    }

    /**
     * CustomItemBuilderを作成
     * 
     * @param customId カスタムアイテムID
     * @param baseMaterial ベースマテリアル
     */
    public CustomItemBuilder(String customId, Material baseMaterial) {
        this.customId = customId;
        this.baseMaterial = baseMaterial;
        this.item = new CustomItem(customId, baseMaterial);
    }

    /**
     * ベースマテリアルを設定
     * 注意: CustomItemは作成後にマテリアルを変更できないため、
     * このメソッドは新しいCustomItemを内部で再作成します。
     * 
     * @param material マテリアル
     * @return このビルダー
     */
    public CustomItemBuilder material(Material material) {
        this.baseMaterial = material;
        return this;
    }

    /**
     * 表示名を設定
     * 
     * @param displayName 表示名（Componentオブジェクト）
     * @return このビルダー
     */
    public CustomItemBuilder displayName(Component displayName) {
        item.setDisplayName(displayName);
        return this;
    }

    /**
     * 表示名を設定（文字列）
     * 
     * @param displayName 表示名
     * @return このビルダー
     */
    public CustomItemBuilder displayName(String displayName) {
        item.setDisplayName(Component.text(displayName));
        return this;
    }

    /**
     * 表示名を設定（文字列とカラー）
     * 
     * @param displayName 表示名
     * @param color 色
     * @return このビルダー
     */
    public CustomItemBuilder displayName(String displayName, TextColor color) {
        item.setDisplayName(Component.text(displayName).color(color));
        return this;
    }

    /**
     * Loreを設定
     * 
     * @param lore Loreのリスト
     * @return このビルダー
     */
    public CustomItemBuilder lore(List<Component> lore) {
        item.setLore(lore);
        return this;
    }

    /**
     * Loreを設定（可変長引数）
     * 
     * @param lore Lore
     * @return このビルダー
     */
    public CustomItemBuilder lore(Component... lore) {
        item.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Loreを設定（文字列可変長引数）
     * 
     * @param lore Lore
     * @return このビルダー
     */
    public CustomItemBuilder lore(String... lore) {
        Component[] components = Arrays.stream(lore)
            .map(line -> Component.text(line)
                .decoration(TextDecoration.ITALIC, false))
            .toArray(Component[]::new);
        return lore(components);
    }

    /**
     * Lore行を追加
     * 
     * @param line 追加する行
     * @return このビルダー
     */
    public CustomItemBuilder addLore(Component line) {
        item.addLore(line);
        return this;
    }

    /**
     * Lore行を追加（文字列）
     * 
     * @param line 追加する行
     * @return このビルダー
     */
    public CustomItemBuilder addLore(String line) {
        item.addLore(Component.text(line)
            .decoration(TextDecoration.ITALIC, false));
        return this;
    }

    /**
     * Lore行を追加（文字列とカラー）
     * 
     * @param line 追加する行
     * @param color 色
     * @return このビルダー
     */
    public CustomItemBuilder addLore(String line, TextColor color) {
        item.addLore(Component.text(line)
            .color(color)
            .decoration(TextDecoration.ITALIC, false));
        return this;
    }

    /**
     * カスタムモデルデータを設定
     * 
     * @param customModelData カスタムモデルデータ
     * @return このビルダー
     */
    public CustomItemBuilder customModelData(int customModelData) {
        item.setCustomModelData(customModelData);
        return this;
    }

    /**
     * 耐久無限を設定
     * 
     * @param unbreakable 耐久無限にする場合true
     * @return このビルダー
     */
    public CustomItemBuilder unbreakable(boolean unbreakable) {
        item.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * 耐久無限を設定（デフォルトtrue）
     * 
     * @return このビルダー
     */
    public CustomItemBuilder unbreakable() {
        return unbreakable(true);
    }

    /**
     * 最大スタック数を設定
     * 
     * @param maxStackSize 最大スタック数（1-64）
     * @return このビルダー
     */
    public CustomItemBuilder maxStackSize(int maxStackSize) {
        item.setMaxStackSize(maxStackSize);
        return this;
    }

    /**
     * カスタムコンポーネントを追加（整数値）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, int value) {
        item.addComponent(key, CustomDataComponent.ofInt(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（文字列値）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, String value) {
        item.addComponent(key, CustomDataComponent.ofString(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（Double値）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, double value) {
        item.addComponent(key, CustomDataComponent.ofDouble(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（Float値）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, float value) {
        item.addComponent(key, CustomDataComponent.ofFloat(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（Boolean値）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, boolean value) {
        item.addComponent(key, CustomDataComponent.ofBoolean(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（Long値）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, long value) {
        item.addComponent(key, CustomDataComponent.ofLong(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（Byte値）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, byte value) {
        item.addComponent(key, CustomDataComponent.ofByte(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（バイト配列）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, byte[] value) {
        item.addComponent(key, CustomDataComponent.ofByteArray(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（整数配列）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, int[] value) {
        item.addComponent(key, CustomDataComponent.ofIntArray(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加（Long配列）
     * 
     * @param key コンポーネントキー
     * @param value 値
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, long[] value) {
        item.addComponent(key, CustomDataComponent.ofLongArray(value));
        return this;
    }

    /**
     * カスタムコンポーネントを追加
     * 
     * @param key コンポーネントキー
     * @param component コンポーネント
     * @return このビルダー
     */
    public CustomItemBuilder addComponent(String key, CustomDataComponent<?> component) {
        item.addComponent(key, component);
        return this;
    }

    /**
     * コンポーネントを削除
     * 
     * @param key コンポーネントキー
     * @return このビルダー
     */
    public CustomItemBuilder removeComponent(String key) {
        item.removeComponent(key);
        return this;
    }

    /**
     * CustomItemをビルド
     * 
     * @return 構築されたCustomItem
     */
    public CustomItem build() {
        return item;
    }

    /**
     * CustomItemをビルドしてItemStackとして取得
     * 
     * @return 構築されたItemStack
     */
    public org.bukkit.inventory.ItemStack buildItemStack() {
        return item.toItemStack();
    }

    /**
     * CustomItemをビルドしてItemStackとして取得（個数指定）
     * 
     * @param amount アイテムの個数
     * @return 構築されたItemStack
     */
    public org.bukkit.inventory.ItemStack buildItemStack(int amount) {
        return item.toItemStack(amount);
    }
}