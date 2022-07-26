package _128_bit_guy.simple_routed_pipes.pipe;

import _128_bit_guy.simple_routed_pipes.mixin.TravellingItemAccessor;
import _128_bit_guy.simple_routed_pipes.pipe.path.ItemPath;
import _128_bit_guy.simple_routed_pipes.pipe.path.ItemPathfinder;
import _128_bit_guy.simple_routed_pipes.util.NbtUtil;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import alexiil.mc.mod.pipes.blocks.BlockPipe;
import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.pipe.ISimplePipe;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import alexiil.mc.mod.pipes.pipe.PipeSpBehaviour;
import alexiil.mc.mod.pipes.pipe.TravellingItem;
import io.netty.util.internal.ThreadLocalRandom;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public abstract class PipeBehaviourRouted extends PipeSpBehaviour {
    private static final Set<BlockPos> WAS = new HashSet<>();
    private final Map<Direction, PipeNetworkConnectionData> connectionDatas;
    public long netId = 0;
    public PipeNetwork network;
    private byte hasNetworkConnection;
    private boolean connectionDatasDirty = false;
    public UUID pipeId;
    public boolean active;

    public PipeBehaviourRouted(PartSpPipe pipe) {
        super(pipe);
        hasNetworkConnection = (byte) ThreadLocalRandom.current().nextInt(0, 1 << 6);
        connectionDatas = new EnumMap<>(Direction.class);
        pipeId = UUID.randomUUID();
    }

    private static boolean hasBlockPipe(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockPipe;
    }

    private static ISimplePipe getBlockPipe(World world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ISimplePipe) {
            return (ISimplePipe) be;
        } else {
            return null;
        }
    }

    private static PipeSpBehaviour getPipeBehaviour(World world, BlockPos pos) {
        MultipartContainer container = MultipartUtil.get(world, pos);
        if (container == null) {
            return null;
        } else {
            PartSpPipe pipe = container.getFirstPart(PartSpPipe.class);
            if (pipe == null) {
                return null;
            } else {
                return pipe.behaviour;
            }
        }
    }

    private static void connectionDfs(World world, BlockPos pos, Direction direction, PipeNetworkConnectionData data, PipeNetwork network) {
        if (world.isChunkLoaded(pos)) {
            PipeSpBehaviour behaviour = getPipeBehaviour(world, pos);
            if (behaviour instanceof PipeBehaviourRouted) {
                if (!((PipeBehaviourRouted) behaviour).connectionDatas.containsKey(direction) || ((PipeBehaviourRouted) behaviour).connectionDatas.get(direction).refreshTime != world.getTime()) {
                    //System.out.println("Adding " + behaviour.pipe.getPipePos() + " " + direction + " to connection data");
                    data.parts.add(new Pair<>((PipeBehaviourRouted) behaviour, direction));
                    ((PipeBehaviourRouted) behaviour).connectionDatas.put(direction, data);
                    ((PipeBehaviourRouted) behaviour).connectionDatasDirty = true;
                }
            } else if (!WAS.contains(pos) && (behaviour != null || hasBlockPipe(world, pos))) {
                //System.out.println("Visiting " + pos + " from " + direction);
                WAS.add(pos);
                ++data.size;
                ISimplePipe pipe = behaviour == null ? getBlockPipe(world, pos) : behaviour.pipe;
                for (Direction odir : Direction.values()) {
                    if (odir != direction && pipe.isConnected(odir)) {
                        connectionDfs(world, pos.offset(odir), odir.getOpposite(), data, network);
                    }
                }
            }
        } else {
            network.fullyLoaded = false;
        }
    }

    public PipeFlowRouted getFlow() {
        return (PipeFlowRouted) pipe.flow;
    }

    public Direction getMovementDirection(UUID uuid) {
        for(Direction direction : Direction.values()) {
            PipeNetworkConnectionData connectionData = getConnectionData(direction);
            if(connectionData != null) {
                for (Pair<PipeBehaviourRouted, Direction> b : connectionData.parts) {
                    if (b.getLeft().pipeId.equals(uuid)) {
                        return direction;
                    }
                }
            }
        }
        return null;
    }

    public Object2IntMap<Direction> splitItem(TravellingItem item) {
        if(isActive()) {
            Object2IntMap<Direction> map = new Object2IntOpenHashMap<>();
            final ItemStack[] leftStack = {item.stack.copy()};
            Map<PipeBehaviourRouted, List<PipeNetworkElement>> elements = new HashMap<>();
            for(PipeNetworkElement element : network.storageProviders) {
                if(element.getInsertionExcess(leftStack[0]).getCount() != leftStack[0].getCount()) {
                    elements.computeIfAbsent(element.getPipe(), p -> new ArrayList<>()).add(element);
                }
            }
            ItemPathfinder.findPath(this, elements.keySet(), node -> {
                ItemPath path = ItemPathfinder.buildPath(node);
                path.onNodeReached();
                if(path.isCompleted()) {
                    for(Direction direction : Direction.values()) {
                        ItemStack excess = pipe.getItemInsertable(direction).attemptInsertion(
                                leftStack[0],
                                Simulation.SIMULATE
                        );
                        map.put(direction, map.computeIfAbsent(direction, d -> 0) + leftStack[0].getCount() - excess.getCount());
                        leftStack[0] = excess;
                    }
                } else {
                    Direction direction = getMovementDirection(path.getNextNode());
                    for(PipeNetworkElement element : elements.get(node.pipe)) {
                        ItemStack excess = element.getInsertionExcess(leftStack[0]);
                        map.put(direction, map.computeIfAbsent(direction, d -> 0) + leftStack[0].getCount() - excess.getCount());
                        leftStack[0] = excess;
                        if(leftStack[0].getCount() == 0) {
                            return true;
                        }
                    }
                }
                return false;
            });
            return map;
//            TravellingItemAccessor accessor = (TravellingItemAccessor) item;
//            map.put(accessor.getSide(), item.stack.getCount());
//            return map;
        } else {
            return null;
        }
    }

    @Override
    protected TilePipe.PipeBlockModelState createModelState() {
        return new PipeModelStateRouted(pipe.definition, pipe.connections, hasNetworkConnection, active);
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        super.fromNbt(nbt);
        hasNetworkConnection = nbt.getByte("hasNetworkConnection");
        netId = nbt.getLong("networkId");
        pipeId = NbtUtil.toUuidSafe(nbt.get("pipeId"));
        active = nbt.getBoolean("active");
    }

    @Override
    public NbtCompound toNbt() {
        NbtCompound tag = super.toNbt();
        tag.putByte("hasNetworkConnection", hasNetworkConnection);
        tag.putLong("networkId", netId);
        tag.put("pipeId", NbtHelper.fromUuid(pipeId));
        tag.putBoolean("active", active);
        return tag;
    }

    public boolean isActive() {
        return network != null && network.fullyLoaded;
    }

    private void networkDfs(PipeNetwork network) {
        if (this.network == null || this.network.refreshTime != network.refreshTime) {
            this.network = network;
            network.addPipe(this);
            for (Direction direction : Direction.values()) {
                if (pipe.isConnected(direction)) {
                    if (!connectionDatas.containsKey(direction) || connectionDatas.get(direction).refreshTime != pipe.getWorldTime()) {
                        //System.out.println("Starting dfs from " + pipe.getPipePos() + " " + direction);
                        PipeNetworkConnectionData connectionData = new PipeNetworkConnectionData();
                        connectionData.refreshTime = pipe.getWorldTime();
                        connectionData.parts.add(new Pair<>(this, direction));
                        connectionDatas.put(direction, connectionData);
                        connectionDatasDirty = true;
                        WAS.clear();
                        connectionDfs(pipe.getPipeWorld(), pipe.getPipePos().offset(direction), direction.getOpposite(), connectionData, network);
                        connectionData.size += connectionData.parts.size() - 1;
                    }
                } else {
                    connectionDatas.remove(direction);
                }
            }
            for (PipeNetworkConnectionData data : connectionDatas.values()) {
                for (Pair<PipeBehaviourRouted, Direction> pa : data.parts) {
                    pa.getLeft().networkDfs(network);
                }
            }
        }
    }

    public PipeNetworkConnectionData getConnectionData(Direction direction) {
        return connectionDatas.get(direction);
    }

    @Override
    public void tick() {
        if (!pipe.getPipeWorld().isClient) {
            //if(lastRefreshTime != pipe.getWorldTime()) {
            if (network == null || network.refreshTime != pipe.getWorldTime()) {
                PipeNetwork net = new PipeNetwork();
                net.refreshTime = pipe.getWorldTime();
                net.id = PipeNetwork.getNewId(pipe.getWorldTime());
                networkDfs(net);
                //System.out.println("=====================================");
            }
            //}
            boolean refresh = false;
            if (connectionDatasDirty) {
                connectionDatasDirty = false;
                byte ncd = 0;
                for (Direction direction : Direction.values()) {
                    if (connectionDatas.containsKey(direction) && connectionDatas.get(direction).parts.size() > 1) {
                        ncd |= (byte) (1 << direction.ordinal());
                    }
                }
                if (ncd != hasNetworkConnection) {
                    hasNetworkConnection = ncd;
                    refresh = true;
                }
            }
            if (network.id != netId) {
                netId = network.id;
                refresh = true;
            }
            if(active != isActive()) {
                active = isActive();
                refresh = true;
            }
            if (refresh) {
                pipe.refreshModel();
            }
        }
    }

    public abstract List<PipeNetworkElement> getNetworkElements();

    public Text getDebugText() {
        return new LiteralText(pipeId.toString() + ": " + netId);
    }
}
