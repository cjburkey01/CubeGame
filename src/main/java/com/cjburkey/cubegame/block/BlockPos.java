package com.cjburkey.cubegame.block;

import org.joml.Vector3i;

public class BlockPos {
	
	private final Vector3i pos = new Vector3i().zero();
	
	public BlockPos(int x, int y, int z) {
		pos.set(x, y, z);
	}
	
	public BlockPos(Vector3i pos) {
		this.pos.set(pos);
	}
	
	public int getX() {
		return pos.x;
	}
	
	public int getY() {
		return pos.y;
	}
	
	public int getZ() {
		return pos.z;
	}
	
	public Vector3i getPos() {
		return new Vector3i(pos);
	}
	
	public BlockPos add(BlockPos other) {
		return new BlockPos(pos.add(other.pos, new Vector3i()));
	}
	
	public BlockPos sub(BlockPos other) {
		return new BlockPos(pos.sub(other.pos, new Vector3i()));
	}
	
	public BlockPos add(Vector3i other) {
		return new BlockPos(pos.add(other, new Vector3i()));
	}
	
	public BlockPos sub(Vector3i other) {
		return new BlockPos(pos.sub(other, new Vector3i()));
	}
	
	public BlockPos scalar(int scalar) {
		return new BlockPos(pos.mul(scalar, new Vector3i()));
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockPos other = (BlockPos) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}
	
}