package com.teaman.attributecompatible.common.listener;

import com.teaman.attributecompatible.common.compatible.pxrpg.ExtraAttributeCache;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Author: Teaman
 * Date: 2021/9/29 12:16
 */

public class PxRpgListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    private void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        SourceDataManager.INSTANCE.releaseMirrorDataSource(player);
        ExtraAttributeCache.INSTANCE.releaseExtraAttributeData(player);
    }

}
