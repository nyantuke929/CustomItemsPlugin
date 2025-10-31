# CustomItemAPI

Paper 1.21.8用のカスタムアイテムAPI - `minecraft:diamond`のような独自アイテムID（例：`myplugin:chip`）を簡単に作成できます。

## 機能

- ✅ カスタムアイテムID（NamespacedKey形式）
- ✅ カスタムテクスチャ（CustomModelData）
- ✅ アイテム名・説明文
- ✅ エンチャント
- ✅ アイテムフラグ
- ✅ 破壊不可能設定
- ✅ 数量設定
- ✅ 簡単なビルダーパターン

## 依存関係の追加

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
        <artifactId>CustomItemAPI</artifactId>
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
    compileOnly 'com.github.ntn929:CustomItemAPI:1.0.0'
}
```

## 使い方

### 1. APIの初期化

```java
public class YourPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CustomItemAPI.initialize(this);
    }
}
```

### 2. カスタムアイテムの作成

```java
// ビルダーパターンで作成
new CustomItemBuilder("myplugin:chip", Material.DIAMOND)
    .displayName("§b特殊なチップ")
    .lore("§7これは特別なチップです")
    .customModelData(1)
    .buildAndRegister();
```

### 3. アイテムの取得

```java
CustomItem item = CustomItemAPI.getCustomItem("myplugin:chip");
ItemStack itemStack = item.toItemStack();
player.getInventory().addItem(itemStack);
```

### 4. ItemStackからカスタムアイテムを判定

```java
CustomItem customItem = CustomItemAPI.getCustomItem(itemStack);
if (customItem != null) {
    // カスタムアイテムです
    String id = customItem.getId();
}

// または
if (CustomItemAPI.isCustomItem(itemStack, "myplugin:chip")) {
    // myplugin:chipです
}
```

## プロジェクト構造

```
CustomItemAPI/
├── src/
│   └── main/
│       └── java/
│           └── com/github/ntn929/customitemapi/
│               ├── CustomItem.java          # カスタムアイテムクラス
│               ├── CustomItemAPI.java       # APIメインクラス
│               └── CustomItemBuilder.java   # ビルダークラス
├── pom.xml
├── jitpack.yml
└── README.md
```

## ライセンス

MIT License