package com.example.customitems.api;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Paper 1.21.3固定 NMSカスタムアイテムAPI
 * F3+Hで独自のアイテムID（例: customitemsplugin:flame_sword）を表示
 */
public class NMSCustomItemAPI {
    
    private final JavaPlugin plugin;
    private final Map<String, CustomItem> registeredItems;
    private final NamespacedKey itemIdKey;
    private static Field frozenField = null;
    
    public NMSCustomItemAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        this.registeredItems = new HashMap<>();
        this.itemIdKey = new NamespacedKey(plugin, "custom_item_id");
        
        // frozenフィールドを事前に取得
        if (frozenField == null) {
            try {
                frozenField = findFrozenField();
                frozenField.setAccessible(true);
                plugin.getLogger().info("Successfully found registry frozen field");
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to find frozen field! Custom items will not work.");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * カスタムアイテムを登録
     */
    public void registerItem(String id, CustomItemBuilder builder) {
        try {
            CustomItem customItem = builder.build(plugin, id, itemIdKey);
            registeredItems.put(id, customItem);
            
            String fullId = plugin.getName().toLowerCase() + ":" + id;
            plugin.getLogger().info("✓ Registered custom item: " + fullId);
        } catch (Exception e) {
            plugin.getLogger().severe("✗ Failed to register custom item: " + id);
            e.printStackTrace();
        }
    }
    
    /**
     * カスタムアイテムのItemStackを取得
     */
    public org.bukkit.inventory.ItemStack getCustomItem(String id) {
        return getCustomItem(id, 1);
    }
    
    /**
     * カスタムアイテムのItemStackを指定数量で取得
     */
    public org.bukkit.inventory.ItemStack getCustomItem(String id, int amount) {
        CustomItem customItem = registeredItems.get(id);
        if (customItem == null) {
            return null;
        }
        return customItem.createItemStack(amount);
    }
    
    /**
     * アイテムがカスタムアイテムかどうかを判定
     */
    public boolean isCustomItem(org.bukkit.inventory.ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
            .has(itemIdKey, PersistentDataType.STRING);
    }
    
    /**
     * カスタムアイテムのIDを取得
     */
    public String getCustomItemId(org.bukkit.inventory.ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer()
            .get(itemIdKey, PersistentDataType.STRING);
    }
    
    /**
     * 登録されているアイテムIDの一覧を取得
     */
    public Set<String> getRegisteredItemIds() {
        return new HashSet<>(registeredItems.keySet());
    }
    
    /**
     * 登録されているアイテムの一覧を取得
     */
    public Collection<CustomItem> getRegisteredItems() {
        return new ArrayList<>(registeredItems.values());
    }
    
    /**
     * カスタムアイテムクラス
     */
    public static class CustomItem {
        private final Item nmsItem;
        private final String id;
        private final String namespace;
        private final NamespacedKey itemIdKey;
        private String displayName;
        private List<String> lore;
        private final Material baseTexture;
        
        public CustomItem(String namespace, String id, Item nmsItem, Material baseTexture, NamespacedKey itemIdKey) {
            this.namespace = namespace;
            this.id = id;
            this.nmsItem = nmsItem;
            this.baseTexture = baseTexture;
            this.itemIdKey = itemIdKey;
            this.lore = new ArrayList<>();
        }
        
        public Item getNMSItem() {
            return nmsItem;
        }
        
        public String getId() {
            return id;
        }
        
        public String getFullId() {
            return namespace + ":" + id;
        }
        
        public Material getBaseTexture() {
            return baseTexture;
        }
        
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        
        public void setLore(List<String> lore) {
            this.lore = lore;
        }
        
        public org.bukkit.inventory.ItemStack createItemStack() {
            return createItemStack(1);
        }
        
        /**
         * アイテムスタックを作成
         * NMSアイテムからBukkitアイテムに変換し、メタデータを付与
         */
        public org.bukkit.inventory.ItemStack createItemStack(int amount) {
            // NMSアイテムからCraftItemStackを作成
            net.minecraft.world.item.ItemStack nmsStack = new net.minecraft.world.item.ItemStack(nmsItem, amount);
            org.bukkit.inventory.ItemStack bukkitStack = CraftItemStack.asBukkitCopy(nmsStack);
            
            // テクスチャ用のマテリアルを設定（見た目）
            bukkitStack.setType(baseTexture);
            
            org.bukkit.inventory.meta.ItemMeta meta = bukkitStack.getItemMeta();
            if (meta != null) {
                // カスタムアイテムIDを保存
                meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, id);
                
                // 表示名を設定
                if (displayName != null) {
                    meta.setDisplayName(displayName);
                }
                
                // Loreを設定
                if (!lore.isEmpty()) {
                    meta.setLore(new ArrayList<>(lore));
                }
                
                bukkitStack.setItemMeta(meta);
            }
            
            return bukkitStack;
        }
    }
    
    /**
     * カスタムアイテムビルダー
     */
    public static class CustomItemBuilder {
        private final Material baseTexture;
        private String displayName;
        private List<String> lore;
        private int maxStackSize;
        private Rarity rarity;
        private boolean fireResistant;
        
        public CustomItemBuilder(Material baseTexture) {
            this.baseTexture = baseTexture;
            this.lore = new ArrayList<>();
            this.maxStackSize = 64;
            this.rarity = Rarity.COMMON;
            this.fireResistant = false;
        }
        
        public CustomItemBuilder displayName(String name) {
            this.displayName = name;
            return this;
        }
        
        public CustomItemBuilder lore(String... lines) {
            this.lore = new ArrayList<>(Arrays.asList(lines));
            return this;
        }
        
        public CustomItemBuilder lore(List<String> lines) {
            this.lore = new ArrayList<>(lines);
            return this;
        }
        
        public CustomItemBuilder addLore(String line) {
            this.lore.add(line);
            return this;
        }
        
        public CustomItemBuilder maxStackSize(int size) {
            this.maxStackSize = Math.max(1, Math.min(99, size));
            return this;
        }
        
        public CustomItemBuilder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }
        
        public CustomItemBuilder fireResistant(boolean resistant) {
            this.fireResistant = resistant;
            return this;
        }
        
        /**
         * カスタムアイテムをビルド
         */
        protected CustomItem build(JavaPlugin plugin, String id, NamespacedKey itemIdKey) throws Exception {
            // アイテムプロパティを作成
            Item.Properties properties = new Item.Properties()
                .stacksTo(maxStackSize)
                .rarity(rarity);
            
            if (fireResistant) {
                properties.fireResistant();
            }
            
            // 新しいItemインスタンスを作成
            Item customItem = new Item(properties);
            
            // ResourceLocationを作成
            String namespace = plugin.getName().toLowerCase();
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath(namespace, id);
            
            // レジストリに登録（F3+Hで表示されるようになる）
            registerToRegistry(plugin, location, customItem);
            
            // CustomItemオブジェクトを作成
            CustomItem custom = new CustomItem(namespace, id, customItem, baseTexture, itemIdKey);
            custom.setDisplayName(displayName);
            custom.setLore(lore);
            
            return custom;
        }
        
        /**
         * レジストリへの登録（重要: F3+Hで表示されるようにする）
         */
        private static void registerToRegistry(JavaPlugin plugin, ResourceLocation location, Item item) throws Exception {
            if (frozenField == null) {
                throw new IllegalStateException("Frozen field not initialized!");
            }
            
            try {
                // レジストリを一時的にアンフリーズ
                synchronized (BuiltInRegistries.ITEM) {
                    boolean wasFrozen = frozenField.getBoolean(BuiltInRegistries.ITEM);
                    
                    if (wasFrozen) {
                        frozenField.setBoolean(BuiltInRegistries.ITEM, false);
                    }
                    
                    // アイテムを登録（これでF3+Hに表示される）
                    Registry.register(BuiltInRegistries.ITEM, location, item);
                    
                    // レジストリを再フリーズ
                    if (wasFrozen) {
                        frozenField.setBoolean(BuiltInRegistries.ITEM, true);
                    }
                }
                
                plugin.getLogger().fine("Successfully registered " + location + " to registry");
                
            } catch (Exception e) {
                throw new RuntimeException("Failed to register item to registry: " + location, e);
            }
        }
    }
    
    /**
     * frozenフィールドを探す（1.21.3 Mojangマッピング用）
     */
    private static Field findFrozenField() throws NoSuchFieldException {
        // 1.21.3 Mojangマッピングでの候補フィールド名
        String[] candidateNames = {
            "frozen",  // 通常のMojangマッピング名
            "l", "m", "k", "n", "o", "p"  // 難読化の可能性
        };
        
        // まず候補名で検索
        for (String fieldName : candidateNames) {
            try {
                Field field = BuiltInRegistries.ITEM.getClass().getDeclaredField(fieldName);
                if (field.getType() == boolean.class) {
                    return field;
                }
            } catch (NoSuchFieldException ignored) {
            }
        }
        
        // 見つからなければ全フィールドをスキャン
        for (Field field : BuiltInRegistries.ITEM.getClass().getDeclaredFields()) {
            if (field.getType() == boolean.class) {
                // 唯一のbooleanフィールドがfrozenフィールドのはず
                return field;
            }
        }
        
        throw new NoSuchFieldException("Could not find 'frozen' field in Item registry");
    }
}
