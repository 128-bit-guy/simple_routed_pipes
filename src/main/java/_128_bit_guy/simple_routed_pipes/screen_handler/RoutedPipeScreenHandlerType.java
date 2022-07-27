package _128_bit_guy.simple_routed_pipes.screen_handler;

import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourRouted;
import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RoutedPipeScreenHandlerType <B extends PipeBehaviourRouted, T extends RoutedPipeScreenHandler<B>> extends ExtendedScreenHandlerType<T> {
    private final Factory<B, T> factory;
    public final Text text;
    @SuppressWarnings("unchecked")
    private RoutedPipeScreenHandlerType(Factory<B, T> factory, Text text, Object[] obj) {
        super((syncId, inventory, buf) -> factory.createScreenHandler((ScreenHandlerType<?>) obj[0], syncId, inventory, (B)getBehaviour(buf)));
        this.factory = factory;
        obj[0] = this;
        this.text = text;
    }

    public RoutedPipeScreenHandlerType(Factory<B, T> factory, Text text) {
        this(factory, text, new Object[1]);
    }

    public void open(PlayerEntity player, B behaviour) {
        player.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(behaviour.pipe.getPipePos());
            }

            @Override
            public Text getDisplayName() {
                return text;
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return factory.createScreenHandler(RoutedPipeScreenHandlerType.this, syncId, inv, behaviour);
            }
        });
    }

    @Environment(EnvType.CLIENT)
    private static PipeBehaviourRouted getBehaviour(PacketByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        MultipartContainer container = MultipartContainer.ATTRIBUTE.getFirstOrNull(
                MinecraftClient.getInstance().world,
                pos
        );
        PartSpPipe pipe = container.getFirstPart(PartSpPipe.class);
        return (PipeBehaviourRouted) pipe.behaviour;
    }

    @FunctionalInterface
    public interface Factory<B extends PipeBehaviourRouted, T extends RoutedPipeScreenHandler<B>> {
        T createScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory inventory, B behaviour);
    }
}
