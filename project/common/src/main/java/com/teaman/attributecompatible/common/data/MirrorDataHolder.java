package com.teaman.attributecompatible.common.data;

import com.google.common.collect.Maps;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Author: Teaman
 * Date: 2021/9/30 23:26
 */
public class MirrorDataHolder {

    private final Map<UUID, Map<String, MirrorDataContainer>> mirrorContainers;
    private final Map<UUID, Queue<MirrorDataOperator>> opCache;

    private final String pluginName;

    public static MirrorDataHolder INSTANCE;

    public MirrorDataHolder(Plugin plugin){
        this.mirrorContainers = Maps.newHashMap();
        this.opCache = Maps.newHashMap();
        this.pluginName = plugin.getName();
        INSTANCE = this;
    }

    public void addAttributeAddOperator(UUID uuid, String key, MirrorDataContainer mirrorDataContainer){
        @Nullable Queue<MirrorDataOperator> operators = this.opCache.get(uuid);
        if (operators == null){
            operators = new LinkedList<>();
            opCache.put(uuid, operators);
        }
        operators.offer(MirrorDataOperator.createAddOperation(createIdentifierKey(key), mirrorDataContainer));
    }

    public void addAttributeRemoveOperator(UUID uuid, String key){
        @Nullable Queue<MirrorDataOperator> operators = this.opCache.get(uuid);
        if (operators == null){
            operators = new LinkedList<>();
            opCache.put(uuid, operators);
        }
        operators.offer(MirrorDataOperator.createRemoveOperation(createIdentifierKey(key)));
    }

    public @NotNull Queue<MirrorDataOperator> getAttributeOperatorQueue(UUID uuid){
        @Nullable Queue<MirrorDataOperator> operators = this.opCache.get(uuid);
        if (operators == null){
            operators = new LinkedList<>();
            opCache.put(uuid, operators);
        }
        return operators;
    }

    public void addMirrorDataContainer(UUID uuid, String identifier, MirrorDataContainer holder){
        @NotNull Map<String, MirrorDataContainer> mirror = this.mirrorContainers.computeIfAbsent(uuid, k -> new HashMap<>());
        mirror.put(identifier, holder);
    }

    public void removeMirrorDataContainer(UUID uuid, String identifier){
        @Nullable Map<String, MirrorDataContainer> mirror = this.mirrorContainers.get(uuid);
        if (mirror != null){
            mirror.remove(identifier);
        }
    }

    public @Nullable Object readMirrorDataSource(UUID uuid, String key){
        @Nullable Map<String, MirrorDataContainer> mirror = this.mirrorContainers.get(uuid);
        if (mirror == null) return null;
        @Nullable MirrorDataContainer holder = mirror.get(createIdentifierKey(key));
        if (holder == null) return null;
        return holder.getData();
    }

    public @NotNull Set<MirrorDataContainer> getAllMirrorDataContainer(UUID uuid){
        Set<MirrorDataContainer> set = new HashSet<>();
        @Nullable Map<String, MirrorDataContainer> mirror = this.mirrorContainers.get(uuid);
        if (mirror == null) return set;
        set.addAll(mirror.values());
        return set;
    }

    protected void release(UUID uuid){
        mirrorContainers.remove(uuid);
        opCache.remove(uuid);
    }


    public String createIdentifierKey(@NotNull String index){
        return pluginName + "$" + index;
    }


}
