package io.github.ntn929.paperitemapi.api;

import io.github.ntn929.paperitemapi.PaperItemAPI;
import io.github.ntn929.paperitemapi.item.CustomItem;
import io.github.ntn929.paperitemapi.item.CustomItemBuilder;
import io.github.ntn929.paperitemapi.item.CustomItemRegistry;
import org.bukkit.inventory.ItemStack;

/**
 * PaperItemAPIへのアクセスを提供する静的APIプロバイダー
 * 他のプラグインから使用する際の簡易的なアクセスポイント
 * 
 * 使用例:
 * <pre>
 * CustomItem item = PaperItemAPIProvider.getRegistry().get("myitem:sword");
 * PaperItemAPIProvider.registerItem(item);
 * </pre>
 * 
 * @author ntn929
 */
public final class PaperItemAPIProvider {

    private PaperItemAPIProvider() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * PaperItemAPIインスタンスを取得
     * 
     * @return PaperItemAPIインスタンス
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static PaperItemAPI getAPI() {
        PaperItemAPI instance = PaperItemAPI.getInstance();
        if (instance == null) {
            throw new IllegalStateException("PaperItemAPI is not enabled");
        }
        return instance;
    }

    /**
     * カスタムアイテムレジストリを取得
     * 
     * @return CustomItemRegistry
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static CustomItemRegistry getRegistry() {
        return getAPI().getItemRegistry();
    }

    /**
     * カスタムアイテムを登録
     * 
     * @param item 登録するCustomItem
     * @return 登録に成功した場合true
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static boolean registerItem(CustomItem item) {
        return getRegistry().register(item);
    }

    /**
     * カスタムアイテムを登録解除
     * 
     * @param customId カスタムアイテムID
     * @return 登録解除に成功した場合true
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static boolean unregisterItem(String customId) {
        return getRegistry().unregister(customId);
    }

    /**
     * カスタムアイテムを取得
     * 
     * @param customId カスタムアイテムID
     * @return CustomItem（存在しない場合null）
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static CustomItem getItem(String customId) {
        return getRegistry().get(customId);
    }

    /**
     * ItemStackからカスタムアイテムを取得
     * 
     * @param itemStack ItemStack
     * @return CustomItem（存在しない場合null）
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static CustomItem getItemFromStack(ItemStack itemStack) {
        return getRegistry().getFromItemStack(itemStack);
    }

    /**
     * ItemStackがカスタムアイテムかチェック
     * 
     * @param itemStack ItemStack
     * @return カスタムアイテムの場合true
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static boolean isCustomItem(ItemStack itemStack) {
        return getRegistry().isCustomItem(itemStack);
    }

    /**
     * カスタムアイテムが登録されているかチェック
     * 
     * @param customId カスタムアイテムID
     * @return 登録されている場合true
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static boolean isRegistered(String customId) {
        return getRegistry().isRegistered(customId);
    }

    /**
     * 新しいCustomItemBuilderを作成
     * 
     * @param customId カスタムアイテムID
     * @return CustomItemBuilder
     */
    public static CustomItemBuilder builder(String customId) {
        return new CustomItemBuilder(customId);
    }

    /**
     * 新しいCustomItemBuilderを作成（マテリアル指定）
     * 
     * @param customId カスタムアイテムID
     * @param material ベースマテリアル
     * @return CustomItemBuilder
     */
    public static CustomItemBuilder builder(String customId, org.bukkit.Material material) {
        return new CustomItemBuilder(customId, material);
    }

    /**
     * APIバージョンを取得
     * 
     * @return バージョン文字列
     * @throws IllegalStateException プラグインが有効でない場合
     */
    public static String getVersion() {
        return getAPI().getAPIVersion();
    }

    /**
     * プラグインが有効かチェック
     * 
     * @return 有効な場合true
     */
    public static boolean isEnabled() {
        try {
            return PaperItemAPI.getInstance() != null && 
                   PaperItemAPI.getInstance().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}