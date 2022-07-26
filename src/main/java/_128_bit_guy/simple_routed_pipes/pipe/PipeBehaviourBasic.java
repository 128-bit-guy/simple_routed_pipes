package _128_bit_guy.simple_routed_pipes.pipe;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.ItemAttributes;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PipeBehaviourBasic extends PipeBehaviourRouted implements PipeNetworkElement {
    public PipeBehaviourBasic(PartSpPipe pipe) {
        super(pipe);
    }

    @Override
    public List<PipeNetworkElement> getNetworkElements() {
        return Collections.singletonList(this);
    }

    @Override
    public UUID getUuid() {
        return pipeId;
    }

    @Override
    public PipeBehaviourRouted getPipe() {
        return this;
    }

    @Override
    public boolean canProvideItemStorage() {
        for (Direction direction : Direction.values()) {
            if (pipe.getItemInsertable(direction) != ItemAttributes.INSERTABLE.defaultValue) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getInsertionExcess(ItemStack stack) {
        ItemStack leftStack = stack;
        for(Direction direction : Direction.values()) {
            leftStack = pipe.getItemInsertable(direction).attemptInsertion(leftStack, Simulation.SIMULATE);
        }
        return leftStack;
    }

    @Override
    public ItemStack onItemReachDestination(ItemStack stack, UUID promiseId, ResultItemConsumer consumer) {
        ItemStack leftStack = stack;
        for(Direction direction : Direction.values()) {
            ItemStack excess = pipe.getItemInsertable(direction).attemptInsertion(
                    leftStack,
                    Simulation.SIMULATE
            );
            consumer.sendItem(direction, leftStack.getCount() - excess.getCount(), null);
            leftStack = excess;
        }
        return leftStack;
    }
}
