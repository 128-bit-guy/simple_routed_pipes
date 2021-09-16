package _128_bit_guy.simple_routed_pipes.mixin;

import _128_bit_guy.simple_routed_pipes.ext.PipeSpFlowItemExt;
import _128_bit_guy.simple_routed_pipes.pipe.PipeFlowItemSpecialOnReachCenter;
import alexiil.mc.mod.pipes.pipe.PipeSpFlowItem;
import alexiil.mc.mod.pipes.pipe.TravellingItem;
import alexiil.mc.mod.pipes.util.DelayedList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PipeSpFlowItem.class)
public abstract class PipeSpFlowItemMixin implements PipeSpFlowItemExt {
    @Shadow abstract void sendItemDataToClient(TravellingItem item);

    @Shadow protected abstract void dropItem(ItemStack stack, Direction side, Direction motion, double speed);

    @Shadow @Final private DelayedList<TravellingItem> items;

    @Inject(method = "onItemReachCenter", at = @At("HEAD"), cancellable = true, remap = false)
    private void onOnItemReachCenter(TravellingItem item, CallbackInfo ci) {
        if(this instanceof PipeFlowItemSpecialOnReachCenter f) {
            f.onItemReachCenter2(item);
            ci.cancel();
        }
    }

    @Override
    public void simple_routed_pipes_sendItemDataToClient(TravellingItem item) {
        sendItemDataToClient(item);
    }

    @Override
    public void simple_routed_pipes_dropItem(ItemStack stack, Direction side, Direction motion, double speed) {
        dropItem(stack, side, motion, speed);
    }

    @Override
    public DelayedList<TravellingItem> simple_routed_pipes_getItems() {
        return items;
    }
}
