package com.teaman.attributecompatible.common.compatible.pxrpg;

import com.pxpmc.pxrpg.api.Module;
import com.pxpmc.pxrpg.api.adapter.AdapterItemStack;
import com.pxpmc.pxrpg.api.attributeitem.ItemAttributeCompatible;
import com.pxpmc.pxrpg.api.modules.attribute.AttributeManager;
import com.pxpmc.pxrpg.api.modules.attribute.AttributeModule;
import com.pxpmc.pxrpg.api.modules.equip.EquipInter;
import com.pxpmc.pxrpg.api.modules.equip.EquipManager;
import com.pxpmc.pxrpg.api.modules.equip.EquipModule;
import com.teaman.attributecompatible.common.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Author Teaman
 * Date 2024/9/15 20:31
 */
public class PxRpgAdapter {

    private final AttributeManager attributeManager;
    private final EquipManager equipManager;
    private final ItemAttributeCompatible compatible;

    public PxRpgAdapter() {
        this.attributeManager = Module.getModule(AttributeModule.class).getAttributeManager();
        this.equipManager = Module.getModule(EquipModule.class).getEquipManager();
        this.compatible = this.equipManager.getCompatibleManager().getCompatible(ItemAttributeCompatible.class);
        if (attributeManager == null) Utils.println("AttributeCompatibleAPI", "§4无法检测到AttributeModule，请检查PxRpg~");
        if (equipManager == null) Utils.println("AttributeCompatibleAPI", "§4无法检测到EquipModule，请检查PxRpg~");
        if (compatible == null) Utils.println("AttributeCompatibleAPI", "§4无法检测到ItemAttributeCompatible，请检查PxRpg~");
        INSTANCE = this;
    }

    public static PxRpgAdapter INSTANCE;

    public static boolean isUnhooked() {
        return INSTANCE == null;
    }

    public Collection<String> getAttrKeys(){
        return attributeManager == null ? new ArrayList<>() : attributeManager.getKeys();
    }

    public boolean isEquipInter(AdapterItemStack itemStack){
        return equipManager != null && equipManager.isThat(itemStack);
    }

    public EquipInter toEquipInter(AdapterItemStack itemStack){
        return equipManager.toThat(itemStack);
    }

    public double getAttributeNum(EquipInter equipInter, String id, boolean flag1, boolean flag2){
        return compatible == null ? 0.0D : compatible.getAttribute(equipInter, id, flag1, flag2);
    }

}
