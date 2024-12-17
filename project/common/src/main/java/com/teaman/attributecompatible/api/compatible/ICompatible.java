package com.teaman.attributecompatible.api.compatible;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Author: Teaman
 * Date: 2021/9/28 13:05
 */
public interface ICompatible{
    /**
     * 获取实体对应索引的源数据
     * @param plugin       注册过的源数据操作插件
     * @param livingEntity 进行源数据操作的实体
     * @param index        源数据对应的索引
     * @return 对应索引的源数据 List<String>或ItemStack;
     */
    @Nullable Object getAttributeSource(Plugin plugin, LivingEntity livingEntity, String index);

    /**
     * 添加实体对应索引的源数据
     * 对应索引的源数据如果已存在，执行覆盖
     * @param plugin       注册过的源数据操作插件
     * @param livingEntity 进行源数据操作的实体
     * @param index        源数据对应的索引
     * @param itemStack    ItemStack形式的源数据
     */
    void addAttributeSource(Plugin plugin, LivingEntity livingEntity, String index, ItemStack itemStack);

    /**
     * 添加实体对应索引的源数据
     * 对应索引的源数据如果已存在，执行覆盖
     * @param plugin       注册过的源数据操作插件
     * @param livingEntity 进行源数据操作的实体
     * @param index        源数据对应的索引
     * @param attr         List<String>形式的源数据
     */
    void addAttributeSource(Plugin plugin, LivingEntity livingEntity, String index, List<String> attr);

    /**
     * 添加实体对应索引的源数据
     * 对应索引的源数据如果已存在，执行覆盖
     * @deprecated
     * @param plugin       注册过的源数据操作插件
     * @param livingEntity 进行源数据操作的实体
     * @param index        源数据对应的索引
     * @param itemStack    ItemStack形式的源数据
     */
    @Deprecated
    default void addAttributeFromItem(Plugin plugin, LivingEntity livingEntity, String index, ItemStack itemStack){
        addAttributeSource(plugin, livingEntity, index,  itemStack);
    }

    /**
     * 添加实体对应索引的源数据
     * 对应索引的源数据如果已存在，执行覆盖
     * @deprecated
     * @param plugin       注册过的源数据操作插件
     * @param livingEntity 进行源数据操作的实体
     * @param index        源数据对应的索引
     * @param attr         List<String>形式的源数据
     */
    @Deprecated
    default void addAttributeFromString(Plugin plugin, LivingEntity livingEntity, String index, List<String> attr){
        addAttributeSource(plugin, livingEntity, index,  attr);
    }
    /**
     * 移除实体对应索引的源数据
     * @param plugin       注册过的源数据操作插件
     * @param livingEntity 进行源数据操作的实体
     * @param index        源数据对应的索引
     */
    void removeAttributeSource(Plugin plugin,LivingEntity livingEntity,String index);

    /**
     * 从实体的源数据给予实体对应属性(无需二次刷新)
     * @param plugin       注册过的源数据操作插件
     * @param livingEntity 进行源数据操作的实体
     */
    void mergeAttributeSource(Plugin plugin, LivingEntity livingEntity);

    /**
     * 尝试刷新对应属性插件的实体的全部属性
     * @param livingEntity 实体
     */
    void refreshAttribute(LivingEntity livingEntity);
}
