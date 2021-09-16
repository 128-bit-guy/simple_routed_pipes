package _128_bit_guy.simple_routed_pipes.pipe;

import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class PipeNetworkConnectionData {
    public List<Pair<PipeBehaviourRouted, Direction>> parts;
    public int size;
    public long refreshTime;

    public PipeNetworkConnectionData() {
        parts = new ArrayList<>();
        size = 0;
    }
}
