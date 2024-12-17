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
public class MirrorDataSource {

    private final Map<UUID, Map<String, AttributeHolder>> sourceMirror;
    private final Map<UUID, Queue<MirrorDataOperator>> opCache;

    private final String pluginName;

    public static MirrorDataSource INSTANCE;

    public MirrorDataSource(Plugin plugin){
        this.sourceMirror = Maps.newHashMap();
        this.opCache = Maps.newHashMap();
        this.pluginName = plugin.getName();
        INSTANCE = this;
    }

    public void addAttributeAddOperator(UUID uuid, String key, AttributeHolder attributeHolder){
        @Nullable Queue<MirrorDataOperator> operators = this.opCache.get(uuid);
        if (operators == null){
            operators = new LinkedList<>();
            opCache.put(uuid, operators);
        }
        operators.offer(MirrorDataOperator.createAddOperation(createIdentifierKey(key), attributeHolder));
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

    public void addSourceMirrorData(UUID uuid, String identifier, AttributeHolder holder){
        @NotNull Map<String, AttributeHolder> mirror = this.sourceMirror.computeIfAbsent(uuid, k -> new HashMap<>());
        mirror.put(identifier, holder);
    }

    public void removeSourceMirrorData(UUID uuid, String identifier){
        @Nullable Map<String, AttributeHolder> mirror = this.sourceMirror.get(uuid);
        if (mirror != null){
            mirror.remove(identifier);
        }
    }

    public @Nullable Object readSourceMirrorData(UUID uuid, String key){
        @Nullable Map<String, AttributeHolder> mirror = this.sourceMirror.get(uuid);
        if (mirror == null) return null;
        @Nullable AttributeHolder holder = mirror.get(createIdentifierKey(key));
        if (holder == null) return null;
        return holder.getData();
    }

    public @NotNull Set<AttributeHolder> getAllSourceMirrorData(UUID uuid){
        Set<AttributeHolder> set = new HashSet<>();
        @Nullable Map<String, AttributeHolder> mirror = this.sourceMirror.get(uuid);
        if (mirror == null) return set;
        set.addAll(mirror.values());
        return set;
    }

    protected void release(UUID uuid){
        sourceMirror.remove(uuid);
        opCache.remove(uuid);
    }


    public String createIdentifierKey(@NotNull String index){
        return pluginName + "$" + index;
    }


}
