package com.teaman.attributecompatible.common.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Author Teaman
 * Date 2024/8/5 9:40
 */
public class MirrorDataOperator {
    private final @NotNull String key;
    private final @Nullable AttributeHolder holder;
    private final @NotNull Operator operator;

    public static MirrorDataOperator createAddOperation(@NotNull String index, @NotNull AttributeHolder holder){
        return new MirrorDataOperator(index, holder, Operator.ADD);
    }
    public static MirrorDataOperator createRemoveOperation(@NotNull String index){
        return new MirrorDataOperator(index, null, Operator.REMOVE);
    }

    private MirrorDataOperator(@NotNull String key, @Nullable AttributeHolder holder, @NotNull Operator operator) {
        this.operator = operator;
        this.key = key;
        this.holder = holder;
    }

    public @NotNull String getIdentifierKey() {
        return key;
    }

    public @Nullable AttributeHolder getHolder() {
        return holder;
    }

    public boolean isAddOperation() {
        return operator.equals(Operator.ADD);
    }

    public boolean isRemoveOperation() {
        return operator.equals(Operator.REMOVE);
    }

    @Override
    public String toString() {
        return "MirrorDataOperator{" +
                "key='" + key + '\'' +
                ", holder=" + holder +
                ", operator=" + operator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MirrorDataOperator)) return false;
        MirrorDataOperator operator1 = (MirrorDataOperator) o;
        return Objects.equals(getIdentifierKey(), operator1.getIdentifierKey()) && Objects.equals(getHolder(), operator1.getHolder()) && operator == operator1.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifierKey(), getHolder(), operator);
    }

    public enum Operator{
        ADD, REMOVE
    }

}
