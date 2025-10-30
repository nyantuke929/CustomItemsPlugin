# 🎮 PaperItemAPI

[![](https://jitpack.io/v/ntn929/PaperItemAPI.svg)](https://jitpack.io/#ntn929/PaperItemAPI)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.8-green.svg)](https://papermc.io/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Minecraft 1.21.8用のカスタムアイテムデータコンポーネントAPI

完全カスタムアイテムを作成し、F3+Hで`minecraft:diamond`のような独自アイテムIDを表示できます。

## ✨ 特徴

- 🎯 **完全カスタムアイテム** - `namespace:key`形式の独自アイテムID
- 💾 **データコンポーネント** - 10種類のデータ型をサポート
- 🔧 **簡単なAPI** - ビルダーパターンで直感的に作成
- ⚡ **高性能** - インデックスとキャッシュによる高速検索
- 🔒 **スレッドセーフ** - マルチスレッド環境で安全
- 📦 **軽量** - 依存関係はPaper APIのみ

## 📦 インストール

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.ntn929</groupId>
        <artifactId>PaperItemAPI</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.ntn929:PaperItemAPI:1.0.0'
}
```

## 🚀 使用例

### 基本的な使い方
```java
import io.github.ntn929.paperitemapi.PaperItemAPI;
import io.github.ntn929.paperitemapi.item.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

// カスタムアイテムの作成
CustomItem legendarySword = new CustomItemBuilder("myitem:legendary_sword", Material.DIAMOND_SWORD)
    .displayName(Component.text("伝説の剣").color(NamedTextColor.GOLD))
    .lore(
        "古代の力を宿した剣",
        "攻撃力: +100",
        "耐久力: 無限"
    )
    .customModelData(1001)
    .addComponent("attack_power", 100)
    .addComponent("legendary", true)
    .addComponent("rarity", "legendary")
    .unbreakable()
    .build();

// レジストリに登録
PaperItemAPI.getInstance().getItemRegistry().register(legendarySword);

// ItemStackとして取得
ItemStack item = legendarySword.toItemStack();

// プレイヤーに付与
player.getInventory().addItem(item);
```

### アイテムの検証
```java
// ItemStackがカスタムアイテムかチェック
CustomItem customItem = PaperItemAPI.getInstance()
    .getItemRegistry()
    .getFromItemStack(itemStack);

if (customItem != null) {
    // カスタムアイテムの処理
    String id = customItem.getCustomId();
    player.sendMessage("カスタムアイテム: " + id);
}
```

### コンポーネントの使用
```java
// 様々な型のコンポーネント
CustomItem magicWand = new CustomItemBuilder("myitem:magic_wand", Material.STICK)
    .displayName("魔法の杖")
    .addComponent("mana", 500)              // Integer
    .addComponent("spell_type", "fire")     // String
    .addComponent("power", 99.5)            // Double
    .addComponent("enchanted", true)        // Boolean
    .addComponent("created_at", System.currentTimeMillis())  // Long
    .build();
```

### イベントリスナー
```java
@EventHandler
public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();
    if (item == null) return;
    
    CustomItem customItem = PaperItemAPI.getInstance()
        .getItemRegistry()
        .getFromItemStack(item);
    
    if (customItem != null && customItem.getCustomId().equals("myitem:legendary_sword")) {
        Player player = event.getPlayer();
        
        // コンポーネントから値を取得
        Object power = customItem.getComponentValue(item, "attack_power");
        if (power instanceof Integer) {
            player.sendMessage("攻撃力: " + power);
        }
        
        // 特別な効果を発動
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 2));
    }
}
```

## 📚 API リファレンス

### CustomItemBuilder

| メソッド | 説明 |
|---------|------|
| `displayName(Component)` | 表示名を設定 |
| `lore(String...)` | Loreを設定 |
| `customModelData(int)` | カスタムモデルデータを設定 |
| `unbreakable()` | 耐久無限にする |
| `addComponent(String, T)` | コンポーネントを追加 |
| `build()` | CustomItemを構築 |

### CustomItemRegistry

| メソッド | 説明 |
|---------|------|
| `register(CustomItem)` | アイテムを登録 |
| `get(String)` | IDでアイテムを取得 |
| `getFromItemStack(ItemStack)` | ItemStackから取得 |
| `getByNamespace(String)` | namespaceで検索 |
| `getAllItems()` | 全アイテムを取得 |

### CustomDataComponent

サポートする型:
- `Integer` / `Long` / `Byte`
- `Double` / `Float`
- `String`
- `Boolean`
- `byte[]` / `int[]` / `long[]`

## 🎮 コマンド

### `/customitem give <player> <itemId> [amount]`
プレイヤーにカスタムアイテムを付与

### `/listitems [namespace]`
登録されているカスタムアイテムを一覧表示

## 🔧 設定

`config.yml`で以下の設定が可能:
```yaml
debug: false
log-registration: true
max-registered-items: 0
default-stack-size: 1
```

## 📖 ドキュメント

詳細なドキュメントは[Wiki](https://github.com/ntn929/PaperItemAPI/wiki)を参照してください。

## 🤝 コントリビューション

プルリクエストを歓迎します！

1. Fork する
2. Feature ブランチを作成 (`git checkout -b feature/amazing-feature`)
3. 変更をコミット (`git commit -m 'Add amazing feature'`)
4. ブランチにプッシュ (`git push origin feature/amazing-feature`)
5. Pull Request を作成

## 📝 ライセンス

このプロジェクトは[MIT License](LICENSE)の下でライセンスされています。

## 👤 作者

**ntn929**

- GitHub: [@ntn929](https://github.com/ntn929)

## 🙏 謝辞

- [PaperMC](https://papermc.io/) - 素晴らしいサーバーソフトウェア
- Minecraft コミュニティ

---

⭐ このプロジェクトが役に立ったらスターをお願いします！