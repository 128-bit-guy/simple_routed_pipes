package _128_bit_guy.simple_routed_pipes.pipe;

import net.minecraft.item.ItemStack;

import java.util.UUID;

public interface PipeNetworkElement {
    UUID getUuid();
    PipeBehaviourRouted getPipe();
    boolean canProvideItemStorage();
    ItemStack getInsertionExcess(ItemStack stack);
    ItemStack onItemReachDestination(ItemStack stack, UUID promiseId, ResultItemConsumer consumer);
}
