package se.intercour.devtools.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.chunk.Chunk;

public final class PobHelper
{
    public static void AddChatMessage(EntityPlayer player, String message)
    {
        player.addChatMessage(new ChatComponentText(message));
    }

    public static <T> boolean arrayContains(final T[] array, final T v) {
        if (v == null) {
            for (final T e : array)
                if (e == null)
                    return true;
        } else {
            for (final T e : array)
                if (e == v || v.equals(e))
                    return true;
        }

        return false;
    }
    
    public static String chunkName(Chunk chunk)
    {
    	return chunk.xPosition + "," + chunk.zPosition;
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    private PobHelper()
    {}

}
