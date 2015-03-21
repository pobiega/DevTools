package se.intercour.devtools.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import se.intercour.devtools.common.DevTools;

public class DevToolsBaseItem extends Item {

	public DevToolsBaseItem()
	{
		super();
		setCreativeTab(DevTools.tabDevTools);
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		itemIcon = register.registerIcon(DevTools.modId + ":" + getUnlocalizedName().replace("item.",""));
	}
}
