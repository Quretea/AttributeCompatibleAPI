package com.teaman.attributecompatible.common.listener;

import com.teaman.attributecompatible.common.AttributeCompatiblePlugin;
import com.teaman.attributecompatible.common.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

/**
 * Author: Teaman
 * Date: 2021/10/8 13:34
 */
public class CompatibleListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEnable(PluginEnableEvent event) {
        if (AttributeCompatiblePlugin.inst().getDescription().getSoftDepend().contains(event.getPlugin().getName())) {
            final String name = "AttributeCompatibleAPI";
            Utils.println(name, "AttributeCompatibleAPI正在兼容~~");
            Utils.println(name, "作者：清茶");
            Utils.println(name, "版本："+ AttributeCompatiblePlugin.inst().getDescription().getVersion());
            if (!AttributeCompatiblePlugin.forceCheckCompatiblePlugin()) {
                AttributeCompatiblePlugin.autoCheckCompatiblePlugin();
            }
            String compatiblePluginName = event.getPlugin().getName();
            String compatiblePluginVersion = event.getPlugin().getDescription().getVersion();
            AttributeCompatiblePlugin.setCompatiblePluginName(compatiblePluginName);
            AttributeCompatiblePlugin.setCompatiblePluginVersion(compatiblePluginVersion);
            Utils.println(name, compatiblePluginName + " " + compatiblePluginVersion + "版本 成功兼容~");
            Utils.println(name, "感谢使用AttributeCompatibleAPI~~");
            HandlerList.unregisterAll(this);
        }
    }
}
