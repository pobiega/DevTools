package se.intercour.devtools.common;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.intercour.devtools.common.item.ItemChunkAnalyzer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = DevTools.modId, name=DevTools.modName, version=DevTools.version)
public class DevTools
{
	public static final String modId = "DevTools";
	public static final String modName = "Dev Tools";
	public static final String version = "0.9.1";

	@Instance(modId)
	public static DevTools instance;

	public static Logger logger = LogManager.getLogger(modId);

	public static Configuration configuration;

	public static CreativeTabDevTools tabDevTools = new CreativeTabDevTools();

	public static Item ChunkAnalyzer;

	public static boolean verboseNames = false;
	public static boolean useLocalizedNames = true;
	public static boolean useBlacklist = true;
	public static String[] analyzerFilterList = {"minecraft:stone:0"};
	public static int maxChunksAnalyzed = 16;


	protected final String[] defaultFilterList = {
			"minecraft:redstone_ore:0",
			"minecraft:diamond_ore:0",
			"minecraft:iron_ore:0",
			"minecraft:gold_ore:0",
			"minecraft:coal_ore:0",
			"minecraft:lapis_ore:0",
			"minecraft:quartz_ore:0",
			"minecraft:emerald_ore:0",
			"minecraft:glowstone:0",
			"ThermalFoundation:Ore:0",
			"ThermalFoundation:Ore:1",
			"ThermalFoundation:Ore:2",
			"ThermalFoundation:Ore:3",
			"ThermalFoundation:Ore:4",
			"ThermalFoundation:Ore:5",
			"ThermalFoundation:Ore:6",
			"BigReactors:YelloriteOre:0",
			"appliedenergistics2:tile.OreQuartz:0",
			"appliedenergistics2:tile.OreQuartzCharged:0",
			"TConstruct:SearedBrick:3",
			"TConstruct:SearedBrick:4",
			"TConstruct:SearedBrick:5",
			"TConstruct:GravelOre:0",
			"TConstruct:GravelOre:1",
			"TConstruct:GravelOre:2",
			"TConstruct:GravelOre:3",
			"TConstruct:GravelOre:4",
			"TConstruct:GravelOre:5",
			"DraconicEvolution:draconiumOre:0"
	};

	public void addItems()
	{
		ChunkAnalyzer = new ItemChunkAnalyzer().setUnlocalizedName("ChunkAnalyzer");
		GameRegistry.registerItem(ChunkAnalyzer,"ChunkAnalyzer");
	}

	protected void loadConfiguration()
	{
		verboseNames = configuration.get("ChunkAnalyzer", "VerboseNames", true, "The chunk analyzer will print full names. If true, ignores UseLocalizedNames.").getBoolean(false);
		useLocalizedNames = configuration.get("ChunkAnalyzer", "UseLocalizedNames", true, "The chunk analyzer will print localized names instead of unlocalized.").getBoolean(true);
		maxChunksAnalyzed = configuration.get("ChunkAnalyzer", "MaxChunksAnalyzed", 16, "Maximum number of chunks to analyze in one go. Don't set it too high or you will crash the server.").getInt(16);
		useBlacklist = configuration.get("ChunkAnalyzer", "UseBlacklist", false, "Treat the filter list as a blacklist. Useful when trying to find the UniqueIdentifier of a block.").getBoolean(false);
		analyzerFilterList = configuration.get("ChunkAnalyzer", "FilterList", defaultFilterList,  "Blocks to white/blacklist.").getStringList();

		if(configuration.hasChanged())
			configuration.save();
	}


	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		loadConfiguration();

		addItems();
	}
}
