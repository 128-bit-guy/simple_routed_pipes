package _128_bit_guy.simple_routed_pipes.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> extends MutableRegistry<T> {
    @Shadow private boolean frozen;

    @Shadow private @Nullable Map<T, RegistryEntry.Reference<T>> unfrozenValueToEntry;

    public SimpleRegistryMixin(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    /**
     * @author 128_bit_guy
     * @reason Minecraft crashes without this
     */
    @Overwrite
    @Override
    public Registry<T> freeze() {
        this.frozen = true;
        unfrozenValueToEntry = null;
        return this;
    }
}
