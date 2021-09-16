package _128_bit_guy.simple_routed_pipes.ext;

import alexiil.mc.mod.pipes.pipe.TravellingItem;
import alexiil.mc.mod.pipes.util.DelayedList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public interface PipeSpFlowItemExt {
    void simple_routed_pipes_sendItemDataToClient(TravellingItem item);
    void simple_routed_pipes_dropItem(ItemStack stack, Direction side, Direction motion, double speed);
    DelayedList<TravellingItem> simple_routed_pipes_getItems();
}
