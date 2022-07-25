package _128_bit_guy.simple_routed_pipes.pipe;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.*;

public class PipeNetwork {
    private static long LAST_ID = 0;
    private static long ID_REFRESH_TIME = 0;
    public final Map<UUID, PipeBehaviourRouted> pipes = new HashMap<>();
    public long refreshTime;
    public long id;

    public static long getNewId(long worldTime) {
        if (ID_REFRESH_TIME != worldTime) {
            ID_REFRESH_TIME = worldTime;
            LAST_ID = 0;
        }
        return LAST_ID++;
    }

    public void addPipe(PipeBehaviourRouted b) {
        pipes.put(b.pipeId, b);
    }
}
