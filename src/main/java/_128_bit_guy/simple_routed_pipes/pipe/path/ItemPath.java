package _128_bit_guy.simple_routed_pipes.pipe.path;

import _128_bit_guy.simple_routed_pipes.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemPath {
    public final List<UUID> nodes;
    public ItemPath(List<UUID> nodes) {
        this.nodes = nodes;
    }
    public ItemPath(NbtList list) {
        nodes = new ArrayList<>();
        for (NbtElement element : list) {
            nodes.add(NbtUtil.toUuidSafe(element));
        }
    }
    public NbtList toTag() {
        NbtList list = new NbtList();
        for(UUID uuid : nodes) {
            list.add(NbtHelper.fromUuid(uuid));
        }
        return list;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPath path = (ItemPath) o;
        return nodes.equals(path.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }
}
