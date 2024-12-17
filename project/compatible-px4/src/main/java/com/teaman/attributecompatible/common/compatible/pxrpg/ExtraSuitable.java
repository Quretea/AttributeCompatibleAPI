package com.teaman.attributecompatible.common.compatible.pxrpg;

import com.pxpmc.pxrpg.api.MAPI;
import com.pxpmc.pxrpg.api.Module;
import com.pxpmc.pxrpg.api.adapter.AdapterItemStack;
import com.pxpmc.pxrpg.api.modules.equip.EquipInter;
import com.pxpmc.pxrpg.api.modules.equip.EquipManager;
import com.pxpmc.pxrpg.api.modules.equip.EquipModule;
import com.pxpmc.pxrpg.api.modules.equipcontainer.PlayerEquip;
import com.pxpmc.pxrpg.api.modules.suit.Suitable;
import com.teaman.attributecompatible.common.data.MirrorDataContainer;
import com.teaman.attributecompatible.common.data.MirrorDataHolder;
import com.teaman.attributecompatible.common.data.MirrorDataManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * Author: Teaman
 * Date: 2021/9/28 20:35
 */
public class ExtraSuitable implements Suitable {

    private final Player player;
    private final EquipManager equipManager;

    public ExtraSuitable(PlayerEquip player) {
        this.player = MAPI.getBukkitPxRpgAPI().toBukkitPlayer(player.getPlayer());
        this.equipManager = Module.getModule(EquipModule.class).getEquipManager();

    }
    public Map<String, Integer> getAllEquip() {
        Map<String, Integer> suitMap = new HashMap<>();

        // 缓存 SourceDataManager 实例
        MirrorDataManager mirrorDataManager = MirrorDataManager.INSTANCE;
        List<Plugin> registeredPlugins = mirrorDataManager.getRegisteredSourcePlugin();

        for (Plugin plugin : registeredPlugins) {
            MirrorDataHolder dataSource = mirrorDataManager.getMirrorDataHolder(plugin);
            if (dataSource == null) continue;

            // 缓存玩家的所有源镜像数据
            Set<MirrorDataContainer> holders = dataSource.getAllMirrorDataContainer(this.player.getUniqueId());
            for (MirrorDataContainer holder : holders) {
                ItemStack item = holder.getItemStack();
                if (item == null) continue;

                AdapterItemStack itemStack = MAPI.getBukkitPxRpgAPI().toPxRpgItemStack(item);
                int amount = itemStack.getAmount();
                if (equipManager.isThat(itemStack)) {
                    EquipInter equipInter = equipManager.toThat(itemStack);
                    String id = equipInter.getId();

                    // 使用 Map 的 merge 方法简化更新操作
                    suitMap.merge(id, amount, Integer::sum);
                }
            }
        }
        return suitMap;
    }

    @Override
    public Collection<EquipInter> allEquip() {
        Collection<EquipInter> suitMap = new ArrayList<>();
        MirrorDataManager mirrorDataManager = MirrorDataManager.INSTANCE;
        List<Plugin> registeredPlugins = mirrorDataManager.getRegisteredSourcePlugin();
        for (Plugin plugin : registeredPlugins) {
            MirrorDataHolder dataSource = mirrorDataManager.getMirrorDataHolder(plugin);
            if (dataSource == null) continue;

            // 缓存玩家的所有源镜像数据
            Set<MirrorDataContainer> holders = dataSource.getAllMirrorDataContainer(this.player.getUniqueId());
            for (MirrorDataContainer holder : holders) {
                ItemStack item = holder.getItemStack();
                if (item == null) continue;

                AdapterItemStack itemStack = MAPI.getBukkitPxRpgAPI().toPxRpgItemStack(item);
                if (equipManager.isThat(itemStack)) {
                    EquipInter equipInter = equipManager.toThat(itemStack);
                    suitMap.add(equipInter);
                }
            }
        }
        return suitMap;
    }
}
