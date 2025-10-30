package io.github.ntn929.paperitemapi.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * カスタムアイテムを登録・管理するレジストリ
 * 
 * スレッドセーフな実装でマルチスレッド環境でも安全に使用できます。
 * 
 * 主な機能:
 * - カスタムアイテムの登録・登録解除
 * - IDによる検索
 * - ItemStackからのカスタムアイテム検索
 * - namespace（プレフィックス）によるフィルタリング
 * - マテリアルによるフィルタリング
 * 
 * 使用例:
 * <pre>
 * CustomItemRegistry registry = PaperItemAPI.getInstance().getItemRegistry();
 * 
 * // 登録
 * CustomItem item = new CustomItemBuilder("myitem:sword").build();
 * registry.register(item);
 * 
 * // 取得
 * CustomItem found = registry.get("myitem:sword");
 * 
 * // ItemStackから検索
 * CustomItem fromStack = registry.getFromItemStack(itemStack);
 * </pre>
 * 
 * @author ntn929
 */
public class CustomItemRegistry {

    private final Map<String, CustomItem> registeredItems;
    private final Map<String, Set<String>> namespaceIndex;
    private final Map<Material, Set<String>> materialIndex;

    /**
     * CustomItemRegistryを作成
     */
    public CustomItemRegistry() {
        this.registeredItems = new ConcurrentHashMap<>();
        this.namespaceIndex = new ConcurrentHashMap<>();
        this.materialIndex = new ConcurrentHashMap<>();
    }

    /**
     * カスタムアイテムを登録
     * 
     * @param item 登録するCustomItem
     * @return 登録に成功した場合true、既に登録済みの場合false
     * @throws IllegalArgumentException itemまたはcustomIdがnullの場合
     */
    public boolean register(CustomItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getCustomId() == null) {
            throw new IllegalArgumentException("Item customId cannot be null");
        }

        String id = item.getCustomId();
        
        // 既に登録済みの場合は失敗
        if (registeredItems.containsKey(id)) {
            return false;
        }

        // アイテムを登録
        registeredItems.put(id, item);
        
        // インデックスを更新
        updateNamespaceIndex(id, true);
        updateMaterialIndex(id, item.getBaseMaterial(), true);

        return true;
    }

    /**
     * カスタムアイテムを登録解除
     * 
     * @param customId カスタムアイテムID
     * @return 登録解除に成功した場合true、存在しない場合false
     */
    public boolean unregister(String customId) {
        if (customId == null) {
            return false;
        }

        CustomItem removed = registeredItems.remove(customId);
        
        if (removed != null) {
            // インデックスを更新
            updateNamespaceIndex(customId, false);
            updateMaterialIndex(customId, removed.getBaseMaterial(), false);
            return true;
        }
        
        return false;
    }

    /**
     * カスタムアイテムを取得
     * 
     * @param customId カスタムアイテムID
     * @return CustomItem（存在しない場合null）
     */
    public CustomItem get(String customId) {
        return registeredItems.get(customId);
    }

    /**
     * ItemStackからカスタムアイテムを取得
     * 
     * @param itemStack ItemStack
     * @return 該当するCustomItem（存在しない場合null）
     */
    public CustomItem getFromItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }

        // マテリアルでフィルタリングしてから検索（パフォーマンス最適化）
        Material material = itemStack.getType();
        Set<String> candidateIds = materialIndex.get(material);
        
        if (candidateIds == null || candidateIds.isEmpty()) {
            return null;
        }

        // 候補の中から一致するアイテムを検索
        for (String id : candidateIds) {
            CustomItem item = registeredItems.get(id);
            if (item != null && item.isCustomItem(itemStack)) {
                return item;
            }
        }

        return null;
    }

    /**
     * カスタムアイテムが登録されているかチェック
     * 
     * @param customId カスタムアイテムID
     * @return 登録されている場合true
     */
    public boolean isRegistered(String customId) {
        return registeredItems.containsKey(customId);
    }

    /**
     * ItemStackがカスタムアイテムかチェック
     * 
     * @param itemStack ItemStack
     * @return カスタムアイテムの場合true
     */
    public boolean isCustomItem(ItemStack itemStack) {
        return getFromItemStack(itemStack) != null;
    }

    /**
     * 登録されているすべてのカスタムアイテムIDを取得
     * 
     * @return カスタムアイテムIDのセット（読み取り専用）
     */
    public Set<String> getRegisteredIds() {
        return Collections.unmodifiableSet(new HashSet<>(registeredItems.keySet()));
    }

    /**
     * 登録されているすべてのカスタムアイテムを取得
     * 
     * @return CustomItemのコレクション（読み取り専用）
     */
    public Collection<CustomItem> getAllItems() {
        return Collections.unmodifiableCollection(new ArrayList<>(registeredItems.values()));
    }

    /**
     * 登録されているアイテム数を取得
     * 
     * @return アイテム数
     */
    public int size() {
        return registeredItems.size();
    }

    /**
     * レジストリが空かチェック
     * 
     * @return 空の場合true
     */
    public boolean isEmpty() {
        return registeredItems.isEmpty();
    }

    /**
     * すべての登録を解除
     */
    public void clear() {
        registeredItems.clear();
        namespaceIndex.clear();
        materialIndex.clear();
    }

    /**
     * namespace（プレフィックス）でフィルタリング
     * 
     * 例: "myitem" を指定すると "myitem:sword", "myitem:armor" などが取得される
     * 
     * @param namespace namespace
     * @return 該当するCustomItemのリスト
     */
    public List<CustomItem> getByNamespace(String namespace) {
        if (namespace == null) {
            return Collections.emptyList();
        }

        Set<String> ids = namespaceIndex.get(namespace);
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return ids.stream()
            .map(registeredItems::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * マテリアルでフィルタリング
     * 
     * @param material マテリアル
     * @return 該当するCustomItemのリスト
     */
    public List<CustomItem> getByMaterial(Material material) {
        if (material == null) {
            return Collections.emptyList();
        }

        Set<String> ids = materialIndex.get(material);
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return ids.stream()
            .map(registeredItems::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * 登録されているすべてのnamespaceを取得
     * 
     * @return namespaceのセット
     */
    public Set<String> getNamespaces() {
        return Collections.unmodifiableSet(new HashSet<>(namespaceIndex.keySet()));
    }

    /**
     * 登録されているすべてのマテリアルを取得
     * 
     * @return マテリアルのセット
     */
    public Set<Material> getMaterials() {
        return Collections.unmodifiableSet(new HashSet<>(materialIndex.keySet()));
    }

    /**
     * カスタムIDが有効な形式かチェック
     * 
     * 有効な形式: "namespace:key" または "key"
     * 
     * @param customId カスタムアイテムID
     * @return 有効な場合true
     */
    public static boolean isValidCustomId(String customId) {
        if (customId == null || customId.isEmpty()) {
            return false;
        }

        // 基本的な文字チェック
        if (!customId.matches("^[a-z0-9_.-]+(:[a-z0-9_.-]+)?$")) {
            return false;
        }

        // コロンが複数ある場合は無効
        int colonCount = customId.length() - customId.replace(":", "").length();
        return colonCount <= 1;
    }

    /**
     * カスタムIDからnamespaceを抽出
     * 
     * @param customId カスタムアイテムID
     * @return namespace（存在しない場合は"minecraft"）
     */
    public static String extractNamespace(String customId) {
        if (customId == null) {
            return "minecraft";
        }

        int colonIndex = customId.indexOf(':');
        if (colonIndex > 0) {
            return customId.substring(0, colonIndex);
        }
        return "minecraft";
    }

    /**
     * カスタムIDからkeyを抽出
     * 
     * @param customId カスタムアイテムID
     * @return key
     */
    public static String extractKey(String customId) {
        if (customId == null) {
            return "";
        }

        int colonIndex = customId.indexOf(':');
        if (colonIndex > 0 && colonIndex < customId.length() - 1) {
            return customId.substring(colonIndex + 1);
        }
        return customId;
    }

    /**
     * namespaceインデックスを更新
     */
    private void updateNamespaceIndex(String customId, boolean add) {
        String namespace = extractNamespace(customId);
        
        if (add) {
            namespaceIndex.computeIfAbsent(namespace, k -> ConcurrentHashMap.newKeySet())
                .add(customId);
        } else {
            Set<String> ids = namespaceIndex.get(namespace);
            if (ids != null) {
                ids.remove(customId);
                if (ids.isEmpty()) {
                    namespaceIndex.remove(namespace);
                }
            }
        }
    }

    /**
     * マテリアルインデックスを更新
     */
    private void updateMaterialIndex(String customId, Material material, boolean add) {
        if (material == null) {
            return;
        }

        if (add) {
            materialIndex.computeIfAbsent(material, k -> ConcurrentHashMap.newKeySet())
                .add(customId);
        } else {
            Set<String> ids = materialIndex.get(material);
            if (ids != null) {
                ids.remove(customId);
                if (ids.isEmpty()) {
                    materialIndex.remove(material);
                }
            }
        }
    }

    /**
     * レジストリの統計情報を取得
     * 
     * @return 統計情報の文字列
     */
    public String getStatistics() {
        return String.format(
            "CustomItemRegistry Statistics:\n" +
            "  Total Items: %d\n" +
            "  Namespaces: %d\n" +
            "  Materials: %d",
            size(),
            namespaceIndex.size(),
            materialIndex.size()
        );
    }

    @Override
    public String toString() {
        return "CustomItemRegistry{" +
                "items=" + registeredItems.size() +
                ", namespaces=" + namespaceIndex.size() +
                ", materials=" + materialIndex.size() +
                '}';
    }
}