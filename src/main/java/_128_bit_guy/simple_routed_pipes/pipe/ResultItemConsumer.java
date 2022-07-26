package _128_bit_guy.simple_routed_pipes.pipe;

import net.minecraft.util.math.Direction;

public interface ResultItemConsumer {
    void setInactive();
    void sendItem(Direction direction, int count, TravellingItemRouteData routeData);
}
