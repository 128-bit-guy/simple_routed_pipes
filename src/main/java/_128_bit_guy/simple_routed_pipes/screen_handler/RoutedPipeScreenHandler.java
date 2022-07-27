package _128_bit_guy.simple_routed_pipes.screen_handler;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourRouted;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class RoutedPipeScreenHandler<T extends PipeBehaviourRouted> extends ScreenHandler {
    public final T behaviour;

    public RoutedPipeScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory inv, T behaviour) {
        super(type, syncId);
        this.behaviour = behaviour;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
