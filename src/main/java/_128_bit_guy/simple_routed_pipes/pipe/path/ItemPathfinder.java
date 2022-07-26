package _128_bit_guy.simple_routed_pipes.pipe.path;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourRouted;
import _128_bit_guy.simple_routed_pipes.pipe.PipeNetwork;
import _128_bit_guy.simple_routed_pipes.pipe.PipeNetworkConnectionData;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import net.minecraft.util.math.Direction;

import java.util.*;

public class ItemPathfinder {
    public static void findPath(
            PipeBehaviourRouted from,
            Set<PipeBehaviourRouted> to,
            ItemPathfinderCallback callback
    ) {
        PriorityQueue<ItemPathNode> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.getPriority(to)));
        Set<PipeBehaviourRouted> was = new HashSet<>();
        queue.add(new ItemPathNode(from, 0, null));
        while(!queue.isEmpty()) {
            ItemPathNode node = queue.poll();
            if(!was.contains(node.pipe)) {
                was.add(node.pipe);
                if (to.contains(node.pipe) && callback.onPathFound(node)) {
                    return;
                }
                queue.addAll(node.getNeighbourNodes());
            }
        }
    }

    public static ItemPath buildPath(ItemPathNode node) {
        List<UUID> list = new ArrayList<>();
        while(node != null) {
            list.add(node.pipe.pipeId);
            node = node.from;
        }
        return new ItemPath(list);
    }

    public static ItemPath findPath(PipeBehaviourRouted from, PipeBehaviourRouted to) {
        ItemPath[] result = new ItemPath[1];
        findPath(from, Collections.singleton(to), node -> {
            result[0] = buildPath(node);
            return true;
        });
        return result[0];
    }

    @FunctionalInterface
    public interface ItemPathfinderCallback {
        boolean onPathFound(ItemPathNode node);
    }
}
