package com.teaman.attributecompatible.common.compatible.pxrpg;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.UUID;

/**
 * Author: Teaman
 * Date: 2022/2/8 1:28
 */
public class ExtraAttributeCache {

    public static ExtraAttributeCache INSTANCE;

    public ExtraAttributeCache() {
        ATTRIBUTE_CACHE = Maps.newConcurrentMap();
        INSTANCE = this;
    }

    private final Map<String, Table<UUID, String, Double>> ATTRIBUTE_CACHE;

    public void addExtraAttributeData(String c, LivingEntity livingEntity, String index, double num) {
        if (!ATTRIBUTE_CACHE.containsKey(c)) {
            Table<UUID, String, Double> data = HashBasedTable.create();
            ATTRIBUTE_CACHE.put(c, data);
        }
        UUID uuid = livingEntity.getUniqueId();
        Table<UUID, String, Double> cacheTable = ATTRIBUTE_CACHE.get(c);
        if (cacheTable.contains(uuid, index)) {
            double value = cacheTable.get(uuid, index);
            value = value + num;
            cacheTable.put(uuid, index, value);
            return;
        }
        cacheTable.put(uuid, index, num);
    }

    public Double getExtraAttributeData(String c, LivingEntity livingEntity, String index) {
        double var = 0.0;
        if (!ATTRIBUTE_CACHE.containsKey(c)) {
            return var;
        }
        UUID uuid = livingEntity.getUniqueId();
        Table<UUID, String, Double> cacheData = ATTRIBUTE_CACHE.get(c);
        if (cacheData.contains(uuid, index)) {
            var = cacheData.get(uuid, index);
            return var;
        }
        return var;
    }

    public void removeExtraAttributeData(String c, LivingEntity livingEntity) {
        if (!ATTRIBUTE_CACHE.containsKey(c)) {
            return;
        }
        UUID uuid = livingEntity.getUniqueId();
        Table<UUID, String, Double> cacheData = ATTRIBUTE_CACHE.get(c);
        cacheData.rowMap().remove(uuid);
    }

    public Double getExtraAttributeTotal(LivingEntity livingEntity, String index) {
        UUID uuid = livingEntity.getUniqueId();
        return ATTRIBUTE_CACHE.values().stream()
                .filter(cacheData -> cacheData.contains(uuid, index))
                .mapToDouble(cacheData -> cacheData.get(uuid, index))
                .sum();
    }

    public void releaseExtraAttributeData(LivingEntity livingEntity) {
        UUID uuid = livingEntity.getUniqueId();
        ATTRIBUTE_CACHE.values().forEach(cacheData -> {
            cacheData.rowMap().remove(uuid);
        });
    }
}
