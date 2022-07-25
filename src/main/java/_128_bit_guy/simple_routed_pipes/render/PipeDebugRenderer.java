package _128_bit_guy.simple_routed_pipes.render;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourRouted;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

public class PipeDebugRenderer {
    public static void render(PipeBehaviourRouted b, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Text text = b.getDebugText();
        matrices.push();
        matrices.translate(0.5D, 1f, 0.5D);
        MinecraftClient mc = MinecraftClient.getInstance();
        matrices.multiply(mc.getEntityRenderDispatcher().getRotation());
        matrices.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
        int j = (int) (g * 255.0F) << 24;
        TextRenderer textRenderer = mc.textRenderer;
        float h = (float) (-textRenderer.getWidth(text) / 2);
        textRenderer.draw(text, h, 0, 553648127, false, matrix4f, vertexConsumers, true, j, light);
        textRenderer.draw(text, h, 0, -1, false, matrix4f, vertexConsumers, false, 0, light);

        matrices.pop();
    }
}
