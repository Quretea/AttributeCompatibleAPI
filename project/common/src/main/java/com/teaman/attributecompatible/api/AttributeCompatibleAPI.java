package com.teaman.attributecompatible.api;

import com.teaman.attributecompatible.api.compatible.ICompatible;
import com.teaman.attributecompatible.common.data.SourceDataManager;
import org.bukkit.plugin.Plugin;

/**
 * Author: Teaman
 * Date: 2021/9/28 12:52
 */
public class AttributeCompatibleAPI {

    private static ICompatible compatible;

    /**
     * 获取当前属性插件兼容类型实例
     * @return ICompatible   属性插件兼容类型实例
     */
    public static ICompatible getCompatible() {
        return compatible;
    }

    /**
     * 强制设置当前属性插件兼容类型实例
     */
    public static void setCompatible(ICompatible iCompatible) {
        compatible = iCompatible;
    }

    /**
     * 注册需要操作源数据的插件
     * 传入自己的插件实例（并非是你依赖的属性插件）
     * @param plugin       源数据操作插件
     */
    public static void registerSourcePlugin(Plugin plugin) {
        SourceDataManager.INSTANCE.registerSourcePlugin(plugin);
    }
}
