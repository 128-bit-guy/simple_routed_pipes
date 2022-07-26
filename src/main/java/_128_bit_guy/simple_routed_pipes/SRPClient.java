package _128_bit_guy.simple_routed_pipes;

import _128_bit_guy.simple_routed_pipes.pipe.PipeDefRouted;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

public class SRPClient implements ClientModInitializer {
    public static final Identifier ROUTED_PIPE_CONNECTED = SRP.id("routed_pipe_connected");
    public static final Identifier ROUTED_PIPE_DISCONNECTED = SRP.id("routed_pipe_disconnected");
    public static final Identifier ROUTED_PIPE_CONNECTED_INACTIVE = SRP.id("routed_pipe_connected_inactive");

    @Override
    public void onInitializeClient() {
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(this::registerSprites);
    }

    private void registerSprites(SpriteAtlasTexture atlasTexture, ClientSpriteRegistryCallback.Registry registry) {
        for (PipeDefRouted def : PipeDefRouted.DEFS) {
            registry.register(def.identifier);
        }
        registry.register(ROUTED_PIPE_CONNECTED);
        registry.register(ROUTED_PIPE_DISCONNECTED);
        registry.register(ROUTED_PIPE_CONNECTED_INACTIVE);
    }
}
