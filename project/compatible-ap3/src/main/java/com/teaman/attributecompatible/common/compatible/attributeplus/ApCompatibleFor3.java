package com.teaman.attributecompatible.common.compatible.attributeplus;

import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.MirrorDataContainer;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataHolder;
import com.teaman.attributecompatible.common.data.MirrorDataManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.serverct.ersha.api.AttributeAPI;
import org.serverct.ersha.attribute.data.AttributeData;
import org.serverct.ersha.attribute.data.AttributeSource;

import java.util.List;
import java.util.Queue;

/**
 * Author: Teaman
 * Date: 2021/9/28 13:15
 */
public class ApCompatibleFor3 implements ICompatible {

    @Override
    public @Nullable Object getAttributeSource(Plugin plugin, LivingEntity livingEntity, String index) {
        @Nullable MirrorDataHolder mirror = MirrorDataManager.INSTANCE.getMirrorDataHolder(plugin);
        if (mirror == null){
            return null;
        }
        return mirror.readMirrorDataSource(livingEntity.getUniqueId(), index);
    }

    @Override
    public void addAttributeSource(Plugin plugin, LivingEntity livingEntity, String index, ItemStack itemStack) {
        @Nullable MirrorDataHolder mirror = MirrorDataManager.INSTANCE.getMirrorDataHolder(plugin);
        if (mirror == null){
            return;
        }

        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        mirror.addAttributeAddOperator(livingEntity.getUniqueId(), index, new MirrorDataContainer(itemStack));
    }

    @Override
    public void addAttributeSource(Plugin plugin, LivingEntity livingEntity, String index, List<String> attr) {
        @Nullable MirrorDataHolder mirror = MirrorDataManager.INSTANCE.getMirrorDataHolder(plugin);
        if (mirror == null){
            return;
        }
        mirror.addAttributeAddOperator(livingEntity.getUniqueId(), index, new MirrorDataContainer(attr));
    }

    @Override
    public void removeAttributeSource(Plugin plugin, LivingEntity livingEntity,String index) {
        @Nullable MirrorDataHolder mirror = MirrorDataManager.INSTANCE.getMirrorDataHolder(plugin);
        if (mirror == null){
            return;
        }
        mirror.addAttributeRemoveOperator(livingEntity.getUniqueId(), index);
    }


    @Override
    public void mergeAttributeSource(Plugin plugin, LivingEntity livingEntity) {
        @Nullable MirrorDataHolder mirror = MirrorDataManager.INSTANCE.getMirrorDataHolder(plugin);
        if (mirror == null){
            return;
        }
        AttributeData data = AttributeAPI.getAttrData(livingEntity);
        Queue<MirrorDataOperator> queue = mirror.getAttributeOperatorQueue(livingEntity.getUniqueId());
        MirrorDataOperator operator;
        //boolean flag = false;
        while ((operator = queue.poll()) != null) {
            //flag = true;
            MirrorDataContainer holder = operator.getHolder();
            String id = operator.getIdentifierKey();
            if (operator.isAddOperation() && holder != null){
                @Nullable ItemStack itemStack = holder.getItemStack();
                if (itemStack != null){
                    mirror.addMirrorDataContainer(livingEntity.getUniqueId(), id, holder);
                    AttributeAPI.addSourceAttribute(data, id, new AttributeSource(livingEntity,itemStack,false));
                    continue;
                }
                List<String> stringList = holder.getStringList();
                if (stringList != null) {
                    mirror.addMirrorDataContainer(livingEntity.getUniqueId(), id, holder);
                    AttributeAPI.addSourceAttribute(data, id, new AttributeSource(livingEntity,stringList,false));
                }
            }else {
                mirror.removeMirrorDataContainer(livingEntity.getUniqueId(), id);
                AttributeAPI.takeSourceAttribute(data, id);
                AttributeAPI.updateAttribute(livingEntity);
            }
        }
        //if (flag) AttributeAPI.updateAttribute(livingEntity);
    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        AttributeAPI.updateAttribute(livingEntity);
    }
}
