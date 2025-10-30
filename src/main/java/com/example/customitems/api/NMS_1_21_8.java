package com.example.customitems.api;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.craftbukkit.v1_21_R8.inventory.CraftItemStack;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Minecraft 1.21.8 専用のNMS実装
 */
public class NMS_1_21_8 implements VersionedCustomItemAPI {
    
    private final JavaPlugin plugin;
    private final Map<String, Item> registeredItems = new HashMap<>();
    private final Map<String, CustomItemBuilderImpl> builders = new HashMap<>();
    private final String namespace;
    
    public NMS_1_21_8(JavaPlugin plugin) {
        this.plugin = plugin;
        this.namespace = plugin.getName().toLowerCase();
        unfreezeRegistry();
    }
    
    @Override
    public void registerItem(String id, ItemBuilder builder) {
        if (!(builder instanceof CustomItemBuilderImpl)) {
            throw new IllegalArgumentException("Invalid builder type for 1.21.8");
        }
        
        CustomItemBuilderImpl impl = (CustomItemBuilderImpl) builder;
        
        try {
            // ベースアイテムのプロパティを取得
            Material baseMaterial = impl.baseMaterial;
            Item baseItem = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(baseMaterial)).getItem();
            
            // カスタムアイテムプロパティを作成
            Item.Properties properties = new Item.Properties()
                .stacksTo(impl.maxStackSize);
            
            if (impl.fireResistant) {
                properties.fireResistant();
            }
            
            if (impl.rarity != null) {
                properties.rarity(convertRarity(impl.rarity));
            }
            
            // 新しいアイテムを作成
            Item customItem = new Item(properties);
            
            // レジストリに登録
            ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, id);
            Registry.register(BuiltInRegistries.ITEM, resourceLocation, customItem);
            
            registeredItems.put(id, customItem);
            builders.put(id, impl);
            
            plugin.getLogger().info("Registered custom item: " + namespace + ":" + id);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to register item " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public org.bukkit.inventory.ItemStack getCustomItem(String id) {
        return getCustomItem(id, 1);
    }
    
    @Override
    public org.bukkit.inventory.ItemStack getCustomItem(String id, int amount) {
        Item item = registeredItems.get(id);
        if (item == null) {
            plugin.getLogger().warning("Custom item not found: " + id);
            return null;
        }
        
        // NMSアイテムスタックを作成
        ItemStack nmsStack = new ItemStack(item, amount);
        
        // データコンポーネントを設定
        CustomItemBuilderImpl builder = builders.get(id);
        if (builder != null) {
            if (builder.displayName != null) {
                nmsStack.set(DataComponents.ITEM_NAME, 
                    Component.literal(builder.displayName));
            }
            
            if (builder.lore != null && builder.lore.length > 0) {
                List<Component> loreComponents = new ArrayList<>();
                for (String line : builder.lore) {
                    loreComponents.add(Component.literal(line));
                }
                nmsStack.set(DataComponents.LORE, new ItemLore(loreComponents));
            }
        }
        
        // Bukkitアイテムスタックに変換
        return CraftItemStack.asBukkitCopy(nmsStack);
    }
    
    @Override
    public boolean isCustomItem(org.bukkit.inventory.ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        
        ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(nmsStack.getItem());
        
        return key != null && namespace.equals(key.getNamespace());
    }
    
    @Override
    public String getCustomItemId(org.bukkit.inventory.ItemStack item) {
        if (!isCustomItem(item)) {
            return null;
        }
        
        ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(nmsStack.getItem());
        
        return key != null ? key.getPath() : null;
    }
    
    @Override
    public Set<String> getRegisteredItemIds() {
        return new HashSet<>(registeredItems.keySet());
    }
    
    @Override
    public String getSupportedVersion() {
        return "1.21.8";
    }
    
    private void unfreezeRegistry() {
        try {
            // 1.21.8のfrozenフィールドを探す
            Field frozenField = findFrozenField();
            if (frozenField != null) {
                frozenField.setAccessible(true);
                frozenField.setBoolean(BuiltInRegistries.ITEM, false);
                plugin.getLogger().info("Successfully unfroze item registry for 1.21.8");
            } else {
                plugin.getLogger().severe("Could not find frozen field in Registry class");
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to unfreeze registry: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Field findFrozenField() {
        // 1.21.8で可能性のあるフィールド名を試す
        String[] possibleFieldNames = {"l", "m", "n", "frozen", "locked", "o", "p"};
        
        for (String fieldName : possibleFieldNames) {
            try {
                Field field = Registry.class.getDeclaredField(fieldName);
                if (field.getType() == boolean.class) {
                    plugin.getLogger().info("Found frozen field: " + fieldName);
                    return field;
                }
            } catch (NoSuchFieldException ignored) {
                // 次のフィールド名を試す
            }
        }
        
        return null;
    }
    
    private Rarity convertRarity(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> Rarity.COMMON;
            case UNCOMMON -> Rarity.UNCOMMON;
            case RARE -> Rarity.RARE;
            case EPIC -> Rarity.EPIC;
        };
    }
    
    /**
     * 1.21.8専用のアイテムビルダー実装
     */
    public static class CustomItemBuilderImpl implements ItemBuilder {
        
        private final Material baseMaterial;
        private String displayName;
        private String[] lore;
        private int maxStackSize = 64;
        private boolean fireResistant = false;
        private ItemRarity rarity = ItemRarity.COMMON;
        
        public CustomItemBuilderImpl(Material baseMaterial) {
            this.baseMaterial = baseMaterial;
        }
        
        @Override
        public ItemBuilder displayName(String name) {
            this.displayName = name;
            return this;
        }
        
        @Override
        public ItemBuilder lore(String... lines) {
            this.lore = lines;
            return this;
        }
        
        @Override
        public ItemBuilder maxStackSize(int size) {
            this.maxStackSize = Math.min(99, Math.max(1, size));
            return this;
        }
        
        @Override
        public ItemBuilder fireResistant(boolean resistant) {
            this.fireResistant = resistant;
            return this;
        }
        
        @Override
        public ItemBuilder rarity(ItemRarity rarity) {
            this.rarity = rarity;
            return this;
        }
    }
}
