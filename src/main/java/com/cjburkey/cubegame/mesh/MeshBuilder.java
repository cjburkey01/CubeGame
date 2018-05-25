package com.cjburkey.cubegame.mesh;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.world.World;

public final class MeshBuilder {
	
	// TODO: IMPLEMENT GREEDY MESHING ALGORITHM (THAT WORKS)
	public static void greedyMeshChunk(MeshData mesh, Chunk chunk) {
		Vector3i chunkWorldPos = World.getBlockFromChunk(chunk.chunkPos).getPos();
		
		// Top-down
		int y = 0;
		Set<Quad> topDownQuads = new HashSet<>();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
				for (y = World.BLOCKS_PER_CHUNK - 1; y >= 0; y --) {
					// Check that the block exists and is not included in another quad
					if (Quad.contains(topDownQuads, new Vector3i(x, y, z)) || chunk.getIsTransparentAt(new BlockPos(x, y, z)) || !chunk.getIsTransparentAt(new BlockPos(x, y + 1, z))) {
						continue;
					}
					
					// Get the block at the start and set the quad endpoint to the start
					BlockState atStart = chunk.getBlockState(new BlockPos(x, y, z));
					Vector2i end = new Vector2i(x, z);
					
					// Find the widest the quad can be
					while (isValidBlockForMeshingTop(chunk, atStart, topDownQuads, new Vector3i(end.x, y, z))) {
						end.x ++;
					}
					end.x --;
					
					// Find the longest that a quad of this width can be
					boolean failed = false;
					while (isValidBlockForMeshingTop(chunk, atStart, topDownQuads, new Vector3i(x, y, end.y))) {
						end.y ++;
						for (int a = x; a <= end.x; a ++) {
							if (!isValidBlockForMeshingTop(chunk, atStart, topDownQuads, new Vector3i(a, y, end.y))) {
								failed = true;
								break;
							}
						}
						if (failed) {
							break;
						}
					}
					end.y --;
					
					topDownQuads.add(new Quad(new Vector2i(x, z), new Vector2i(end), y));
				}
			}
		}
		for (Quad tdquad : topDownQuads) {
			int lx = tdquad.end.x - tdquad.start.x + 1;
			int ly = tdquad.end.y - tdquad.start.y + 1;
			//addQuad(mesh, new Vector3f(chunkWorldPos.x + tdquad.start.x, chunkWorldPos.y + tdquad.y + 1.0f, chunkWorldPos.z + tdquad.start.y), right().mul(lx), forward().mul(ly), new Vector2f().zero(), new Vector2f(1.0f, 1.0f));
			Vector3f start = new Vector3f(chunkWorldPos.x + tdquad.start.x, chunkWorldPos.y + tdquad.y + 1.0f, -chunkWorldPos.z - tdquad.start.y);
			addQuad(mesh, new Vector3f(start), new Vector3f(start).add(lx, 0, 0), new Vector3f(start).add(lx, 0, -ly), new Vector3f(start).add(0, 0, -ly), new Vector2f().zero(), new Vector2f(1.0f, 1.0f));
			//Debug.log("Added quad: {}, {} to {}, {} on y level {}", tdquad.start.x, tdquad.start.y, tdquad.end.x, tdquad.end.y, tdquad.y);
		}
	}
	
	private static boolean isValidBlockForMeshingTop(Chunk chunk, BlockState start, Set<Quad> quads, Vector3i pos) {
		return !chunk.getIsTransparentAt(new BlockPos(pos)) && chunk.getIsTransparentAt(new BlockPos(pos).add(new Vector3i(0, 1, 0))) && start.isSame(chunk.getBlockState(new BlockPos(pos))) && !Quad.contains(quads, pos);
	}
	
	@Deprecated
	// Use greedyMeshChunk instead
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
	
	@Deprecated
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
		addQuad(mesh, bLCorner, new Vector3f(bLCorner).add(right), new Vector3f(bLCorner).add(right).add(up), new Vector3f(bLCorner).add(up), minUv, maxUv);
	}
	
	public static void addQuad(MeshData mesh, Vector3f bLCorner, Vector3f bRCorner, Vector3f tRCorner, Vector3f tLCorner, Vector2f minUv, Vector2f maxUv) {
		// Starting index
		int i = mesh.verts.size();
		
		// Vertices
		mesh.verts.add(new Vector3f(bLCorner));
		mesh.verts.add(new Vector3f(bRCorner));
		mesh.verts.add(new Vector3f(tRCorner));
		mesh.verts.add(new Vector3f(tLCorner));
		
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