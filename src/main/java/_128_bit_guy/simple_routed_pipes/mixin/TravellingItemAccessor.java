package _128_bit_guy.simple_routed_pipes.mixin;

import alexiil.mc.mod.pipes.pipe.TravellingItem;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.EnumSet;

@Mixin(TravellingItem.class)
public interface TravellingItemAccessor {
    @Accessor("side")
    Direction getSide();
    @Accessor("tried")
    EnumSet<Direction> getTried();
    @Accessor("toCenter")
    boolean isToCenter();
    @Accessor("speed")
    double getSpeed();
    @Accessor("side")
    void setSide(Direction side);
    @Accessor("toCenter")
    void setToCenter(boolean toCenter);
    @Accessor("speed")
    void setSpeed(double speed);
    @Accessor("timeToDest")
    int getTimeToDest();
}
