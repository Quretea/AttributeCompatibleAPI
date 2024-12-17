package com.teaman.attributecompatible.common.listener;

import com.teaman.attributecompatible.api.event.AttributeLoadOverEvent;
import com.teaman.attributecompatible.api.event.AttributeReadyEvent;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


/**
 * Author: Teaman
 * Date: 2021/9/29 12:16
 */
public class DefaultListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    private void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getPluginManager().callEvent(new AttributeReadyEvent(player));
        Bukkit.getPluginManager().callEvent(new AttributeLoadOverEvent(player));
    }


    @EventHandler(priority = EventPriority.MONITOR)
    private void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        SourceDataManager.INSTANCE.releaseMirrorDataSource(player);
    }

}
