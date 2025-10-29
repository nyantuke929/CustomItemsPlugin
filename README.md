# CustomItemsPlugin

Paper 1.21.3+ 用のNMSカスタムアイテムプラグインです。Mojangマッピングを使用して、独自のカスタムアイテムを簡単に作成できます。

## 特徴

- 🎯 Paper 1.21.3+ 対応
- 🔧 Mojangマッピング使用（NMS互換）
- 📦 簡単なアイテム登録システム
- ⚡ カスタムアイテムの動作定義
- 🎨 完全なアイテムカスタマイズ（名前、説明、レアリティなど）

## 必要環境

- **Minecraft**: 1.21.3以降
- **サーバー**: Paper/Purpur/Folia など Paper系サーバー
- **Java**: 21以降

## インストール

1. [Releases](../../releases)から最新版の`CustomItemsPlugin-1.0-SNAPSHOT.jar`をダウンロード
2. サーバーの`plugins`フォルダに配置
3. サーバーを再起動

## 使用方法

### コマンド

```
/customitem <item_id> [amount]
```

**エイリアス**: `/ci`, `/citem`

**権限**: `customitems.give` (デフォルト: OP)

### 利用可能なアイテム

- `flame_sword` - 炎の剣（右クリックで炎耐性付与）
- `ice_sword` - 氷の剣（右クリックで移動速度上昇）
- `magic_wand` - 魔法の杖（魔法エフェクト）
- `gold_coin` - 金貨（通貨アイテム）
- `super_heal_potion` - 超回復ポーション（体力全回復+再生効果）
- `magic_book` - 魔法の書（右クリックでメッセージ表示）

### 使用例

```
/customitem flame_sword
/customitem gold_coin 64
/ci ice_sword 1
```

## 開発者向け

### ビルド方法

```bash
git clone https://github.com/YOUR_USERNAME/CustomItemsPlugin.git
cd CustomItemsPlugin
mvn clean package
```

ビルドされたjarファイルは`target/CustomItemsPlugin-1.0-SNAPSHOT.jar`に生成されます。

### カスタムアイテムの追加

`ItemRegistry.java`を編集してアイテムを追加できます：

```java
api.registerItem("your_item_id",
    new NMSCustomItemAPI.CustomItemBuilder(Material.DIAMOND)
        .displayName("§b§lカスタムアイテム")
        .lore(
            "§7説明文1",
            "§7説明文2"
        )
        .rarity(Rarity.EPIC)
        .maxStackSize(64)
        .fireResistant(true)
);
```

### アイテムの動作定義

`ItemInteractListener.java`でアイテムの右クリック/左クリック動作を定義できます。

## API使用例

```java
// プラグインインスタンスの取得
CustomItemsPlugin plugin = CustomItemsPlugin.getInstance();
NMSCustomItemAPI api = plugin.getItemAPI();

// カスタムアイテムの取得
ItemStack item = api.getCustomItem("flame_sword");
player.getInventory().addItem(item);

// アイテムがカスタムアイテムか確認
if (api.isCustomItem(item)) {
    String itemId = api.getCustomItemId(item);
    // 処理...
}
```

## 技術詳細

- **NMS バージョン**: Mojang Mappings
- **Paper API**: 1.21.3-R0.1-SNAPSHOT
- **ビルドツール**: Maven
- **Java バージョン**: 21

### 依存関係

- Paper API (provided)
- Paper NMS (provided)
- Mojang DataFixerUpper (provided)

## ライセンス

MIT License - 詳細は[LICENSE](LICENSE)を参照

## 貢献

プルリクエストを歓迎します！大きな変更の場合は、まずissueを開いて変更内容を議論してください。

## サポート

- **Issues**: [GitHub Issues](../../issues)
- **Discussions**: [GitHub Discussions](../../discussions)

## 作者

YourName

## 変更履歴

### v1.0.0
- 初回リリース
- 基本的なカスタムアイテムシステム
- 6種類のサンプルアイテム実装
