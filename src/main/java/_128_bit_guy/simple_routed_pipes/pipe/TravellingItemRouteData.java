package _128_bit_guy.simple_routed_pipes.pipe;

import _128_bit_guy.simple_routed_pipes.pipe.path.ItemPath;
import _128_bit_guy.simple_routed_pipes.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;

import java.util.Objects;
import java.util.UUID;

public class TravellingItemRouteData {
    public static TravellingItemRouteData DATA_TO_SET;
    public ItemPath path;
    public UUID destination;
    public UUID promiseId;

    public TravellingItemRouteData(ItemPath path, UUID destination, UUID promiseId) {
        this.path = path;
        this.destination = destination;
        this.promiseId = promiseId;
    }

    public TravellingItemRouteData(NbtCompound compound) {
        path = new ItemPath(compound.getList("path", NbtElement.INT_ARRAY_TYPE));
        destination = NbtUtil.toUuidSafe(compound.get("destination"));
        promiseId = NbtUtil.toUuidSafe(compound.get("promiseId"));
    }

    public NbtCompound toTag() {
        NbtCompound compound = new NbtCompound();
        compound.put("path", path.toTag());
        compound.put("destination", NbtHelper.fromUuid(destination));
        compound.put("promiseId", NbtHelper.fromUuid(promiseId));
        return compound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravellingItemRouteData that = (TravellingItemRouteData) o;
        return path.equals(that.path) && destination.equals(that.destination) && promiseId.equals(that.promiseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, destination, promiseId);
    }
}
