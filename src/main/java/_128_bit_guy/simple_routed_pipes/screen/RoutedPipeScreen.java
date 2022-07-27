package _128_bit_guy.simple_routed_pipes.screen;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourRouted;
import _128_bit_guy.simple_routed_pipes.screen_handler.RoutedPipeScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class RoutedPipeScreen<B extends PipeBehaviourRouted, T extends RoutedPipeScreenHandler<B>> extends HandledScreen<T> {
    public RoutedPipeScreen(T handler, PlayerInventory inventory, Text text) {
        super(handler, inventory, text);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

    }
}
