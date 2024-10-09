package net.darpserj.simplestmobfarm;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMobFarm implements ModInitializer {
	public static final String MOD_ID = "simplemobfarm";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Item UNIVERSAL_MOB_CATCHER = new UniversalMobCatcher(new FabricItemSettings().maxCount(1));
	public static final ItemGroup SIMPLE_MOB_FARM_GROUP = FabricItemGroup.builder().displayName(Text.of("Simple Mob Farm")).build();
	public static final RegistryKey<ItemGroup> SIMPLE_MOB_FARM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier(MOD_ID, "item_group"));

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "universal_mob_catcher"), UNIVERSAL_MOB_CATCHER);

		Registry.register(Registries.ITEM_GROUP, SIMPLE_MOB_FARM_GROUP_KEY, SIMPLE_MOB_FARM_GROUP);
		ItemGroupEvents.modifyEntriesEvent(SIMPLE_MOB_FARM_GROUP_KEY).register((items) -> {
			items.add(UNIVERSAL_MOB_CATCHER);
		});

	}
}