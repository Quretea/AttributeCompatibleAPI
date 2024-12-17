package com.teaman.attributecompatible.common.compatible.attributesystem;

import com.skillw.attsystem.AttributeSystem;
import com.skillw.attsystem.api.AttrAPI;
import com.skillw.attsystem.api.attribute.compound.AttributeData;
import com.skillw.attsystem.api.attribute.compound.AttributeDataCompound;
import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.AttributeHolder;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataSource;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Queue;

/**
 * Author: Teaman
 * Date: 2021/9/28 14:09
 */
public class AsCompatible implements ICompatible {

    @Override
    public @Nullable Object getAttributeSource(Plugin plugin, LivingEntity livingEntity, String index) {
        @Nullable MirrorDataSource mirror = SourceDataManager.INSTANCE.getMirrorDataSource(plugin);
        if (mirror == null){
            return null;
        }
        return mirror.readSourceMirrorData(livingEntity.getUniqueId(), index);
    }

    @Override
    public void addAttributeSource(Plugin plugin, LivingEntity livingEntity, String index, ItemStack itemStack) {
        @Nullable MirrorDataSource mirror = SourceDataManager.INSTANCE.getMirrorDataSource(plugin);
        if (mirror == null){
            return;
        }

        if (itemStack==null || itemStack.getData().getItemType().equals(Material.AIR)) {
            return;
        }
        mirror.addAttributeAddOperator(livingEntity.getUniqueId(), index, new AttributeHolder(itemStack));
    }

    @Override
    public void addAttributeSource(Plugin plugin, LivingEntity livingEntity, String index, List<String> attr) {
        @Nullable MirrorDataSource mirror = SourceDataManager.INSTANCE.getMirrorDataSource(plugin);
        if (mirror == null){
            return;
        }
        mirror.addAttributeAddOperator(livingEntity.getUniqueId(), index, new AttributeHolder(attr));
    }

    @Override
    public void removeAttributeSource(Plugin plugin, LivingEntity livingEntity,String index) {
        @Nullable MirrorDataSource mirror = SourceDataManager.INSTANCE.getMirrorDataSource(plugin);
        if (mirror == null){
            return;
        }
        mirror.addAttributeRemoveOperator(livingEntity.getUniqueId(), index);
    }


    @Override
    public void mergeAttributeSource(Plugin plugin, LivingEntity livingEntity) {
        @Nullable MirrorDataSource mirror = SourceDataManager.INSTANCE.getMirrorDataSource(plugin);
        if (mirror == null){
            return;
        }
        Queue<MirrorDataOperator> queue = mirror.getAttributeOperatorQueue(livingEntity.getUniqueId());
        MirrorDataOperator operator;
        while ((operator = queue.poll()) != null) {
            AttributeHolder holder = operator.getHolder();
            String id = operator.getIdentifierKey();
            if (operator.isAddOperation() && holder != null){
                @Nullable ItemStack itemStack = holder.getItemStack();
                if (itemStack != null){
                    AttributeDataCompound dataCompound = AttrAPI.readItem(itemStack,livingEntity,null);
                    AttrAPI.addAttribute(livingEntity, id, dataCompound.toAttributeData(),false);
                    mirror.addSourceMirrorData(livingEntity.getUniqueId(), id, holder);
                    continue;
                }
                List<String> stringList = holder.getStringList();
                if (stringList != null) {
                    AttributeData data = AttrAPI.read(stringList,livingEntity,null);
                    AttrAPI.addAttribute(livingEntity, id, data,false);
                    mirror.addSourceMirrorData(livingEntity.getUniqueId(), id, holder);
                }
            }else {
                mirror.removeSourceMirrorData(livingEntity.getUniqueId(), id);
                AttrAPI.removeAttribute(livingEntity, id);
            }
        }
    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        AttributeSystem.getAttributeDataManager().update(livingEntity);
    }
}
