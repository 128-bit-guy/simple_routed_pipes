package _128_bit_guy.simple_routed_pipes.mixin;

import _128_bit_guy.simple_routed_pipes.ext.TravellingItemExt;
import _128_bit_guy.simple_routed_pipes.pipe.TravellingItemRouteData;
import alexiil.mc.mod.pipes.pipe.TravellingItem;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TravellingItem.class)
public class TravellingItemMixin implements TravellingItemExt {
    @Unique
    private TravellingItemRouteData routeData;

    @Override
    public TravellingItemRouteData simple_routed_pipes_getRouteData() {
        return routeData;
    }

    @Override
    public void simple_routed_pipes_setRouteData(TravellingItemRouteData routeData) {
        this.routeData = routeData;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;J)V", at = @At("RETURN"))
    private void onInit(NbtCompound nbt, long tickNow, CallbackInfo ci) {
        if(nbt.contains("simple_routed_pipes_routeData")) {
            routeData = new TravellingItemRouteData(nbt.getCompound("simple_routed_pipes_routeData"));
        }
    }

    @Inject(method = "writeToNbt", at = @At("RETURN"))
    private void onWriteToNbt(long tickNow, CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound compound = cir.getReturnValue();
        if(routeData != null) {
            compound.put("simple_routed_pipes_routeData", routeData.toTag());
        }
    }

    @Inject(method = "canMerge", at = @At("RETURN"), cancellable = true, remap = false)
    private void onCanMerge(TravellingItem _with, CallbackInfoReturnable<Boolean> cir) {
        if(!routeData.equals(((TravellingItemExt)_with).simple_routed_pipes_getRouteData())) {
            cir.setReturnValue(false);
        }
    }
}
