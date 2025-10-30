package io.github.ntn929.paperitemapi;

import io.github.ntn929.paperitemapi.command.CustomItemCommand;
import io.github.ntn929.paperitemapi.command.ListItemsCommand;
import io.github.ntn929.paperitemapi.item.CustomItemRegistry;
import io.github.ntn929.paperitemapi.listener.CustomItemListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * PaperItemAPI - Custom Data Component API for Paper 1.21.8
 * Minecraft 1.21.xのカスタムデータコンポーネントシステムに対応したAPI
 * 
 * @author ntn929
 * @version 1.0.0
 */
public final class PaperItemAPI extends JavaPlugin {

    private static PaperItemAPI instance;
    private CustomItemRegistry itemRegistry;

    @Override
    public void onEnable() {
        instance = this;
        
        // 設定ファイルの保存
        saveDefaultConfig();
        
        // カスタムアイテムレジストリの初期化
        this.itemRegistry = new CustomItemRegistry();
        
        // コマンドの登録
        registerCommands();
        
        // リスナーの登録
        registerListeners();
        
        // 起動メッセージ
        getLogger().info("==========================================");
        getLogger().info("  PaperItemAPI has been enabled!");
        getLogger().info("  Version: " + getDescription().getVersion());
        getLogger().info("  Author: ntn929");
        getLogger().info("  Minecraft: 1.21.8");
        getLogger().info("==========================================");
        getLogger().info("Custom Data Component API is ready!");
        getLogger().info("Create custom items with namespace:key format");
        getLogger().info("Example: myitem:custom_sword");
    }

    @Override
    public void onDisable() {
        // レジストリのクリーンアップ
        if (itemRegistry != null) {
            getLogger().info("Unregistering " + itemRegistry.size() + " custom items...");
            itemRegistry.clear();
        }
        
        getLogger().info("==========================================");
        getLogger().info("  PaperItemAPI has been disabled!");
        getLogger().info("  Total custom items registered: " + 
            (itemRegistry != null ? itemRegistry.size() : 0));
        getLogger().info("==========================================");
    }

    /**
     * コマンドを登録
     */
    private void registerCommands() {
        // /customitem コマンド
        PluginCommand customItemCmd = getCommand("customitem");
        if (customItemCmd != null) {
            CustomItemCommand customItemCommand = new CustomItemCommand(this);
            customItemCmd.setExecutor(customItemCommand);
            customItemCmd.setTabCompleter(customItemCommand);
            getLogger().info("Registered command: /customitem");
        }

        // /listitems コマンド
        PluginCommand listItemsCmd = getCommand("listitems");
        if (listItemsCmd != null) {
            ListItemsCommand listItemsCommand = new ListItemsCommand(this);
            listItemsCmd.setExecutor(listItemsCommand);
            listItemsCmd.setTabCompleter(listItemsCommand);
            getLogger().info("Registered command: /listitems");
        }
    }

    /**
     * イベントリスナーを登録
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new CustomItemListener(this), this);
        getLogger().info("Registered event listeners");
    }

    /**
     * プラグインインスタンスを取得
     * 
     * @return PaperItemAPIインスタンス
     */
    public static PaperItemAPI getInstance() {
        return instance;
    }

    /**
     * カスタムアイテムレジストリを取得
     * 
     * @return CustomItemRegistryインスタンス
     */
    public CustomItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    /**
     * APIバージョンを取得
     * 
     * @return バージョン文字列
     */
    public String getAPIVersion() {
        return "1.0.0";
    }
}