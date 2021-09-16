package _128_bit_guy.simple_routed_pipes.pipe;

import java.util.ArrayList;
import java.util.List;

public class PipeNetwork {
    private static long LAST_ID = 0;
    private static long ID_REFRESH_TIME = 0;
    public final List<PipeBehaviourRouted> pipes = new ArrayList<>();
    public long refreshTime;
    public long id;

    public static long getNewId(long worldTime) {
        if (ID_REFRESH_TIME != worldTime) {
            ID_REFRESH_TIME = worldTime;
            LAST_ID = 0;
        }
        return LAST_ID++;
    }
}
