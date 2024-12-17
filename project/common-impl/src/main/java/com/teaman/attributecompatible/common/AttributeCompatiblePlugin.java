package com.teaman.attributecompatible.common;

import com.teaman.attributecompatible.api.AttributeCompatibleAPI;
import com.teaman.attributecompatible.common.compatible.attributeplus.ApCompatibleFor2;
import com.teaman.attributecompatible.common.compatible.attributeplus.ApCompatibleFor3;
import com.teaman.attributecompatible.common.compatible.attributesystem.AsCompatible;
import com.teaman.attributecompatible.common.compatible.itemloreorigin.IloCompatibleForLrd1122;
import com.teaman.attributecompatible.common.compatible.itemloreorigin.IloCompatibleForMchim;
import com.teaman.attributecompatible.common.compatible.originattribute.OaCompatible;
import com.teaman.attributecompatible.common.compatible.pxrpg.ExtraAttributeCache;
import com.teaman.attributecompatible.common.compatible.pxrpg.ExtraAttributeModule;
import com.teaman.attributecompatible.common.compatible.pxrpg.PrCompatible;
import com.teaman.attributecompatible.common.compatible.pxrpg.PxRpgAdapter;
import com.teaman.attributecompatible.common.compatible.sxattribute.SxCompatibleFor2;
import com.teaman.attributecompatible.common.compatible.sxattribute.SxCompatibleFor3;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import com.teaman.attributecompatible.common.listener.CompatibleListener;
import com.teaman.attributecompatible.common.listener.DefaultListener;
import com.teaman.attributecompatible.common.listener.PxRpgListener;
import com.teaman.attributecompatible.common.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author Teaman
 */
public final class AttributeCompatiblePlugin extends JavaPlugin {

    private static final String NAME = "AttributeCompatibleAPI";
    private static String compatiblePluginName= null;
    private static String compatiblePluginVersion = null;
    private static AttributeCompatiblePlugin INSTANCE;


    @Override
    public void onEnable() {
        INSTANCE = this;
        Utils.println(NAME, "AttributeCompatibleAPI正在启动~~");
        Utils.println(NAME, "作者：清茶");
        Utils.println(NAME, "版本："+ this.getDescription().getVersion());
        saveDefaultConfig();
        reloadConfig();
        new SourceDataManager();
        if (!forceCheckCompatiblePlugin()) {
            autoCheckCompatiblePlugin();
        }
        if (compatiblePluginName==null){
            Utils.println(NAME, "§4未找到相关属性插件 兼容失败~");
            Utils.println(NAME, "自动兼容检测程序启动中~~");
            Bukkit.getPluginManager().registerEvents(new CompatibleListener(),this);
            return;
        }
        Utils.println(NAME, compatiblePluginName + " " + compatiblePluginVersion + "版本 成功兼容~");
        Utils.println(NAME, "感谢使用AttributeCompatibleAPI~~");
    }

    @Override
    public void onDisable() {
        Utils.println(NAME, "§4AttributeCompatibleAPI已卸载");
    }

    public static AttributeCompatiblePlugin inst() {
        return INSTANCE;
    }

    public static String getCompatiblePluginName() {
        return compatiblePluginName;
    }

    public static String getCompatiblePluginVersion() {
        return compatiblePluginVersion;
    }

    public static void setCompatiblePluginName(String name) {
        compatiblePluginName = name;
    }

    public static void setCompatiblePluginVersion(String version) {
        compatiblePluginVersion = version;
    }

    public static boolean forceCheckCompatiblePlugin(){
        final PluginManager pluginManager = Bukkit.getPluginManager();
        final boolean force = INSTANCE.getConfig().getBoolean("force",false);
        if (!force){
            return false;
        }
        Utils.println(NAME,"强制兼容程序启动中~~");
        final String forcePlugin = INSTANCE.getConfig().getString("plugin","");
        if ("AttributePlus".equalsIgnoreCase(forcePlugin) && pluginManager.isPluginEnabled("AttributePlus")) {
            if (!checkApCompatible(pluginManager)) {
                Utils.println(NAME,"§4无对应版本的兼容类型 强制兼容失败!!!");
                return false;
            }
            return true;
        }
        if ("SX-Attribute".equalsIgnoreCase(forcePlugin) && pluginManager.isPluginEnabled("SX-Attribute")) {
            if (!checkSxCompatible(pluginManager)) {
                Utils.println(NAME,"§4无对应版本的兼容类型 强制兼容失败!!!");
                return false;
            }
            return true;
        }
        if ("PxRpg".equalsIgnoreCase(forcePlugin) && pluginManager.isPluginEnabled("PxRpg")) {
            if (!checkPrCompatible(pluginManager)) {
                Utils.println(NAME,"§4无对应版本的兼容类型 强制兼容失败!!!");
                return false;
            }
            return true;
        }
        if ("OriginAttribute".equalsIgnoreCase(forcePlugin) && pluginManager.isPluginEnabled("OriginAttribute")) {
            if (!checkOaCompatible(pluginManager)) {
                Utils.println(NAME,"§4无对应版本的兼容类型 强制兼容失败!!!");
                return false;
            }
            return true;
        }
        if ("AttributeSystem".equalsIgnoreCase(forcePlugin) && pluginManager.isPluginEnabled("AttributeSystem")) {
            if (!checkAsCompatible(pluginManager)) {
                Utils.println(NAME,"§4无对应版本的兼容类型 强制兼容失败!!!");
                return false;
            }
            return true;
        }
        if ("ItemLoreOrigin".equalsIgnoreCase(forcePlugin) && pluginManager.isPluginEnabled("ItemLoreOrigin")) {
            if (!checkIloCompatible(pluginManager)) {
                Utils.println(NAME,"§4无对应版本的兼容类型 强制兼容失败!!!");
                return false;
            }
            return true;
        }
        Utils.println(NAME,"§4未找到对应插件 强制兼容失败!!!");
        return false;
    }

    public static void autoCheckCompatiblePlugin(){
        final PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.isPluginEnabled("AttributePlus")) {
            if (checkApCompatible(pluginManager)) {
                return;
            }
        }
        if (pluginManager.isPluginEnabled("SX-Attribute")) {
            if (checkSxCompatible(pluginManager)) {
                return;
            }
        }
        if (pluginManager.isPluginEnabled("PxRpg")) {
            if (checkPrCompatible(pluginManager)) {
                return;
            }
        }
        if (pluginManager.isPluginEnabled("OriginAttribute")) {
            if (checkOaCompatible(pluginManager)) {
                return;
            }
        }
        if (pluginManager.isPluginEnabled("AttributeSystem")) {
            if (checkAsCompatible(pluginManager)) {
                return;
            }
        }
        if (pluginManager.isPluginEnabled("ItemLoreOrigin")) {
            if (checkIloCompatible(pluginManager)) {
                return;
            }
        }
    }

    private static boolean checkApCompatible(PluginManager pluginManager) {
        final String version = pluginManager.getPlugin("AttributePlus").getDescription().getVersion();
        if (version.startsWith("2")) {
            //兼容AP2
            AttributeCompatibleAPI.setCompatible(new ApCompatibleFor2());
            compatiblePluginName = "AttributePlus";
            compatiblePluginVersion = version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        if (version.startsWith("3")) {
            //兼容AP3
            AttributeCompatibleAPI.setCompatible(new ApCompatibleFor3());
            compatiblePluginName = "AttributePlus";
            compatiblePluginVersion = version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        return false;
    }


    private static boolean checkSxCompatible(PluginManager pluginManager) {
        final String version = pluginManager.getPlugin("SX-Attribute").getDescription().getVersion();
        if (version.startsWith("3")) {
            //兼容SX3
            AttributeCompatibleAPI.setCompatible(new SxCompatibleFor3());
            compatiblePluginName = "SX-Attribute";
            compatiblePluginVersion = version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        if (version.startsWith("2")) {
            //兼容SX2
            AttributeCompatibleAPI.setCompatible(new SxCompatibleFor2());
            compatiblePluginName = "SX-Attribute";
            compatiblePluginVersion = version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        return false;
    }

    private static boolean checkPrCompatible(PluginManager pluginManager) {
        final String version = pluginManager.getPlugin("PxRpg").getDescription().getVersion();
        if (version.startsWith("4")) {
            //兼容PR4
            AttributeCompatibleAPI.setCompatible(new PrCompatible());
            compatiblePluginName = "PxRpg";
            compatiblePluginVersion = version;
            Bukkit.getScheduler().runTaskLater(AttributeCompatiblePlugin.inst(), () -> {
                new ExtraAttributeCache();
                new ExtraAttributeModule(AttributeCompatiblePlugin.inst());
                new PxRpgAdapter();
                Bukkit.getPluginManager().registerEvents(new PxRpgListener(), AttributeCompatiblePlugin.inst());
            },1L);
            return true;
        }
        return false;
    }

    private static boolean checkOaCompatible(PluginManager pluginManager) {
        final String version = pluginManager.getPlugin("OriginAttribute").getDescription().getVersion();
        if (version.startsWith("2")) {
            //兼容OA2
            AttributeCompatibleAPI.setCompatible(new OaCompatible());
            compatiblePluginName = "OriginAttribute";
            compatiblePluginVersion = version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        return false;
    }


    private static boolean checkAsCompatible(PluginManager pluginManager) {
        final String version = pluginManager.getPlugin("AttributeSystem").getDescription().getVersion();
        if (version.startsWith("1")) {
            //兼容AS
            AttributeCompatibleAPI.setCompatible(new AsCompatible());
            compatiblePluginName = "AttributeSystem";
            compatiblePluginVersion = version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        return false;
    }


    private static boolean checkIloCompatible(PluginManager pluginManager) {
        final String version = pluginManager.getPlugin("ItemLoreOrigin").getDescription().getVersion();
        boolean lrd = false;
        boolean mchim = false;
        try {
            Class.forName("gx.lrd1122.ItemLoreOrigin.API.AttributeAPI");
            lrd = true;
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("com.mchim.ItemLoreOrigin.API.AttributeAPI");
            mchim = true;
        } catch (ClassNotFoundException ignored) {
        }
        if(lrd){
            //兼容ItemLoreOrigin的lrd1122版本
            AttributeCompatibleAPI.setCompatible(new IloCompatibleForLrd1122());
            compatiblePluginName = "ItemLoreOrigin";
            compatiblePluginVersion = "lrd1122-"+version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        if(mchim){
            //兼容ItemLoreOrigin的mchim版本
            AttributeCompatibleAPI.setCompatible(new IloCompatibleForMchim());
            compatiblePluginName = "ItemLoreOrigin";
            compatiblePluginVersion = "mchim-"+version;
            Bukkit.getPluginManager().registerEvents(new DefaultListener(), AttributeCompatiblePlugin.inst());
            return true;
        }
        return false;
    }
}
