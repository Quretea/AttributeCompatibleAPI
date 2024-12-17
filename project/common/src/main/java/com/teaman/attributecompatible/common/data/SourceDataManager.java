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
public class SourceDataManager {

    public static SourceDataManager INSTANCE;

    public SourceDataManager() {
        INSTANCE = this;
    }

    private final Map<Plugin, MirrorDataSource> ATTRIBUTE_DATA = Maps.newConcurrentMap();

    public void registerSourcePlugin(Plugin plugin) {
        ATTRIBUTE_DATA.put(plugin, new MirrorDataSource(plugin));
        Utils.println("AttributeCompatibleAPI",plugin.getName()+"-"+plugin.getDescription().getVersion()+" 已注册为源数据来源!!");
    }

    public List<Plugin> getRegisteredSourcePlugin() {
        return new ArrayList<>(ATTRIBUTE_DATA.keySet());
    }


    public @Nullable MirrorDataSource getMirrorDataSource(Plugin plugin){
        return ATTRIBUTE_DATA.get(plugin);
    }


    public void releaseMirrorDataSource(LivingEntity livingEntity) {
        UUID uuid = livingEntity.getUniqueId();
        ATTRIBUTE_DATA.values().forEach(dataSource -> {
            dataSource.release(uuid);
        });
    }


}
