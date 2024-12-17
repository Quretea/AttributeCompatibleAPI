package com.teaman.attributecompatible.common.compatible.attributeplus;

import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.MirrorDataContainer;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataHolder;
import com.teaman.attributecompatible.common.data.MirrorDataManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.serverct.ersha.jd.AttributeAPI;
import org.serverct.ersha.jd.api.EntityAttributeAPI;
import org.serverct.ersha.jd.attribute.AttributeManager;

import java.util.List;
import java.util.Queue;

/**
 * Author: Teaman
 * Date: 2021/9/28 13:14
 */
public class ApCompatibleFor2 implements ICompatible {

    @Override
    public Object getAttributeSource(Plugin plugin, LivingEntity livingEntity, String index) {
        @Nullable MirrorDataHolder mirror = MirrorDataManager.INSTANCE.getMirrorDataHolder(plugin);
        if (mirror == null){
            return null;
        }
        return mirror.readMirrorDataSource(livingEntity.getUniqueId(), index);
    }

    @Override
    public void addAttributeSource(Plugin plugin , LivingEntity livingEntity, String index, ItemStack itemStack) {
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
    public void removeAttributeSource(Plugin plugin, LivingEntity livingEntity, String index) {
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
        while ((operator = queue.poll()) != null) {
            MirrorDataContainer holder = operator.getHolder();
            String id = operator.getIdentifierKey();
            if (operator.isAddOperation() && holder != null){
                List<String> stringList;
                @Nullable ItemStack itemStack = holder.getItemStack();
                if (itemStack != null){
                    stringList = transfer(itemStack);
                }else {
                    stringList = holder.getStringList();
                }
                if (stringList == null || stringList.isEmpty()) continue;
                addSourceAttribute(livingEntity, id, stringList);
                mirror.addMirrorDataContainer(livingEntity.getUniqueId(), id, holder);
            }else {
                mirror.removeMirrorDataContainer(livingEntity.getUniqueId(), id);
                takeSourceAttribute(livingEntity, id);
            }
        }
    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        AttributeManager.getInstance().updateAttribute(livingEntity);
    }

    private @Nullable List<String> transfer(@NotNull ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null){
            return null;
        }
        return meta.getLore();
    }

    private void addSourceAttribute(LivingEntity livingEntity, String key, List<String> attr){
        if (livingEntity instanceof Player){
            AttributeAPI.addAttribute((Player) livingEntity, key, attr);
        }else {
            EntityAttributeAPI.addEntityAttribute(livingEntity, key, attr);
        }
    }

    private void takeSourceAttribute(LivingEntity livingEntity, String key){
        if (livingEntity instanceof Player){
            AttributeAPI.deleteAttribute((Player) livingEntity, key);

        }else {
            EntityAttributeAPI.removeEntityAttribute(livingEntity, key);
        }
    }
}
