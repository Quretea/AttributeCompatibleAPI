package com.teaman.attributecompatible.common.utils;

import org.bukkit.Bukkit;

/**
 * Author Teaman
 * Date 2024/12/17 20:22
 */
public class Utils {

    public static void println(String name, String var) {
        if (var != null) Bukkit.getConsoleSender().sendMessage("§f[§e"+name+"§f] §e-> §a"+var);
    }

}
