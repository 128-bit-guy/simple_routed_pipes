package _128_bit_guy.simple_routed_pipes.util;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIntArray;

import java.util.UUID;

public class NbtUtil {
    public static UUID toUuidSafe(NbtElement element) {
        if(element == null || element.getNbtType() != NbtIntArray.TYPE || ((NbtIntArray)element).size() != 4) {
            return UUID.randomUUID();
        } else {
            return NbtHelper.toUuid(element);
        }
    }
}
