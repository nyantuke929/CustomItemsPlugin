package com.example.customitems;

import org.bukkit.plugin.java.JavaPlugin;

import com.example.customitems.api.CustomItemsFactory;
import com.example.customitems.api.VersionedCustomItemAPI;
import com.example.customitems.commands.CustomItemCommand;
import com.example.customitems.items.ItemRegistry;
import com.example.customitems.listeners.ItemInteractListener;

/**
 * カスタムアイテムプラグインのメインクラス
 * バージョン対応APIを使用
 */
public class CustomItemsPlugin extends JavaPlugin {
    
    private VersionedCustomItemAPI api;
    private ItemRegistry itemRegistry;
    
    @Override
    public void onEnable() {
        getLogger().info("CustomItemsPlugin is starting...");
        
        try {
            // 1.21.8専用APIを初期化
            api = CustomItemsFactory.createAPI(this);
            getLogger().info("Initialized API for Minecraft " + api.getSupportedVersion() + " (1.21.8 only)");
            
            // アイテムレジストリを初期化
            itemRegistry = new ItemRegistry(api);
            itemRegistry.registerDefaultItems();
            
            // コマンドを登録
            getCommand("customitem").setExecutor(new CustomItemCommand(api));
            
            // リスナーを登録
            getServer().getPluginManager().registerEvents(new ItemInteractListener(api), this);
            
            getLogger().info("CustomItemsPlugin has been enabled!");
            getLogger().info("Registered " + api.getRegisteredItemIds().size() + " custom items");
            
        } catch (UnsupportedOperationException e) {
            getLogger().severe("Failed to initialize: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        } catch (Exception e) {
            getLogger().severe("Unexpected error during initialization: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onDisable() {
        getLogger().info("CustomItemsPlugin has been disabled!");
    }
    
    /**
     * APIインスタンスを取得
     * @return VersionedCustomItemAPI
     */
    public VersionedCustomItemAPI getAPI() {
        return api;
    }
    
    /**
     * アイテムレジストリを取得
     * @return ItemRegistry
     */
    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }
}