package com.teaman.attributecompatible.common.compatible.originattribute;

import ac.github.oa.internal.core.attribute.equip.Slot;
import com.teaman.attributecompatible.common.data.MirrorDataHolder;
import com.teaman.attributecompatible.common.data.MirrorDataManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Author Teaman
 * Date 2024/8/5 13:49
 */
public class CompatibleMirrorSlot extends Slot {
    private final Plugin plugin;
    private final String id;

    public CompatibleMirrorSlot(Plugin plugin, String id) {
        super(new ItemStack(Material.AIR));
        this.plugin = plugin;
        this.id = id;
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Nullable
    @Override
    public ItemStack getItem(@NotNull LivingEntity livingEntity) {
        @Nullable MirrorDataHolder mirror = MirrorDataManager.INSTANCE.getMirrorDataHolder(plugin);
        if (mirror == null){
            return super.getItem();
        }
        Object o = mirror.readMirrorDataSource(livingEntity.getUniqueId(), id);
        if (o instanceof ItemStack){
            return (ItemStack) o;
        }
        return super.getItem();
    }
}
