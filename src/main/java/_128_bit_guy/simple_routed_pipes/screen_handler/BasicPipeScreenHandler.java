package _128_bit_guy.simple_routed_pipes.screen_handler;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourBasic;
import alexiil.mc.lib.attributes.item.compat.SlotFixedItemInv;
import alexiil.mc.lib.attributes.item.impl.DirectFixedItemInv;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class BasicPipeScreenHandler extends RoutedPipeScreenHandler<PipeBehaviourBasic> {
    public final PlayerInventory playerInventory;
    public final DirectFixedItemInv filterInv;
    public BasicPipeScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory inv, PipeBehaviourBasic behaviour) {
        super(type, syncId, inv, behaviour);
        filterInv = behaviour.filterInventory;
        playerInventory = inv;
        boolean server = !inv.player.world.isClient();
        for (int j = 0; j < 1; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new SlotFixedItemInv(
                        this,
                        filterInv,
                        server,
                        k + j * 9,
                        8 + k * 18,
                        18 + j * 18
                ));
            }
        }
        int i = (1 - 4) * 18;
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
        }
    }
}
