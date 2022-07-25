package _128_bit_guy.simple_routed_pipes.pipe;

import java.util.UUID;

public interface PipeNetworkElement {
    UUID getUuid();
    PipeBehaviourRouted getPipe();
    boolean canProvideItemStorage();
}
