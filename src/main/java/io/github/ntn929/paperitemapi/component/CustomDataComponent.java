package io.github.ntn929.paperitemapi.component;

import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * カスタムデータコンポーネントを管理するクラス
 * Minecraft 1.21.xのData Componentsシステムに対応
 * 
 * PersistentDataContainerを使用して様々な型のデータを
 * アイテムに保存・取得することができます。
 * 
 * サポートする型:
 * - Integer
 * - String
 * - Double
 * - Float
 * - Long
 * - Byte
 * - Boolean
 * - byte[]
 * - int[]
 * - long[]
 * 
 * 使用例:
 * <pre>
 * CustomDataComponent&lt;Integer&gt; powerComponent = CustomDataComponent.ofInt(100);
 * powerComponent.apply(container, key);
 * 
 * Integer power = powerComponent.get(container, key);
 * </pre>
 * 
 * @param <T> データ型
 * @author ntn929
 */
public class CustomDataComponent<T> {

    private final T value;
    private final PersistentDataType<?, T> dataType;

    /**
     * CustomDataComponentを作成
     * 
     * @param value 値
     * @param dataType データ型
     */
    private CustomDataComponent(T value, PersistentDataType<?, T> dataType) {
        this.value = Objects.requireNonNull(value, "Value cannot be null");
        this.dataType = Objects.requireNonNull(dataType, "DataType cannot be null");
    }

    /**
     * PersistentDataContainerに適用
     * 
     * @param container 適用先のコンテナ
     * @param key 保存キー
     * @throws IllegalArgumentException containerまたはkeyがnullの場合
     */
    public void apply(PersistentDataContainer container, NamespacedKey key) {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null");
        }
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        container.set(key, dataType, value);
    }

    /**
     * PersistentDataContainerから値を取得
     * 
     * @param container 取得元のコンテナ
     * @param key 保存キー
     * @return 取得した値（存在しない場合null）
     */
    public T get(PersistentDataContainer container, NamespacedKey key) {
        if (container == null || key == null) {
            return null;
        }
        
        if (container.has(key, dataType)) {
            return container.get(key, dataType);
        }
        return null;
    }

    /**
     * PersistentDataContainerに値が存在するかチェック
     * 
     * @param container チェック対象のコンテナ
     * @param key 保存キー
     * @return 存在する場合true
     */
    public boolean has(PersistentDataContainer container, NamespacedKey key) {
        if (container == null || key == null) {
            return false;
        }
        return container.has(key, dataType);
    }

    /**
     * PersistentDataContainerから値を削除
     * 
     * @param container 削除対象のコンテナ
     * @param key 保存キー
     */
    public void remove(PersistentDataContainer container, NamespacedKey key) {
        if (container != null && key != null) {
            container.remove(key);
        }
    }

    /**
     * 値を取得
     * 
     * @return 保持している値
     */
    public T getValue() {
        return value;
    }

    /**
     * データ型を取得
     * 
     * @return PersistentDataType
     */
    public PersistentDataType<?, T> getDataType() {
        return dataType;
    }

    // ========== ファクトリーメソッド ==========

    /**
     * 整数型コンポーネントを作成
     * 
     * @param value 整数値
     * @return CustomDataComponent
     */
    public static CustomDataComponent<Integer> ofInt(int value) {
        return new CustomDataComponent<>(value, PersistentDataType.INTEGER);
    }

    /**
     * 文字列型コンポーネントを作成
     * 
     * @param value 文字列値
     * @return CustomDataComponent
     * @throws IllegalArgumentException valueがnullの場合
     */
    public static CustomDataComponent<String> ofString(String value) {
        return new CustomDataComponent<>(value, PersistentDataType.STRING);
    }

    /**
     * Double型コンポーネントを作成
     * 
     * @param value Double値
     * @return CustomDataComponent
     */
    public static CustomDataComponent<Double> ofDouble(double value) {
        return new CustomDataComponent<>(value, PersistentDataType.DOUBLE);
    }

    /**
     * Float型コンポーネントを作成
     * 
     * @param value Float値
     * @return CustomDataComponent
     */
    public static CustomDataComponent<Float> ofFloat(float value) {
        return new CustomDataComponent<>(value, PersistentDataType.FLOAT);
    }

    /**
     * Boolean型コンポーネントを作成
     * 内部的にはByteとして保存されます（1=true, 0=false）
     * 
     * @param value Boolean値
     * @return CustomDataComponent
     */
    public static CustomDataComponent<Byte> ofBoolean(boolean value) {
        return new CustomDataComponent<>(
            value ? (byte) 1 : (byte) 0,
            PersistentDataType.BYTE
        );
    }

    /**
     * Booleanコンポーネントから値を取得
     * 
     * @param container 取得元のコンテナ
     * @param key 保存キー
     * @return Boolean値（存在しない場合null）
     */
    public static Boolean getBooleanValue(PersistentDataContainer container, NamespacedKey key) {
        if (container == null || key == null) {
            return null;
        }
        
        if (container.has(key, PersistentDataType.BYTE)) {
            Byte value = container.get(key, PersistentDataType.BYTE);
            return value != null && value == 1;
        }
        return null;
    }

    /**
     * Long型コンポーネントを作成
     * 
     * @param value Long値
     * @return CustomDataComponent
     */
    public static CustomDataComponent<Long> ofLong(long value) {
        return new CustomDataComponent<>(value, PersistentDataType.LONG);
    }

    /**
     * Byte型コンポーネントを作成
     * 
     * @param value Byte値
     * @return CustomDataComponent
     */
    public static CustomDataComponent<Byte> ofByte(byte value) {
        return new CustomDataComponent<>(value, PersistentDataType.BYTE);
    }

    /**
     * バイト配列型コンポーネントを作成
     * 
     * @param value バイト配列
     * @return CustomDataComponent
     * @throws IllegalArgumentException valueがnullの場合
     */
    public static CustomDataComponent<byte[]> ofByteArray(byte[] value) {
        return new CustomDataComponent<>(value, PersistentDataType.BYTE_ARRAY);
    }

    /**
     * 整数配列型コンポーネントを作成
     * 
     * @param value 整数配列
     * @return CustomDataComponent
     * @throws IllegalArgumentException valueがnullの場合
     */
    public static CustomDataComponent<int[]> ofIntArray(int[] value) {
        return new CustomDataComponent<>(value, PersistentDataType.INTEGER_ARRAY);
    }

    /**
     * Long配列型コンポーネントを作成
     * 
     * @param value Long配列
     * @return CustomDataComponent
     * @throws IllegalArgumentException valueがnullの場合
     */
    public static CustomDataComponent<long[]> ofLongArray(long[] value) {
        return new CustomDataComponent<>(value, PersistentDataType.LONG_ARRAY);
    }

    // ========== ユーティリティメソッド ==========

    /**
     * 2つのコンポーネントが同じ型かチェック
     * 
     * @param other 比較対象のコンポーネント
     * @return 同じ型の場合true
     */
    public boolean isSameType(CustomDataComponent<?> other) {
        if (other == null) {
            return false;
        }
        return this.dataType.equals(other.dataType);
    }

    /**
     * データ型の名前を取得
     * 
     * @return データ型の名前
     */
    public String getTypeName() {
        if (dataType == PersistentDataType.INTEGER) return "Integer";
        if (dataType == PersistentDataType.STRING) return "String";
        if (dataType == PersistentDataType.DOUBLE) return "Double";
        if (dataType == PersistentDataType.FLOAT) return "Float";
        if (dataType == PersistentDataType.LONG) return "Long";
        if (dataType == PersistentDataType.BYTE) return "Byte";
        if (dataType == PersistentDataType.BYTE_ARRAY) return "ByteArray";
        if (dataType == PersistentDataType.INTEGER_ARRAY) return "IntegerArray";
        if (dataType == PersistentDataType.LONG_ARRAY) return "LongArray";
        return "Unknown";
    }

    /**
     * コンポーネントのコピーを作成
     * 
     * @return コピーされたコンポーネント
     */
    public CustomDataComponent<T> copy() {
        // 配列型の場合は新しい配列を作成
        if (value instanceof byte[]) {
            byte[] original = (byte[]) value;
            @SuppressWarnings("unchecked")
            T copied = (T) original.clone();
            return new CustomDataComponent<>(copied, dataType);
        } else if (value instanceof int[]) {
            int[] original = (int[]) value;
            @SuppressWarnings("unchecked")
            T copied = (T) original.clone();
            return new CustomDataComponent<>(copied, dataType);
        } else if (value instanceof long[]) {
            long[] original = (long[]) value;
            @SuppressWarnings("unchecked")
            T copied = (T) original.clone();
            return new CustomDataComponent<>(copied, dataType);
        }
        
        // プリミティブ型やStringはイミュータブルなのでそのまま
        return new CustomDataComponent<>(value, dataType);
    }

    @Override
    public String toString() {
        return "CustomDataComponent{" +
                "type=" + getTypeName() +
                ", value=" + (value instanceof byte[] ? "byte[" + ((byte[])value).length + "]" :
                             value instanceof int[] ? "int[" + ((int[])value).length + "]" :
                             value instanceof long[] ? "long[" + ((long[])value).length + "]" :
                             value) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomDataComponent<?> that = (CustomDataComponent<?>) o;
        
        // 配列型の比較
        if (value instanceof byte[] && that.value instanceof byte[]) {
            return java.util.Arrays.equals((byte[]) value, (byte[]) that.value) &&
                   dataType.equals(that.dataType);
        } else if (value instanceof int[] && that.value instanceof int[]) {
            return java.util.Arrays.equals((int[]) value, (int[]) that.value) &&
                   dataType.equals(that.dataType);
        } else if (value instanceof long[] && that.value instanceof long[]) {
            return java.util.Arrays.equals((long[]) value, (long[]) that.value) &&
                   dataType.equals(that.dataType);
        }
        
        return Objects.equals(value, that.value) &&
               Objects.equals(dataType, that.dataType);
    }

    @Override
    public int hashCode() {
        // 配列型のハッシュコード
        if (value instanceof byte[]) {
            return Objects.hash(java.util.Arrays.hashCode((byte[]) value), dataType);
        } else if (value instanceof int[]) {
            return Objects.hash(java.util.Arrays.hashCode((int[]) value), dataType);
        } else if (value instanceof long[]) {
            return Objects.hash(java.util.Arrays.hashCode((long[]) value), dataType);
        }
        
        return Objects.hash(value, dataType);
    }
}