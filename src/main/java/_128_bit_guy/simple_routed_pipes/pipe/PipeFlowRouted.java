package _128_bit_guy.simple_routed_pipes.pipe;

import _128_bit_guy.simple_routed_pipes.ext.PipeSpFlowItemExt;
import _128_bit_guy.simple_routed_pipes.ext.TravellingItemExt;
import _128_bit_guy.simple_routed_pipes.mixin.TravellingItemAccessor;
import alexiil.mc.mod.pipes.pipe.ISimplePipe;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import alexiil.mc.mod.pipes.pipe.PipeSpFlowItem;
import alexiil.mc.mod.pipes.pipe.TravellingItem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class PipeFlowRouted extends PipeSpFlowItem implements PipeFlowItemSpecialOnReachCenter {
    public List<TravellingItem> suspendedItems = new ArrayList<>();
    public PipeFlowRouted(ISimplePipe pipe) {
        super(pipe);
    }

    public PipeBehaviourRouted getBehaviour() {
        return (PipeBehaviourRouted) ((PartSpPipe) pipe).behaviour;
    }

    private void addSplittedItem(ItemStack stack, Direction direction, TravellingItem item, long now, double newSpeed, TravellingItemRouteData routeData) {
        TravellingItemAccessor acc = (TravellingItemAccessor) item;
        PipeSpFlowItemExt ext = (PipeSpFlowItemExt) this;
        TravellingItem newItem = new TravellingItem(stack);
        TravellingItemAccessor newAcc = (TravellingItemAccessor) newItem;
        newAcc.getTried().addAll(acc.getTried());
        newAcc.setToCenter(false);
        newItem.colour = item.colour;
        newAcc.setSide(direction);
        newAcc.setSpeed(newSpeed);
        newItem.genTimings(now, pipe.getPipeLength(newAcc.getSide()));
        ((TravellingItemExt)newItem).simple_routed_pipes_setRouteData(routeData);
        ext.simple_routed_pipes_getItems().add(newAcc.getTimeToDest(), newItem);
        ext.simple_routed_pipes_sendItemDataToClient(newItem);
    }

    @Override
    public void onItemReachCenter2(TravellingItem item) {
        PipeSpFlowItemExt ext = (PipeSpFlowItemExt) this;
        if (item.stack.isEmpty()) {
            return;
        }
        boolean[] active = new boolean[1];
        active[0] = true;
        long now = pipe.getWorldTime();
        final double newSpeed = 0.08 * getSpeedModifier();
        ItemStack nStack = item.stack.copy();
        TravellingItemAccessor acc = (TravellingItemAccessor) item;
        ResultItemConsumer consumer = new ResultItemConsumer() {
            @Override
            public void setInactive() {
                if(active[0]) {
                    active[0] = false;
                    suspendedItems.add(item);
                } else {
                    System.out.println("Routed pipe at " + pipe.getPipePos() + " tried to call set inactive twice");
                }
            }

            @Override
            public void sendItem(Direction dir, int cnt, TravellingItemRouteData routeData) {
                if(active[0]) {
                    if (cnt > nStack.getCount()) {
                        System.out.println("Routed pipe at " + pipe.getPipePos() + " tried to send " + cnt + " items to direction " + dir + ", but there were only " + nStack.getCount() + " items");
                        cnt = nStack.getCount();
                    }
                    if (cnt > 0) {
                        ItemStack stack = nStack.copy();
                        stack.setCount(cnt);
                        nStack.increment(-cnt);
                        if (pipe.isConnected(dir)) {
                            addSplittedItem(stack, dir, item, now, newSpeed, routeData);
                        } else {
                            System.out.println("Routed pipe at " + pipe.getPipePos() + " tried to send items to direction " + dir + ", but pipe has no connection at this direction");
                            ext.simple_routed_pipes_dropItem(stack, null, acc.getSide().getOpposite(), newSpeed);
                        }
                    }
                } else {
                    System.out.println("Routed pipe at " + pipe.getPipePos()+ " tried to send items to direction " + dir +", but it already called set inactive");
                }
            }
        };
        getBehaviour().splitItem(item, consumer);
        if (active[0] && !nStack.isEmpty()) {
            ext.simple_routed_pipes_dropItem(nStack, null, acc.getSide().getOpposite(), newSpeed);
        }
    }

    @Override
    public void fromTag(NbtCompound tag) {
        super.fromTag(tag);
        NbtList list = tag.getList("suspendedItems", NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < list.size(); ++i) {
            suspendedItems.add(new TravellingItem(list.getCompound(i), 0));
        }
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = super.toTag();
        NbtList list = new NbtList();
        for(TravellingItem item : suspendedItems) {
            list.add(item.writeToNbt(0));
        }
        tag.put("suspendedItems", list);
        return tag;
    }

    @Override
    public void tick() {
        super.tick();
        List<TravellingItem> suspendedItems2 = new ArrayList<>(suspendedItems);
        suspendedItems.clear();
        for(TravellingItem item : suspendedItems2) {
            onItemReachCenter2(item);
        }
    }
}
