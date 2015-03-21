package se.intercour.devtools.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import se.intercour.devtools.common.DevTools;
import se.intercour.devtools.common.PobHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class ItemChunkAnalyzer extends DevToolsBaseItem {

	protected class BlockData
	{
		String localizedName;
		String unlocalizedName;
		Integer meta;
		Integer count;

		public BlockData(String unlocalizedName, String localizedName, int meta)
		{
			this.unlocalizedName = unlocalizedName;
			this.localizedName = localizedName;
			this.count = 1;
			this.meta = meta;
		}

		public String getAveragedData(Integer divisor)
		{
			if(divisor == 1)
				return getName() + " - " + count.toString();

			return getName() + " - " + (count/divisor);
		}

		public String getLocalizedName()
		{
			return localizedName;
		}

		public Integer getMetadata()
		{
			return this.meta;
		}

		public String getName()
		{
			if(DevTools.verboseNames)
				return localizedName + " - " + unlocalizedName + ":" + meta;

			return (DevTools.useLocalizedNames ?  localizedName : unlocalizedName + ":" + meta);
		}

		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}

		public void increment()
		{
			count++;
		}

		@Override
		public String toString()
		{
			return getName() + " - " + count.toString();
		}
	}

	private HashMap<String,BlockData> blockData;
	private List<String> analyzedChunks;
	private List<String> filteredBlocks;

	public ItemChunkAnalyzer()
	{
		super();
		setMaxStackSize(1);
		analyzedChunks = new ArrayList<String>();

	}

	public void addOrIncrement(Block block, World world, int x, int y, int z)
	{
		if(blockData == null) {
			blockData = new HashMap<String, BlockData>();
		}

		UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(block);

		String un = ui.modId + ":" + ui.name;
		int meta = world.getBlockMetadata(x,y,z);

		String key = un + ":" + meta;

		ItemStack is = new ItemStack(block.getItem(world, x, y, z));
		is.setItemDamage(meta);

		if (blockData.containsKey(key))
		{
			BlockData bd = blockData.get(key);
			bd.increment();
			blockData.put(key,bd);
		}
		else
			blockData.put(key,new BlockData(un, is.getDisplayName(), meta));
	}

	public void analyzeChunk(Chunk chunk, EntityPlayer player)
	{

		if(analyzedChunks.size() >= DevTools.maxChunksAnalyzed)
		{
			PobHelper.AddChatMessage(player, "Too many chunks analyzed! Empty the buffer!");
			return;
		}

		if(analyzedChunks.contains(PobHelper.chunkName(chunk)))
		{
			PobHelper.AddChatMessage(player, "Chunk already analyzed.");
			return;
		}

		analyzedChunks.add(PobHelper.chunkName(chunk));

		World world = chunk.worldObj;

		for(int i = 0;i < 16;i++)
		{
			for(int j = 0;j < 16;j++)
			{
				for(int y = 0;y < 256;y++)
				{
					int x = chunk.xPosition*16+i;
					int z = chunk.zPosition*16+j;

					Block block = world.getBlock(x,y,z);
					if(block == null || block == Blocks.air)
						continue;

					UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(block);

					String un = ui.modId + ":" + ui.name;
					int meta = world.getBlockMetadata(x,y,z);

					String key = un + ":" + meta;

					//check if block matches filter
					if(isFilteredBlock(key))
						continue;

					addOrIncrement(block, world, x,y,z);
				}
			}
		}

		PobHelper.AddChatMessage(player,"Chunks analyzed: " + analyzedChunks.size());
	}

	public Chunk getChunkAt(World world, int x, int y, int z)
	{
		return world.getChunkFromBlockCoords(x,z);
	}

	protected boolean isFilteredBlock(String name)
	{
		boolean filter = PobHelper.arrayContains(DevTools.analyzerFilterList, name);
		return (DevTools.useBlacklist ? filter : !filter);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
			return false;

		if(!player.isSneaking())
		{
			Block targetBlock = world.getBlock(x,y,z);

			if (targetBlock != null)
			{
				Chunk c = getChunkAt(world,x,y,z);

				if (c == null)
					return false;

				analyzeChunk(c, player);
				return true;
			}
		}
		else
		{
			if(blockData == null)
				return false;

			Iterator it = blockData.entrySet().iterator();

			PobHelper.AddChatMessage(player,"Analyzed " + analyzedChunks.size() + " chunks. Printing...");
			PobHelper.AddChatMessage(player,"------");
			while(it.hasNext())
			{
				BlockData bd = (BlockData)((Map.Entry) it.next()).getValue();
				PobHelper.AddChatMessage(player, bd.getAveragedData(analyzedChunks.size()));
				it.remove();
			}

			blockData = null;
			analyzedChunks.clear();
		}
		return true;
	}

}
