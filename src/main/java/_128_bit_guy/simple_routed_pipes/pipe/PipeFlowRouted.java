package _128_bit_guy.simple_routed_pipes.pipe;

import _128_bit_guy.simple_routed_pipes.ext.PipeSpFlowItemExt;
import _128_bit_guy.simple_routed_pipes.mixin.TravellingItemAccessor;
import alexiil.mc.mod.pipes.pipe.ISimplePipe;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import alexiil.mc.mod.pipes.pipe.PipeSpFlowItem;
import alexiil.mc.mod.pipes.pipe.TravellingItem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class PipeFlowRouted extends PipeSpFlowItem implements PipeFlowItemSpecialOnReachCenter {
    public PipeFlowRouted(ISimplePipe pipe) {
        super(pipe);
    }

    public PipeBehaviourRouted getBehaviour() {
        return (PipeBehaviourRouted) ((PartSpPipe) pipe).behaviour;
    }

    private void addSplittedItem(ItemStack stack, Direction direction, TravellingItem item, long now, double newSpeed) {
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
        ext.simple_routed_pipes_getItems().add(newAcc.getTimeToDest(), newItem);
        ext.simple_routed_pipes_sendItemDataToClient(newItem);
    }

    @Override
    public void onItemReachCenter2(TravellingItem item) {
        PipeSpFlowItemExt ext = (PipeSpFlowItemExt) this;
        if (item.stack.isEmpty()) {
            return;
        }

        Object2IntMap<Direction> map = getBehaviour().splitItem(item);

        long now = pipe.getWorldTime();
        final double newSpeed = 0.08 * getSpeedModifier();

        TravellingItemAccessor acc = (TravellingItemAccessor) item;
        ItemStack nStack = item.stack.copy();
        for (Direction dir : Direction.values()) {
            int cnt = map.getInt(dir);
            if (cnt > nStack.getCount()) {
                System.out.println("Routed pipe at " + pipe.getPipePos() + " tried to send " + cnt + " items to direction " + dir + ", but there were only " + nStack.getCount() + " items");
                cnt = nStack.getCount();
            }
            if (cnt > 0) {
                ItemStack stack = nStack.copy();
                stack.setCount(cnt);
                nStack.increment(-cnt);
                if (pipe.isConnected(dir)) {
                    addSplittedItem(stack, dir, item, now, newSpeed);
                } else {
                    System.out.println("Routed pipe at " + pipe.getPipePos() + " tried to send items to direction " + dir + ", but pipe has no connection at this direction");
                    ext.simple_routed_pipes_dropItem(stack, null, acc.getSide().getOpposite(), newSpeed);
                }
            }
        }
        if(!nStack.isEmpty()) {
            ext.simple_routed_pipes_dropItem(nStack, null, acc.getSide().getOpposite(), newSpeed);
        }
    }
}
