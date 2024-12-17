package com.teaman.attributecompatible.common.compatible.pxrpg;

import com.pxpmc.pxrpg.api.AbstractModule;
import com.pxpmc.pxrpg.api.MAPI;
import com.pxpmc.pxrpg.api.Module;
import com.pxpmc.pxrpg.api.adapter.AdapterPlayer;
import com.pxpmc.pxrpg.api.compatible.CompatibleManager;
import com.pxpmc.pxrpg.api.modules.attribute.AttributeCompatible;
import com.pxpmc.pxrpg.api.modules.attribute.AttributeSummary;
import com.pxpmc.pxrpg.api.modules.equipcontainer.PlayerEquip;
import com.pxpmc.pxrpg.api.modules.occupation.OccupationModule;
import com.pxpmc.pxrpg.api.modules.occupation.PlayerOccupationManager;
import com.pxpmc.pxrpg.api.modules.playerdata.PlayerData;
import com.pxpmc.pxrpg.api.modules.playerdata.PlayerDataModule;
import com.pxpmc.pxrpg.api.modules.playerdata.PlayerMonitor;
import com.pxpmc.pxrpg.api.modules.suit.PlayerSuit;
import com.teaman.attributecompatible.api.event.AttributeLoadOverEvent;
import com.teaman.attributecompatible.api.event.AttributeReadyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author: Teaman
 * Date: 2021/9/28 13:10
 */

public class ExtraAttributeModule extends AbstractModule implements IExtraAttributeModule {

    public ExtraAttributeModule(JavaPlugin plugin) {
        super(MAPI.getBukkitPxRpgAPI().toPxRpgPlugin(plugin));
        Module.getModuleManager().registerModule(IExtraAttributeModule.class, this);
    }

    @Override
    public String[] getDepends() {
        return new String[]{
                "PlayerDataModule"
        };
    }

    @Override
    public void onLoad() {
        PlayerDataModule module = Module.getModule(PlayerDataModule.class);

        module.getPlayerMonitor().add(new PlayerMonitor() {

            @Override
            public String[] getSoftDepends() {
                return new String[]{
                        "AttributeCompatible"
                };
            }

            @Override
            public void onLoad(PlayerData playerData) {
                CompatibleManager compatibleManager = playerData.getCompatibleManager();
                if (compatibleManager.hasCompatible("PlayerEquip")) {
                    PlayerSuit playerSuit = compatibleManager.getCompatible(PlayerSuit.class);
                    PlayerEquip playerEquip = compatibleManager.getCompatible(PlayerEquip.class);
                    playerSuit.getSuitableManager().add(new ExtraSuitable(playerEquip));
                }
                new ExtraAttrSummary(playerData);
            }

            @Override
            public void loadOver(PlayerData playerData) {
                AdapterPlayer adapterPlayer = playerData.getPlayer();
                Player player = MAPI.getBukkitPxRpgAPI().toBukkitPlayer(adapterPlayer);
                Bukkit.getPluginManager().callEvent(new AttributeReadyEvent(player));
                PlayerOccupationManager playerOccupationManager = Module.getModule(OccupationModule.class).getPlayerOccupationManager(adapterPlayer);
                if (playerOccupationManager != null) {
                    playerOccupationManager.acceptLastHealth();
                }
                Bukkit.getPluginManager().callEvent(new AttributeLoadOverEvent(player));
            }

            @Override
            public String getId() {
                return getModuleName();
            }
        });

    }

    public class ExtraAttrSummary implements AttributeSummary {

        private final Player player;

        public ExtraAttrSummary(PlayerData playerData) {
            this.player = MAPI.getBukkitPxRpgAPI().toBukkitPlayer(playerData.getPlayer());
            AttributeCompatible compatible = playerData.getCompatibleManager().getCompatible(AttributeCompatible.class);
            compatible.addAttributeSummary("玩家属性兼容获取", this);
        }

        @Override
        public double getAttributeTotal(String id, boolean sign, boolean min) {
            return ExtraAttributeCache.INSTANCE.getExtraAttributeTotal(player, id+"@"+sign+"@"+min);
        }
    }
}