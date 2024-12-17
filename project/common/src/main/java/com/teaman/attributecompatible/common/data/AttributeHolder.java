package com.teaman.attributecompatible.common.data;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Author Teaman
 * Date 2024/8/5 9:52
 */
public class AttributeHolder {
    private @Nullable List<String> stringList;
    private @Nullable ItemStack itemStack;

    public AttributeHolder(@NotNull List<String> stringList) {
        this.stringList = stringList;
    }

    public AttributeHolder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @Nullable List<String> getStringList() {
        return stringList;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable Object getData() {
        return itemStack != null ? itemStack : stringList;
    }
}
