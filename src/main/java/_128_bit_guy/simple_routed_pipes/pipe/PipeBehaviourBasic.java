package _128_bit_guy.simple_routed_pipes.pipe;

import _128_bit_guy.simple_routed_pipes.SRP;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.ItemAttributes;
import alexiil.mc.lib.attributes.item.ItemStackUtil;
import alexiil.mc.lib.attributes.item.impl.DirectFixedItemInv;
import alexiil.mc.lib.net.IMsgReadCtx;
import alexiil.mc.lib.net.NetIdSignalK;
import alexiil.mc.lib.net.ParentNetIdSingle;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PipeBehaviourBasic extends PipeBehaviourRouted implements PipeNetworkElement {
    public static final ParentNetIdSingle<PipeBehaviourBasic> NET_PARENT = PipeBehaviourRouted.NET_PARENT.subType(
            PipeBehaviourBasic.class,
            SRP.id("basic_pipe_behaviour").toString()
    );
    public static final NetIdSignalK<PipeBehaviourBasic> NET_SWITCH_DEFAULT_ROUTE =
            NET_PARENT.idSignal("switch_default_route")
                    .toServerOnly()
                    .setReceiver(PipeBehaviourBasic::onSwitchDefaultRoute);
    public final DirectFixedItemInv filterInventory;
    public boolean defaultRoute;

    public PipeBehaviourBasic(PartSpPipe pipe) {
        super(pipe);
        filterInventory = new DirectFixedItemInv(9);
        filterInventory.addListener(inv -> pipe.holder.getContainer().markChunkDirty(), null);
    }

    private void onSwitchDefaultRoute(IMsgReadCtx ctx) {
        defaultRoute = !defaultRoute;
        pipe.holder.getContainer().markChunkDirty();
        pipe.refreshModel();
    }

    @Environment(EnvType.CLIENT)
    public void switchDefaultRoute() {
        pipe.sendNetworkUpdate(this, NET_SWITCH_DEFAULT_ROUTE);
    }

    @Override
    public List<PipeNetworkElement> getNetworkElements() {
        return Collections.singletonList(this);
    }

    @Override
    protected void openScreen(PlayerEntity player) {
        SRP.BASIC_PIPE_SCREEN_HANDLER.open(player, this);
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
        boolean doesNotAcceptAnything = true;
        if(defaultRoute) {
            doesNotAcceptAnything = false;
        } else {
            for (int i = 0; i < 9; ++i) {
                if (!filterInventory.getInvStack(i).isEmpty()) {
                    doesNotAcceptAnything = false;
                }
            }
        }
        if(doesNotAcceptAnything) {
            return false;
        }
        for (Direction direction : Direction.values()) {
            if (pipe.getItemInsertable(direction) != ItemAttributes.INSERTABLE.defaultValue) {
                return true;
            }
        }
        return false;
    }

    public boolean doesAccept(ItemStack stack) {
        if(defaultRoute) {
            return true;
        } else {
            for (int i = 0; i < filterInventory.getSlotCount(); ++i) {
                if (ItemStackUtil.areEqualIgnoreAmounts(filterInventory.getInvStack(i), stack)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public ItemStack getInsertionExcess(ItemStack stack) {
        if(doesAccept(stack)) {
            ItemStack leftStack = stack;
            for (Direction direction : Direction.values()) {
                leftStack = pipe.getItemInsertable(direction).attemptInsertion(leftStack, Simulation.SIMULATE);
            }
            return leftStack;
        } else {
            return stack;
        }
    }

    @Override
    public ItemStack onItemReachDestination(ItemStack stack, UUID promiseId, ResultItemConsumer consumer) {
        if(doesAccept(stack)) {
            ItemStack leftStack = stack;
            for (Direction direction : Direction.values()) {
                ItemStack excess = pipe.getItemInsertable(direction).attemptInsertion(
                        leftStack,
                        Simulation.SIMULATE
                );
                consumer.sendItem(direction, leftStack.getCount() - excess.getCount(), null);
                leftStack = excess;
            }
            return leftStack;
        } else {
            return stack;
        }
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        super.fromNbt(nbt);
        filterInventory.fromTag(nbt.getCompound("filterInventory"));
        defaultRoute = nbt.getBoolean("defaultRoute");
    }

    @Override
    public NbtCompound toNbt() {
        NbtCompound tag = super.toNbt();
        tag.put("filterInventory", filterInventory.toTag());
        tag.putBoolean("defaultRoute", defaultRoute);
        return tag;
    }
}
