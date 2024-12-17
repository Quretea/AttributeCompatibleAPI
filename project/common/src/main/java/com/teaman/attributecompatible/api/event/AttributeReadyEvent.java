package com.teaman.attributecompatible.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Author: Teaman
 * Date: 2021/9/29 12:13
 */
public class AttributeReadyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public AttributeReadyEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {return handlers;}

}
