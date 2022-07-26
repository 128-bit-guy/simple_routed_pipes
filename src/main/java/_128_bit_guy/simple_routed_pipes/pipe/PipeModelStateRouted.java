package _128_bit_guy.simple_routed_pipes.pipe;

import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.pipe.PipeSpDef;
import net.minecraft.util.math.Direction;

import java.util.Objects;

public class PipeModelStateRouted extends TilePipe.PipeBlockModelState {
    private final byte networkConnections;
    public final boolean isActive;

    public PipeModelStateRouted(PipeSpDef def, byte isConnected, byte hasNetworkConnection, boolean isActive) {
        super(def, isConnected);
        networkConnections = hasNetworkConnection;
        this.isActive = isActive;
    }

    public boolean hasNetworkConnection(Direction dir) {
        return (networkConnections & (1 << dir.ordinal())) != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PipeModelStateRouted that = (PipeModelStateRouted) o;
        return networkConnections == that.networkConnections && isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), networkConnections, isActive);
    }
}
