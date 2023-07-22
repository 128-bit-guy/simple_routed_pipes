package _128_bit_guy.simple_routed_pipes.mixin;

import _128_bit_guy.simple_routed_pipes.ext.PipeSpFlowItemExt;
import _128_bit_guy.simple_routed_pipes.pipe.PipeFlowItemSpecialOnReachCenter;
import _128_bit_guy.simple_routed_pipes.pipe.TravellingItemRouteData;
import alexiil.mc.mod.pipes.pipe.PipeSpFlowItem;
import alexiil.mc.mod.pipes.pipe.TravellingItem;
import alexiil.mc.mod.pipes.util.DelayedList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.EnumSet;
import java.util.List;

@Mixin(PipeSpFlowItem.class)
public abstract class PipeSpFlowItemMixin implements PipeSpFlowItemExt {
    @Shadow
    @Final
    private DelayedList<TravellingItem> items;

    @Shadow
    abstract void sendItemDataToClient(TravellingItem item);

    @Shadow
    protected abstract void dropItem(ItemStack stack, Direction side, Direction motion, double speed);

    @Inject(method = "onItemReachCenter", at = @At("HEAD"), cancellable = true, remap = false)
    private void onOnItemReachCenterPre(TravellingItem item, CallbackInfo ci) {
        if (this instanceof PipeFlowItemSpecialOnReachCenter f) {
            f.onItemReachCenter2(item);
            ci.cancel();
        }
    }

//    @Inject(method = "onItemReachCenter", at = @At(value = "INVOKE", target = "Lalexiil/mc/mod/pipes/pipe/TravellingItem;genTimings(JD)V"), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
//    private void onOnItemReachCenterGenTimings(TravellingItem item, CallbackInfo ci, EnumSet dirs, List order, long now, double newSpeed, List destinations, TravellingItem newItem) {
//        ((TravellingItemExt) newItem).simple_routed_pipes_setRouteData(
//                ((TravellingItemExt) item).simple_routed_pipes_getRouteData()
//        );
//    }

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

//    @Inject(method = "onItemReachEnd", at = @At("HEAD"), remap = false)
//    private void onItemReachEndPre(TravellingItem item, CallbackInfo ci) {
//        TravellingItemRouteData.DATA_TO_SET = ((TravellingItemExt) item).simple_routed_pipes_getRouteData();
//    }

    @Inject(method = "onItemReachEnd", at = @At("RETURN"), remap = false)
    private void onItemReachEndPost(TravellingItem item, CallbackInfo ci) {
        TravellingItemRouteData.DATA_TO_SET = null;
    }

//    @Inject(method = "insertItemEvents", at = @At(value = "INVOKE", target = "Lalexiil/mc/mod/pipes/pipe/TravellingItem;genTimings(JD)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
//    private void onInsertItemEvents(ItemStack toInsert, DyeColor colour, double speed, Direction from, CallbackInfo ci, long now, TravellingItem item) {
//        ((TravellingItemExt) item).simple_routed_pipes_setRouteData(TravellingItemRouteData.DATA_TO_SET);
//    }
}
