package com.teaman.attributecompatible.common.compatible.itemloreorigin;

import com.mchim.ItemLoreOrigin.API.AttributeAPI;
import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.AttributeHolder;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataSource;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Author Teaman
 */
public class IloCompatibleForMchim implements ICompatible {

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
        AttributeAPI.removeItems(plugin.getClass(), livingEntity.getUniqueId());
        Queue<MirrorDataOperator> queue = mirror.getAttributeOperatorQueue(livingEntity.getUniqueId());
        MirrorDataOperator operator;
        while ((operator = queue.poll()) != null) {
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
        AttributeAPI.setItems(plugin.getClass(),livingEntity.getUniqueId(),transfer(mirror.getAllSourceMirrorData(livingEntity.getUniqueId())));
    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        // 无需刷新 该插件底层为tick自动刷新
    }

    private List<ItemStack> transfer(Set<AttributeHolder> set){
        List<ItemStack> result = new ArrayList<>();
        for (AttributeHolder holder : set){
            @Nullable ItemStack itemStack = holder.getItemStack();
            if (itemStack != null){
                result.add(itemStack);
                continue;
            }
            List<String> stringList = holder.getStringList();
            if (stringList == null) continue;
            ItemStack vir = new ItemStack(Material.ARROW);
            ItemMeta meta = vir.getItemMeta();
            meta.setLore(stringList);
            vir.setItemMeta(meta);
            result.add(vir);
        }
        return result;
    }
}
