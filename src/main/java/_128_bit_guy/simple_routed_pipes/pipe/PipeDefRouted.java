package _128_bit_guy.simple_routed_pipes.pipe;

import alexiil.mc.mod.pipes.blocks.BlockPipe;
import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.blocks.TilePipeFluidClay;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import alexiil.mc.mod.pipes.pipe.PipeSpBehaviour;
import alexiil.mc.mod.pipes.pipe.PipeSpDef;
import alexiil.mc.mod.pipes.pipe.PipeSpFlowItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PipeDefRouted extends PipeSpDef.PipeDefItem {
    public static final List<PipeDefRouted> DEFS = new ArrayList<>();
    private final Function<PartSpPipe, PipeBehaviourRouted> behaviourCreator;

    public PipeDefRouted(Identifier identifier, boolean isExtraction, boolean canBounce, double speedModifier, Function<PartSpPipe, PipeBehaviourRouted> behaviourCreator) {
        super(identifier, isExtraction, canBounce, speedModifier);
        this.behaviourCreator = behaviourCreator;
        this.pipeBlock = new BlockPipe(AbstractBlock.Settings.of(Material.BAMBOO), this) {
            @Override
            public TilePipe createBlockEntity(BlockPos pos, BlockState pos2) {
                return new TilePipeFluidClay(pos, pos2);
            }
        };
        DEFS.add(this);
    }

    @Override
    public PipeSpFlowItem createFlow(PartSpPipe pipe) {
        return new PipeFlowRouted(pipe);
    }

    @Override
    public PipeSpBehaviour createBehaviour(PartSpPipe pipe) {
        return behaviourCreator.apply(pipe);
    }
}
