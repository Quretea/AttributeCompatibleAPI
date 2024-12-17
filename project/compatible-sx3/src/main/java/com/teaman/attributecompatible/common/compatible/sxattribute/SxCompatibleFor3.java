package com.teaman.attributecompatible.common.compatible.sxattribute;

import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.AttributeHolder;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataSource;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import github.saukiya.sxattribute.SXAttribute;
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
public class SxCompatibleFor3 implements ICompatible {

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

        if (itemStack == null || itemStack.getData().getItemType().equals(Material.AIR)) {
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
        boolean flag = false;
        while ((operator = queue.poll()) != null) {
            flag = true;
            AttributeHolder holder = operator.getHolder();
            String id = operator.getIdentifierKey();
            if (operator.isAddOperation() && holder != null){
                @Nullable ItemStack itemStack = holder.getItemStack();
                if (itemStack != null){
                    mirror.addSourceMirrorData(livingEntity.getUniqueId(), id, holder);
                    continue;
                }
                List<String> stringList = holder.getStringList();
                if (stringList != null) {
                    mirror.addSourceMirrorData(livingEntity.getUniqueId(), id, holder);
                }
            }else {
                mirror.removeSourceMirrorData(livingEntity.getUniqueId(), id);
            }
        }
        if (flag){
            SXAttribute.getApi().setEntityAPIData(plugin.getClass(), livingEntity.getUniqueId(),
                    transfer(mirror.getAllSourceMirrorData(livingEntity.getUniqueId()), livingEntity));
        }
    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        SXAttribute.getApi().attributeUpdate(livingEntity);
    }

    private SXAttributeData transfer(Set<AttributeHolder> set, LivingEntity livingEntity){
        SXAttributeData sxAttributeData = new SXAttributeData();
        for (AttributeHolder holder : set){
            @Nullable ItemStack itemStack = holder.getItemStack();
            if (itemStack != null){
                sxAttributeData.add(SXAttribute.getApi().loadItemData(livingEntity, itemStack));
                continue;
            }
            List<String> stringList = holder.getStringList();
            if (stringList == null) continue;
            sxAttributeData.add(SXAttribute.getApi().loadListData(stringList));
        }
        return sxAttributeData;
    }
}
