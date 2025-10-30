# CustomItemsPlugin

[![](https://jitpack.io/v/YOUR_USERNAME/CustomItemsPlugin.svg)](https://jitpack.io/#YOUR_USERNAME/CustomItemsPlugin)

Minecraft 1.21.x対応のカスタムアイテムAPIプラグイン

## 特徴

- 🎮 **F3+Hで独自ID表示** - `customitemsplugin:flame_sword` のような独自のアイテムIDをゲーム内で確認可能
- 🔄 **マルチバージョン対応** - 1.21.3, 1.21.4, 1.21.8に対応（自動バージョン検出）
- 🛠️ **簡単なAPI** - シンプルなビルダーパターンでアイテム作成
- 📦 **JitPack配布** - GitHubから直接依存関係として追加可能
- ⚡ **NMS使用** - 完全なカスタムアイテムをレジストリに登録

## 対応バージョン

- Minecraft 1.21.3 (Paper)
- Minecraft 1.21.4 (Paper)
- Minecraft 1.21.8 (Paper) - 将来対応予定

## インストール

### プラグインとして使用

1. [Releases](https://github.com/YOUR_USERNAME/CustomItemsPlugin/releases)から最新版をダウンロード
2. サーバーの`plugins`フォルダに配置
3. サーバーを起動

### ライブラリとして使用（Maven）

`pom.xml`に以下を追加:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.YOUR_USERNAME</groupId>
        <artifactId>CustomItemsPlugin</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### ライブラリとして使用（Gradle）

`build.gradle`に以下を追加:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.YOUR_USERNAME:CustomItemsPlugin:1.0.0'
}
```

## 使い方

### プラグインとして使用

```bash
# アイテムを取得
/customitem give flame_sword

# アイテム一覧を表示
/customitem list

# 手持ちアイテムを確認
/customitem check

# API情報を表示
/customitem version
```

### APIとして使用

```java
// プラグインのメインクラスで初期化
public class YourPlugin extends JavaPlugin {
    
    private VersionedCustomItemAPI api;
    
    @Override
    public void onEnable() {
        // バージョン自動検出
        api = CustomItemsFactory.createAPI(this);
        
        // カスタムアイテムを登録
        api.registerItem("my_sword",
            CustomItemsFactory.createBuilder(Material.DIAMOND_SWORD)
                .displayName("§cマイソード")
                .lore("§7説明文1", "§7説明文2")
                .maxStackSize(1)
                .fireResistant(true)
                .rarity(ItemRarity.EPIC)
        );
    }
}
```

```java
// アイテムを取得
ItemStack item = api.getCustomItem("my_sword");
player.getInventory().addItem(item);

// アイテムがカスタムアイテムか確認
if (api.isCustomItem(item)) {
    String id = api.getCustomItemId(item);
    player.sendMessage("カスタムアイテムID: " + id);
}

// 登録されているアイテム一覧
Set<String> ids = api.getRegisteredItemIds();
```

## デフォルトアイテム

プラグインには以下のアイテムがデフォルトで登録されています:

| ID | 名前 | 説明 |
|----|------|------|
| `flame_sword` | 炎の剣 | 右クリックで火の玉を発射 |
| `thunder_hammer` | 雷のハンマー | 右クリックで雷を召喚 |
| `healing_potion` | 癒しのポーション | 右クリックで体力全回復 |
| `magic_wand` | 魔法の杖 | 右クリックでランダムな魔法を発動 |
| `lucky_coin` | 幸運のコイン | レアドロップ率アップ |
| `teleport_stone` | テレポートストーン | 座標保存とテレポート |

## API仕様

### ItemBuilder メソッド

| メソッド | 説明 |
|---------|------|
| `displayName(String)` | アイテムの表示名を設定 |
| `lore(String...)` | 説明文を設定 |
| `maxStackSize(int)` | 最大スタック数を設定（1-99） |
| `fireResistant(boolean)` | 火炎耐性を設定 |
| `rarity(ItemRarity)` | レアリティを設定 |

### VersionedCustomItemAPI メソッド

| メソッド | 説明 |
|---------|------|
| `registerItem(String, ItemBuilder)` | カスタムアイテムを登録 |
| `getCustomItem(String)` | カスタムアイテムを取得 |
| `getCustomItem(String, int)` | 指定数量でアイテムを取得 |
| `isCustomItem(ItemStack)` | カスタムアイテムか判定 |
| `getCustomItemId(ItemStack)` | アイテムIDを取得 |
| `getRegisteredItemIds()` | 登録済みID一覧 |
| `getSupportedVersion()` | 対応バージョンを取得 |

## 開発者向け

### ビルド方法

```bash
git clone https://github.com/YOUR_USERNAME/CustomItemsPlugin.git
cd CustomItemsPlugin
mvn clean package
```

### 新しいバージョンへの対応

1. `api`パッケージに新しいNMS実装クラスを作成（例: `NMS_1_21_9.java`）
2. `VersionedCustomItemAPI.create()`メソッドに条件分岐を追加
3. `CustomItemsFactory.createBuilder()`に対応を追加

## 注意事項

⚠️ このプラグインはNMS（net.minecraft.server）を使用しています。

- Minecraftのバージョンアップデートで動作しなくなる可能性があります
- 必ず対応バージョンで使用してください
- サーバーのバックアップを取ってから導入してください

## ライセンス

MIT License

## 作者

YOUR_NAME

## リンク

- [GitHub](https://github.com/YOUR_USERNAME/CustomItemsPlugin)
- [Issues](https://github.com/YOUR_USERNAME/CustomItemsPlugin/issues)
- [Wiki](https://github.com/YOUR_USERNAME/CustomItemsPlugin/wiki)
