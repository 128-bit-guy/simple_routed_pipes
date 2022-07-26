package _128_bit_guy.simple_routed_pipes.mixin;

import _128_bit_guy.simple_routed_pipes.SRPClient;
import _128_bit_guy.simple_routed_pipes.pipe.PipeDefRouted;
import _128_bit_guy.simple_routed_pipes.pipe.PipeModelStateRouted;
import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.client.model.PipeBaseModelGenStandard;
import alexiil.mc.mod.pipes.client.model.SpriteSupplier;
import alexiil.mc.mod.pipes.pipe.PipeSpDef;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PipeBaseModelGenStandard.class)
public abstract class PipeBaseModelGenStandardMixin {
    //@Shadow protected abstract void addQuads(MutableQuad[] from, List<MutableQuad> to, Sprite sprite);

    @Inject(method = "getSprite", at = @At("HEAD"), cancellable = true)
    private static void onGetSprite(SpriteSupplier sprites, TilePipe.PipeBlockModelState key, Direction face, CallbackInfoReturnable<Sprite> cir) {
        if (key instanceof PipeModelStateRouted && key.isConnected(face)) {
            if (((PipeModelStateRouted) key).hasNetworkConnection(face)) {
                if(((PipeModelStateRouted) key).isActive) {
                    cir.setReturnValue(sprites.getBlockSprite(SRPClient.ROUTED_PIPE_CONNECTED));
                } else {
                    cir.setReturnValue(sprites.getBlockSprite(SRPClient.ROUTED_PIPE_CONNECTED_INACTIVE));
                }
            } else {
                cir.setReturnValue(sprites.getBlockSprite(SRPClient.ROUTED_PIPE_DISCONNECTED));
            }
        }
    }

    @Inject(method = "getCenterSprite", at = @At("HEAD"), cancellable = true)
    private static void onGetCenterSprite(SpriteSupplier sprites, PipeSpDef def, CallbackInfoReturnable<Sprite> cir) {
        if (def instanceof PipeDefRouted) {
            cir.setReturnValue(sprites.getBlockSprite(def.identifier));
        }
    }
}
