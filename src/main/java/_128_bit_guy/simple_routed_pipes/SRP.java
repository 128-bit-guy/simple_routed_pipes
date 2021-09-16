package _128_bit_guy.simple_routed_pipes;

import _128_bit_guy.simple_routed_pipes.item.RoutedPipeItem;
import _128_bit_guy.simple_routed_pipes.pipe.PipeBehaviourBasic;
import _128_bit_guy.simple_routed_pipes.pipe.PipeDefRouted;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SRP implements ModInitializer {
    public static final String MOD_ID = "simple_routed_pipes";
    public static PipeDefRouted BASIC_PIPE;
    public static RoutedPipeItem BASIC_PIPE_ITEM;

    public static Identifier id(String s) {
        return new Identifier(MOD_ID, s);
    }

    @Override
    public void onInitialize() {
        BASIC_PIPE = new PipeDefRouted(id("basic_pipe"), false, false, 1.0d, PipeBehaviourBasic::new);
        BASIC_PIPE.register();
        Registry<Item> item = Registry.ITEM;
        Item.Settings pipe = new Item.Settings().group(ItemGroup.TRANSPORTATION);
        BASIC_PIPE_ITEM = Registry.register(item, id("basic_pipe"), new RoutedPipeItem(pipe, BASIC_PIPE));
    }
}
