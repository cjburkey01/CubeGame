package com.cjburkey.cubegame.mesh;

import java.util.Arrays;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.world.World;

public final class MeshHelper {
	
	public static void dumbMeshChunk(MeshData mesh, Chunk chunk) {
		Vector3i world = World.getBlockFromChunk(chunk.chunkPos).getPos();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
				for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
					BlockState block = chunk.getBlockState(new BlockPos(x, y, z));
					if (block == null || block.block == null) {
						continue;
					}
					addCube(mesh, new Vector3f(world.x + x, world.y + y, world.z + z), new Vector2f(), new Vector2f(1.0f, 1.0f), new boolean[] {
						chunk.getIsTransparentAt(new BlockPos(x, y, z + 1)),
						chunk.getIsTransparentAt(new BlockPos(x + 1, y, z)),
						chunk.getIsTransparentAt(new BlockPos(x, y, z - 1)),
						chunk.getIsTransparentAt(new BlockPos(x - 1, y, z)),
						chunk.getIsTransparentAt(new BlockPos(x, y + 1, z)),
						chunk.getIsTransparentAt(new BlockPos(x, y - 1, z))
					});
				}
			}
		}
	}
	
	public static void addCube(MeshData mesh, Vector3f minCorner, Vector2f minUv, Vector2f maxUv, boolean[] sides) {
		// Sides
		if (sides[0]) {
			addQuad(mesh, minCorner, right(), up(), minUv, maxUv);	// Front
		}
		if (sides[1]) {
			addQuad(mesh, new Vector3f(minCorner).add(right()), forward(), up(), minUv, maxUv);	// Right
		}
		if (sides[2]) {
			addQuad(mesh, new Vector3f(minCorner).add(right()).add(forward()), left(), up(), minUv, maxUv);	// Back
		}
		if (sides[3]) {
			addQuad(mesh, new Vector3f(minCorner).add(forward()), backward(), up(), minUv, maxUv);	// Left
		}
		
		// Top and bottom
		if (sides[4]) {
			addQuad(mesh, new Vector3f(minCorner).add(up()), right(), forward(), minUv, maxUv);	// Top
		}
		if (sides[5]) {
			addQuad(mesh, new Vector3f(minCorner).add(forward()), right(), backward(), minUv, maxUv);	// Bottom
		}
	}
	
	public static void addQuad(MeshData mesh, Vector3f bLCorner, Vector3f right, Vector3f up, Vector2f minUv, Vector2f maxUv) {
		// Starting index
		int i = mesh.verts.size();
		
		// Vertices
		mesh.verts.add(new Vector3f(bLCorner));
		mesh.verts.add(new Vector3f(bLCorner).add(right));
		mesh.verts.add(new Vector3f(bLCorner).add(right).add(up));
		mesh.verts.add(new Vector3f(bLCorner).add(up));
		
		// Indices
		mesh.inds.addAll(Arrays.asList(i, i + 1, i + 2,
									   i, i + 2, i + 3));
		
		// Texture coordinates
		mesh.uvs.add(new Vector2f(minUv.x, maxUv.y));
		mesh.uvs.add(new Vector2f(maxUv));
		mesh.uvs.add(new Vector2f(maxUv.x, minUv.y));
		mesh.uvs.add(new Vector2f(minUv));
	}
	
	public static Vector3f right() {
		return new Vector3f(1.0f, 0.0f, 0.0f);
	}
	
	public static Vector3f left() {
		return new Vector3f(-1.0f, 0.0f, 0.0f);
	}
	
	public static Vector3f up() {
		return new Vector3f(0.0f, 1.0f, 0.0f);
	}
	
	public static Vector3f down() {
		return new Vector3f(0.0f, -1.0f, 0.0f);
	}
	
	public static Vector3f forward() {
		return new Vector3f(0.0f, 0.0f, -1.0f);
	}
	
	public static Vector3f backward() {
		return new Vector3f(0.0f, 0.0f, 1.0f);
	}
	
}