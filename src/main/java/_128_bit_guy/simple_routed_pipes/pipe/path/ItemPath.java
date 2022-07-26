package _128_bit_guy.simple_routed_pipes.pipe.path;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemPath {
    public final List<UUID> nodes;
    public ItemPath(List<UUID> nodes) {
        this.nodes = nodes;
    }
    public UUID getNextNode() {
        return nodes.get(nodes.size() - 1);
    }
    public void onNodeReached() {
        nodes.remove(nodes.size() - 1);
    }
    public boolean isCompleted() {
        return nodes.isEmpty();
    }
}
