package com.teaman.attributecompatible.common.compatible.itemloreorigin;

import com.mchim.ItemLoreOrigin.API.AttributeAPI;
import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.MirrorDataContainer;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataHolder;
import com.teaman.attributecompatible.common.data.MirrorDataManager;
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

        if (itemStack==null || itemStack.getData().getItemType().equals(Material.AIR)) {
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
        AttributeAPI.removeItems(plugin.getClass(), livingEntity.getUniqueId());
        Queue<MirrorDataOperator> queue = mirror.getAttributeOperatorQueue(livingEntity.getUniqueId());
        MirrorDataOperator operator;
        while ((operator = queue.poll()) != null) {
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
        AttributeAPI.setItems(plugin.getClass(),livingEntity.getUniqueId(),transfer(mirror.getAllMirrorDataContainer(livingEntity.getUniqueId())));
    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        // 无需刷新 该插件底层为tick自动刷新
    }

    private List<ItemStack> transfer(Set<MirrorDataContainer> set){
        List<ItemStack> result = new ArrayList<>();
        for (MirrorDataContainer holder : set){
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
