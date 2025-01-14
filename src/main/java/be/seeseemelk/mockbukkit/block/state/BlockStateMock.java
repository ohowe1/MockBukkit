package be.seeseemelk.mockbukkit.block.state;

import be.seeseemelk.mockbukkit.UnimplementedOperationException;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.metadata.MetadataTable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BlockStateMock implements BlockState
{

	private final MetadataTable metadataTable;
	private Block block;
	private Material material;

	public BlockStateMock(@NotNull Material material)
	{
		this.metadataTable = new MetadataTable();
		this.material = material;
	}

	protected BlockStateMock(@NotNull Block block)
	{
		this.metadataTable = new MetadataTable();
		this.block = block;
		this.material = block.getType();
	}

	protected BlockStateMock(@NotNull BlockStateMock state)
	{
		this.metadataTable = new MetadataTable(state.metadataTable);
		this.material = state.getType();
		this.block = state.isPlaced() ? state.getBlock() : null;
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue)
	{
		metadataTable.setMetadata(metadataKey, newMetadataValue);
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey)
	{
		return metadataTable.getMetadata(metadataKey);
	}

	@Override
	public boolean hasMetadata(String metadataKey)
	{
		return metadataTable.hasMetadata(metadataKey);
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin)
	{
		metadataTable.removeMetadata(metadataKey, owningPlugin);
	}

	@Override
	public @NotNull Block getBlock()
	{
		if (block == null)
		{
			throw new IllegalStateException("This BlockState has not been placed!");
		}
		else
		{
			return block;
		}
	}

	@Override
	@Deprecated
	public org.bukkit.material.MaterialData getData()
	{
		return new org.bukkit.material.MaterialData(material);
	}

	@Override
	public Material getType()
	{
		return material;
	}

	@Override
	public byte getLightLevel()
	{
		return getBlock().getLightLevel();
	}

	@Override
	public World getWorld()
	{
		return getBlock().getWorld();
	}

	@Override
	public int getX()
	{
		return getBlock().getX();
	}

	@Override
	public int getY()
	{
		return getBlock().getY();
	}

	@Override
	public int getZ()
	{
		return getBlock().getZ();
	}

	@Override
	public Location getLocation()
	{
		return getBlock().getLocation();
	}

	@Override
	public Location getLocation(Location loc)
	{
		return getBlock().getLocation(loc);
	}

	@Override
	public Chunk getChunk()
	{
		return getBlock().getChunk();
	}

	@Override
	@Deprecated
	public void setData(@NotNull org.bukkit.material.MaterialData data)
	{
		this.material = data.getItemType();
	}

	@Override
	public void setType(Material type)
	{
		this.material = type;
	}

	@Override
	public boolean update()
	{
		return update(false);
	}

	@Override
	public boolean update(boolean force)
	{
		return update(force, true);
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics)
	{
		if (!isPlaced())
		{
			return true;
		}

		Block b = getBlock();

		if (b.getType() != this.getType() && !force)
		{
			return false;
		}

		b.setType(this.getType());

		if (b instanceof BlockMock bm)
		{
			bm.setState(this);
		}

		return true;
	}

	@Override
	@Deprecated
	public byte getRawData()
	{
		// TODO Auto-generated method stub
		throw new UnimplementedOperationException();
	}

	@Override
	@Deprecated
	public void setRawData(byte data)
	{
		// TODO Auto-generated method stub
		throw new UnimplementedOperationException();
	}

	@Override
	public boolean isPlaced()
	{
		return block != null;
	}

	@Override
	public boolean isCollidable()
	{
		// TODO Auto-generated method stub
		throw new UnimplementedOperationException();
	}

	@Override
	public BlockData getBlockData()
	{
		// TODO Auto-generated method stub
		throw new UnimplementedOperationException();
	}

	@Override
	public void setBlockData(BlockData data)
	{
		// TODO Auto-generated method stub
		throw new UnimplementedOperationException();
	}

	/**
	 * This returns a copy of this {@link BlockStateMock}. Inheritents of this class should override this method!
	 *
	 * @return A snapshot of this {@link BlockStateMock}.
	 */
	@NotNull
	public BlockState getSnapshot()
	{
		return new BlockStateMock(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int hash = 1;
		hash = prime * hash + (this.isPlaced() ? this.getWorld().hashCode() : 0);
		hash = prime * hash + (this.isPlaced() ? this.getLocation().hashCode() : 0);
//		hash = prime * hash + (this.getBlockData() != null ? this.getBlockData().hashCode() : 0); Not implemented
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof BlockStateMock other))
		{
			return false;
		}
		if (this.isPlaced() && this.getWorld() != other.getWorld() && (this.getWorld() == null || !this.getWorld().equals(other.getWorld()))) {
			return false;
		}
		if (this.isPlaced() && this.getLocation() != other.getLocation() && (this.getLocation() == null || !this.getLocation().equals(other.getLocation()))) {
			return false;
		}
//		if (this.getBlockData() != other.getBlockData() && (this.getBlockData() == null || !this.getBlockData().equals(other.getBlockData()))) {
//			return false; Not implemented
//		}
		return true;
	}

	@NotNull
	public static BlockStateMock mockState(@NotNull Block block)
	{
		switch (block.getType())
		{
		case DAYLIGHT_DETECTOR:
			return new DaylightDetectorMock(block);
		case COMMAND_BLOCK:
		case CHAIN_COMMAND_BLOCK:
		case REPEATING_COMMAND_BLOCK:
			return new CommandBlockMock(block);
		case CAMPFIRE:
		case SOUL_CAMPFIRE:
			return new CampfireMock(block);
		case BELL:
			return new BellMock(block);
		case LECTERN:
			return new LecternMock(block);
		case HOPPER:
			return new HopperMock(block);
		case BARREL:
			return new BarrelMock(block);
		case DISPENSER:
			return new DispenserMock(block);
		case DROPPER:
			return new DropperMock(block);
		case CHEST:
		case TRAPPED_CHEST:
			return new ChestMock(block);
		case ENDER_CHEST:
			return new EnderChestMock(block);
		case ACACIA_SIGN:
		case ACACIA_WALL_SIGN:
		case BIRCH_SIGN:
		case BIRCH_WALL_SIGN:
		case CRIMSON_SIGN:
		case CRIMSON_WALL_SIGN:
		case DARK_OAK_SIGN:
		case DARK_OAK_WALL_SIGN:
		case JUNGLE_SIGN:
		case JUNGLE_WALL_SIGN:
		case OAK_SIGN:
		case OAK_WALL_SIGN:
		case SPRUCE_SIGN:
		case SPRUCE_WALL_SIGN:
		case WARPED_SIGN:
		case WARPED_WALL_SIGN:
			return new SignMock(block);
		case SHULKER_BOX:
		case WHITE_SHULKER_BOX:
		case ORANGE_SHULKER_BOX:
		case MAGENTA_SHULKER_BOX:
		case LIGHT_BLUE_SHULKER_BOX:
		case YELLOW_SHULKER_BOX:
		case LIME_SHULKER_BOX:
		case PINK_SHULKER_BOX:
		case GRAY_SHULKER_BOX:
		case LIGHT_GRAY_SHULKER_BOX:
		case CYAN_SHULKER_BOX:
		case PURPLE_SHULKER_BOX:
		case BLUE_SHULKER_BOX:
		case BROWN_SHULKER_BOX:
		case GREEN_SHULKER_BOX:
		case RED_SHULKER_BOX:
		case BLACK_SHULKER_BOX:
			return new ShulkerBoxMock(block);
		case WHITE_BANNER:
		case ORANGE_BANNER:
		case MAGENTA_BANNER:
		case LIGHT_BLUE_BANNER:
		case YELLOW_BANNER:
		case LIME_BANNER:
		case PINK_BANNER:
		case GRAY_BANNER:
		case LIGHT_GRAY_BANNER:
		case CYAN_BANNER:
		case PURPLE_BANNER:
		case BLUE_BANNER:
		case BROWN_BANNER:
		case GREEN_BANNER:
		case RED_BANNER:
		case BLACK_BANNER:
		case WHITE_WALL_BANNER:
		case ORANGE_WALL_BANNER:
		case MAGENTA_WALL_BANNER:
		case LIGHT_BLUE_WALL_BANNER:
		case YELLOW_WALL_BANNER:
		case LIME_WALL_BANNER:
		case PINK_WALL_BANNER:
		case GRAY_WALL_BANNER:
		case LIGHT_GRAY_WALL_BANNER:
		case CYAN_WALL_BANNER:
		case PURPLE_WALL_BANNER:
		case BLUE_WALL_BANNER:
		case BROWN_WALL_BANNER:
		case GREEN_WALL_BANNER:
		case RED_WALL_BANNER:
		case BLACK_WALL_BANNER:
			return new BannerMock(block);
		default:
			return new BlockStateMock(block);
		}
	}

}
