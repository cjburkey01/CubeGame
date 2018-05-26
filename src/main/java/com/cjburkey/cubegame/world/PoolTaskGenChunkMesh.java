package com.cjburkey.cubegame.world;

import java.util.UUID;
import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.mesh.ChunkMeshBuilder;
import com.cjburkey.cubegame.mesh.MeshData;
import com.cjburkey.cubegame.thread.IPoolTask;

public final class PoolTaskGenChunkMesh implements IPoolTask {
	
	private final UUID uuid;
	private final Chunk chunk;
	private final MeshData meshData = new MeshData();
	
	public PoolTaskGenChunkMesh(Chunk chunk) {
		uuid = UUID.randomUUID();
		this.chunk = chunk;
	}
	
	public void execute() {
		ChunkMeshBuilder.greedyMeshChunk(meshData, chunk);
		chunk.world.onFinishMesh(this);
	}
	
	public MeshData getMesh() {
		return meshData;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chunk == null) ? 0 : chunk.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PoolTaskGenChunkMesh other = (PoolTaskGenChunkMesh) obj;
		if (chunk == null) {
			if (other.chunk != null)
				return false;
		} else if (!chunk.equals(other.chunk))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
}