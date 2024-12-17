package com.teaman.attributecompatible.common.compatible.sxattribute;

import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.MirrorDataContainer;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataHolder;
import com.teaman.attributecompatible.common.data.MirrorDataManager;
import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.api.SXAttributeAPI;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Author: Teaman
 * Date: 2021/9/28 13:15
 */
public class SxCompatibleFor2 implements ICompatible {

    private final SXAttributeAPI api;

    public SxCompatibleFor2() {
        this.api = SXAttribute.getApi();
    }

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

        if (itemStack == null || itemStack.getData().getItemType().equals(Material.AIR)) {
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
        Queue<MirrorDataOperator> queue = mirror.getAttributeOperatorQueue(livingEntity.getUniqueId());
        MirrorDataOperator operator;
        boolean flag = false;
        while ((operator = queue.poll()) != null) {
            flag = true;
            MirrorDataContainer holder = operator.getHolder();
            String id = operator.getIdentifierKey();
            if (operator.isAddOperation() && holder != null){
                @Nullable ItemStack itemStack = holder.getItemStack();
                if (itemStack != null){
                    mirror.addMirrorDataContainer(livingEntity.getUniqueId(), id, holder);
                    continue;
                }
                List<String> stringList = holder.getStringList();
                if (stringList != null) {
                    mirror.addMirrorDataContainer(livingEntity.getUniqueId(), id, holder);
                }
            }else {
                mirror.removeMirrorDataContainer(livingEntity.getUniqueId(), id);
            }
        }
        if (flag){
            this.api.setEntityAPIData(plugin.getClass(), livingEntity.getUniqueId(),
                    transfer(mirror.getAllMirrorDataContainer(livingEntity.getUniqueId()), livingEntity));
        }

    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        this.api.updateStats(livingEntity);
    }

    private SXAttributeData transfer(Set<MirrorDataContainer> set, LivingEntity livingEntity){
        SXAttributeData sxAttributeData = new SXAttributeData();
        for (MirrorDataContainer holder : set){
            @Nullable ItemStack itemStack = holder.getItemStack();
            if (itemStack != null){
                sxAttributeData.add(this.api.getItemData(livingEntity, null, itemStack));
                continue;
            }
            List<String> stringList = holder.getStringList();
            if (stringList == null) continue;
            sxAttributeData.add(this.api.getLoreData(livingEntity, null, stringList));
        }
        return sxAttributeData;
    }
}
