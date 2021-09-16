package _128_bit_guy.simple_routed_pipes.item;

import _128_bit_guy.simple_routed_pipes.pipe.PipeDefRouted;
import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import alexiil.mc.mod.pipes.util.SoundUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class RoutedPipeItem extends Item {
    public final PipeDefRouted def;

    public RoutedPipeItem(Settings settings, PipeDefRouted def) {
        super(settings);
        this.def = def;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World w = context.getWorld();
        if (w.isClient) {
            return ActionResult.PASS;
        }

        MultipartContainer.PartOffer offer = getOffer(context);
        if (offer == null) {
            offer = getOffer2(context);
            if (offer == null) {
                return super.useOnBlock(context);
            }
        }
        offer.apply();
        offer.getHolder().getPart().onPlacedBy(context.getPlayer(), context.getHand());
        context.getStack().increment(-1);
        SoundUtil.playBlockPlace(w, offer.getHolder().getContainer().getMultipartPos());
        return ActionResult.SUCCESS;
    }

    private MultipartContainer.PartOffer getOffer(ItemUsageContext context) {
        MultipartContainer.MultipartCreator c = h -> new PartSpPipe(def, h);
        World w = context.getWorld();
        return MultipartUtil.offerNewPart(w, context.getBlockPos(), c);
    }

    private MultipartContainer.PartOffer getOffer2(ItemUsageContext context) {
        MultipartContainer.MultipartCreator c = h -> new PartSpPipe(def, h);
        World w = context.getWorld();
        return MultipartUtil.offerNewPart(w, context.getBlockPos().offset(context.getSide()), c);
    }
}
