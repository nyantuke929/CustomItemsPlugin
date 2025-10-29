package com.example.customitems;

import com.example.customitems.api.NMSCustomItemAPI;
import com.example.customitems.commands.CustomItemCommand;
import com.example.customitems.items.ItemRegistry;
import com.example.customitems.listeners.ItemInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemsPlugin extends JavaPlugin {
    
    private static CustomItemsPlugin instance;
    private NMSCustomItemAPI itemAPI;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // APIを初期化
        itemAPI = new NMSCustomItemAPI(this);
        
        // カスタムアイテムを登録
        ItemRegistry.registerItems(itemAPI);
        
        // コマンドを登録
        getCommand("customitem").setExecutor(new CustomItemCommand(itemAPI));
        
        // イベントリスナーを登録
        getServer().getPluginManager().registerEvents(new ItemInteractListener(itemAPI), this);
        
        getLogger().info("CustomItems Plugin has been enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("CustomItems Plugin has been disabled!");
    }
    
    public static CustomItemsPlugin getInstance() {
        return instance;
    }
    
    public NMSCustomItemAPI getItemAPI() {
        return itemAPI;
    }
}