package _128_bit_guy.simple_routed_pipes.pipe.path;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourRouted;
import _128_bit_guy.simple_routed_pipes.pipe.PipeNetworkConnectionData;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemPathNode {
    public PipeBehaviourRouted pipe;
    public int distance;
    public ItemPathNode from;

    public ItemPathNode(PipeBehaviourRouted pipe, int distance, ItemPathNode from) {
        this.pipe = pipe;
        this.distance = distance;
        this.from = from;
    }

    public int getPriority(Set<PipeBehaviourRouted> to) {
        int minDist = (int)1e18;
        for(PipeBehaviourRouted oPipe : to) {
            minDist = Math.min(minDist, oPipe.pipe.getPipePos().getManhattanDistance(pipe.pipe.getPipePos()));
        }
        return distance + minDist;
    }

    public List<ItemPathNode> getNeighbourNodes() {
        List<ItemPathNode> result = new ArrayList<>();
        for(Direction direction : Direction.values()) {
            PipeNetworkConnectionData connectionData = pipe.getConnectionData(direction);
            if(connectionData != null) {
                for(Pair<PipeBehaviourRouted, Direction> pair : connectionData.parts) {
                    result.add(new ItemPathNode(pair.getLeft(), connectionData.size, this));
                }
            }
        }
        return result;
    }
}
