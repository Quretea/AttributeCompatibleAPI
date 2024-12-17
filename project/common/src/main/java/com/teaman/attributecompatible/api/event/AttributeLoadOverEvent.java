package com.teaman.attributecompatible.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Author: Teaman
 * Date: 2021/12/19 13:20
 */

public class AttributeLoadOverEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public AttributeLoadOverEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {return HANDLERS;}

}