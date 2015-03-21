package se.intercour.devtools.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabDevTools extends CreativeTabs {
	public CreativeTabDevTools() {
		super("tabDevTools");
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(DevTools.ChunkAnalyzer);
	}

	@Override
	public Item getTabIconItem() {
		return DevTools.ChunkAnalyzer;
	}
}
