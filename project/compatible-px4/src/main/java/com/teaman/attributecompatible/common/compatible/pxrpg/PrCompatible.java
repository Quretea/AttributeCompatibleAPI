package com.teaman.attributecompatible.common.compatible.pxrpg;

import com.pxpmc.pxrpg.api.MAPI;
import com.pxpmc.pxrpg.api.Module;
import com.pxpmc.pxrpg.api.adapter.AdapterItemStack;
import com.pxpmc.pxrpg.api.adapter.AdapterPlayer;
import com.pxpmc.pxrpg.api.modules.equip.EquipInter;
import com.pxpmc.pxrpg.api.modules.suit.PlayerSuit;
import com.pxpmc.pxrpg.api.modules.suit.SuitModule;
import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.AttributeHolder;
import com.teaman.attributecompatible.common.data.MirrorDataOperator;
import com.teaman.attributecompatible.common.data.MirrorDataSource;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 * Author: Teaman
 * Date: 2021/9/28 15:23
 */


public class PrCompatible implements ICompatible {

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
        if (livingEntity instanceof Player) {
            @Nullable MirrorDataSource mirror = SourceDataManager.INSTANCE.getMirrorDataSource(plugin);
            if (mirror == null){
                return;
            }
            Queue<MirrorDataOperator> queue = mirror.getAttributeOperatorQueue(livingEntity.getUniqueId());
            boolean flag = false;
            MirrorDataOperator operator;
            while ((operator = queue.poll()) != null) {
                flag = true;
                AttributeHolder holder = operator.getHolder();
                String id = operator.getIdentifierKey();
                if (operator.isAddOperation() && holder != null){
                    @Nullable ItemStack itemStack = holder.getItemStack();
                    if (itemStack != null){
                        mirror.addSourceMirrorData(livingEntity.getUniqueId(), id, holder);
                        cacheItemAttr(livingEntity, id, itemStack);
                        continue;
                    }
                    List<String> stringList = holder.getStringList();
                    if (stringList != null) {
                        mirror.addSourceMirrorData(livingEntity.getUniqueId(), id, holder);
                        cacheListAttr(livingEntity, id, stringList);
                    }
                }else {
                    mirror.removeSourceMirrorData(livingEntity.getUniqueId(), id);
                    ExtraAttributeCache.INSTANCE.removeExtraAttributeData(id, livingEntity);
                }
            }
            if (flag){
                AdapterPlayer adapterPlayer = MAPI.getBukkitPxRpgAPI().toPxRpgPlayer((Player) livingEntity);
                PlayerSuit suit = Module.getModule(SuitModule.class).getPlayerSuit(adapterPlayer);
                if (suit != null) {
                    suit.refresh();
                }
                adapterPlayer.update();
            }
        }
        //if (Module.getModule(MobModule.class).getMobDataManager().hasMobData(livingEntity.getUniqueId())) {
        //    MobObject mobObject = Module.getModule(MobModule.class).getMobDataManager().getMobData(livingEntity.getUniqueId()).getMob();
        //    MobDataConfig mobDataConfig = Module.getModule(MobModule.class).getMobDataManager().getMobData(livingEntity.getUniqueId()).getMobDataConfig();
        //    Module.getModule(MobModule.class).getMobDataManager().spawnMobData(mobDataConfig, mobObject);
        //}
    }

    @Override
    public void refreshAttribute(LivingEntity livingEntity) {
        AdapterPlayer adapterPlayer = MAPI.getBukkitPxRpgAPI().toPxRpgPlayer((Player) livingEntity);
        PlayerSuit suit = Module.getModule(SuitModule.class).getPlayerSuit(adapterPlayer);
        if (suit != null) {
            suit.refresh();
        }
        adapterPlayer.update();
    }

    private void cacheListAttr(LivingEntity livingEntity, String index, List<String> list){
        if (list == null || index == null || PxRpgAdapter.isUnhooked()){
            return;
        }
        Collection<String> attrKeys = PxRpgAdapter.INSTANCE.getAttrKeys();
        for (String str : list) {
            String[] s = str.split("<->");
            if (s.length != 4 || !attrKeys.contains(s[0])) {
                continue;
            }
            String attrId = s[0];
            String flag = s[3];
            if (!("true".equalsIgnoreCase(flag) || "false".equalsIgnoreCase(flag))){
                break;
            }
            boolean sign = Boolean.parseBoolean(flag);
            double min = Double.parseDouble(s[1]);
            double max = Double.parseDouble(s[2]);
            ExtraAttributeCache.INSTANCE.addExtraAttributeData(index, livingEntity,attrId+"@"+sign+"@true",min);
            ExtraAttributeCache.INSTANCE.addExtraAttributeData(index, livingEntity,attrId+"@"+sign+"@false",max);
        }
    }

    private void cacheItemAttr(LivingEntity livingEntity, String index, ItemStack item){
        if (item == null || index == null || PxRpgAdapter.isUnhooked()){
            return;
        }
        AdapterItemStack itemStack = MAPI.getBukkitPxRpgAPI().toPxRpgItemStack(item);
        if (PxRpgAdapter.INSTANCE.isEquipInter(itemStack)) {
            EquipInter equipInter = PxRpgAdapter.INSTANCE.toEquipInter(itemStack);
            for (String id : PxRpgAdapter.INSTANCE.getAttrKeys()){
                ExtraAttributeCache.INSTANCE.addExtraAttributeData(index, livingEntity,id+"@true@true",PxRpgAdapter.INSTANCE.getAttributeNum(equipInter, id, true, true));
                ExtraAttributeCache.INSTANCE.addExtraAttributeData(index, livingEntity,id+"@true@false",PxRpgAdapter.INSTANCE.getAttributeNum(equipInter, id, true, false));
                ExtraAttributeCache.INSTANCE.addExtraAttributeData(index, livingEntity,id+"@false@true",PxRpgAdapter.INSTANCE.getAttributeNum(equipInter, id, false, true));
                ExtraAttributeCache.INSTANCE.addExtraAttributeData(index, livingEntity,id+"@false@false",PxRpgAdapter.INSTANCE.getAttributeNum(equipInter, id, false, false));
            }
        }
    }
}
