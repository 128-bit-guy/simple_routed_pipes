package _128_bit_guy.simple_routed_pipes.pipe;

import it.unimi.dsi.fastutil.ints.*;
import org.apache.logging.log4j.util.PropertySource;

import java.util.*;

public class PipeNetwork {
    private static long LAST_ID = 0;
    private static long ID_REFRESH_TIME = 0;
    public final Map<UUID, PipeBehaviourRouted> pipes = new HashMap<>();
    public final Map<UUID, PipeNetworkElement> elements = new HashMap<>();
    public final Int2ObjectSortedMap<List<PipeNetworkElement>> storageProviders = new Int2ObjectRBTreeMap<>(IntComparators.OPPOSITE_COMPARATOR);
    public long refreshTime;
    public long id;
    public boolean fullyLoaded = true;

    public static long getNewId(long worldTime) {
        if (ID_REFRESH_TIME != worldTime) {
            ID_REFRESH_TIME = worldTime;
            LAST_ID = 0;
        }
        return LAST_ID++;
    }

    public void addPipe(PipeBehaviourRouted b) {
        pipes.put(b.pipeId, b);
        for (PipeNetworkElement element : b.getNetworkElements()) {
            elements.put(element.getUuid(), element);
            if(element.canProvideItemStorage()) {
                storageProviders.computeIfAbsent(element.getSortingPriority(), i -> new ArrayList<>()).add(element);
            }
        }
    }
}
