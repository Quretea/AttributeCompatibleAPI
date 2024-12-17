package com.teaman.attributecompatible.common.data;

import com.google.common.collect.Maps;
import com.teaman.attributecompatible.common.utils.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Teaman
 * Date: 2021/9/28 13:34
 */
public class MirrorDataManager {

    public static MirrorDataManager INSTANCE;

    public MirrorDataManager() {
        INSTANCE = this;
    }

    private final Map<Plugin, MirrorDataHolder> ATTRIBUTE_DATA = Maps.newConcurrentMap();

    public void registerSourcePlugin(Plugin plugin) {
        ATTRIBUTE_DATA.put(plugin, new MirrorDataHolder(plugin));
        Utils.println("AttributeCompatibleAPI",plugin.getName()+"-"+plugin.getDescription().getVersion()+" 已注册为源数据来源!!");
    }

    public List<Plugin> getRegisteredSourcePlugin() {
        return new ArrayList<>(ATTRIBUTE_DATA.keySet());
    }


    public @Nullable MirrorDataHolder getMirrorDataHolder(Plugin plugin){
        return ATTRIBUTE_DATA.get(plugin);
    }


    public void releaseMirrorDataHolder(LivingEntity livingEntity) {
        UUID uuid = livingEntity.getUniqueId();
        ATTRIBUTE_DATA.values().forEach(mirrorDataHolder -> {
            mirrorDataHolder.release(uuid);
        });
    }


}
