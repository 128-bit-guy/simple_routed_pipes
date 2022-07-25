package _128_bit_guy.simple_routed_pipes.mixin;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourRouted;
import _128_bit_guy.simple_routed_pipes.render.PipeDebugRenderer;
import alexiil.mc.mod.pipes.client.render.PipePartRenderer;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PipePartRenderer.class)
public class PipePartRendererMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(PartSpPipe part, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (part.behaviour instanceof PipeBehaviourRouted b && MinecraftClient.getInstance().options.debugEnabled) {
            PipeDebugRenderer.render(b, tickDelta, matrices, vertexConsumers, light, overlay);
        }
    }
}
