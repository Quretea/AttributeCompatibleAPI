package com.teaman.attributecompatible.common.compatible.pxrpg;

import com.pxpmc.pxrpg.api.Module;

/**
 * Author: Teaman
 * Date: 2021/9/28 13:09
 */

public interface IExtraAttributeModule extends Module {

    @Override
    default String getModuleName() {
        return "ExtraAttributeModule";
    }
}