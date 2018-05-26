package com.cjburkey.cubegame.mesh;

import java.util.HashSet;
import java.util.Set;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.block.Direction;
import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.world.World;

public final class ChunkMeshBuilder {
	
	// Creates an algorithm that is more locally efficient
	// May not (probably will not) be the BEST mesh, but it
	// Will be as good as it gets with the speed/efficiency ratio
	public static void greedyMeshChunk(MeshData mesh, Chunk chunk) {
		Vector3i chunkWorldPos = World.getBlockFromChunk(chunk.chunkPos).getPos();
		
		// Top-down
		meshTopFaces(mesh, chunk, chunkWorldPos);
		
		// Right-left
		meshRightFaces(mesh, chunk, chunkWorldPos);
		
		// Left-right
		meshLeftFaces(mesh, chunk, chunkWorldPos);
		
		// Bottom-up
		meshBottomFaces(mesh, chunk, chunkWorldPos);
		
		// Back-front
		meshFrontFaces(mesh, chunk, chunkWorldPos);
		
		// Front-back
		meshBackFaces(mesh, chunk, chunkWorldPos);
	}
	
	private static void meshBackFaces(MeshData mesh, Chunk chunk, Vector3i chunkWorldPos) {
		int z = 0;
		Set<Quad> backFrontQuads = new HashSet<>();
		for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
			for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
				for (z = World.BLOCKS_PER_CHUNK - 1; z >= 0; z --) {
					if (Quad.contains(backFrontQuads, new Vector3i(x, z, y)) || checkTransparent(chunk, new Vector3i(x, y, z)) || !checkTransparent(chunk, new Vector3i(x, y, z - 1))) {
						continue;
					}
					
					BlockState atStart = chunk.getBlockState(new BlockPos(x, y, z));
					Vector2i end = new Vector2i(x, y);
					
					while (isValidBlockForMeshingFront(chunk, atStart, backFrontQuads, new Vector3i(end.x, y, z))) {
						end.x ++;
					}
					end.x --;
					
					boolean failed = false;
					while (isValidBlockForMeshingFront(chunk, atStart, backFrontQuads, new Vector3i(x, end.y, z))) {
						end.y ++;
						for (int a = x; a <= end.x; a ++) {
							if (!isValidBlockForMeshingFront(chunk, atStart, backFrontQuads, new Vector3i(a, end.y, z))) {
								failed = true;
								break;
							}
						}
						if (failed) {
							break;
						}
					}
					end.y --;
					
					backFrontQuads.add(new Quad(new Vector2i(x, y), new Vector2i(end), atStart.block.getRenderColorForFace(atStart, Direction.SOUTH), z));
				}
			}
		}
		for (Quad rlquad : backFrontQuads) {
			int lx = rlquad.end.x - rlquad.start.x + 1;
			int ly = rlquad.end.y - rlquad.start.y + 1;
			Vector3f start = new Vector3f(chunkWorldPos.x + rlquad.start.x, chunkWorldPos.y + rlquad.start.y, -chunkWorldPos.z - rlquad.y);
			MeshBuilder.addQuad(mesh, new Vector3f(start), new Vector3f(start).add(lx, 0, 0), new Vector3f(start).add(lx, ly, 0), new Vector3f(start).add(0, ly, 0), MeshBuilder.backward(), rlquad.color);
		}
	}
	
	private static void meshFrontFaces(MeshData mesh, Chunk chunk, Vector3i chunkWorldPos) {
		int z = 0;
		Set<Quad> frontBackQuads = new HashSet<>();
		for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
			for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
				for (z = World.BLOCKS_PER_CHUNK - 1; z >= 0; z --) {
					if (Quad.contains(frontBackQuads, new Vector3i(x, z, y)) || checkTransparent(chunk, new Vector3i(x, y, z)) || !checkTransparent(chunk, new Vector3i(x, y, z + 1))) {
						continue;
					}
					
					BlockState atStart = chunk.getBlockState(new BlockPos(x, y, z));
					Vector2i end = new Vector2i(x, y);
					
					while (isValidBlockForMeshingBack(chunk, atStart, frontBackQuads, new Vector3i(end.x, y, z))) {
						end.x ++;
					}
					end.x --;
					
					boolean failed = false;
					while (isValidBlockForMeshingBack(chunk, atStart, frontBackQuads, new Vector3i(x, end.y, z))) {
						end.y ++;
						for (int a = x; a <= end.x; a ++) {
							if (!isValidBlockForMeshingBack(chunk, atStart, frontBackQuads, new Vector3i(a, end.y, z))) {
								failed = true;
								break;
							}
						}
						if (failed) {
							break;
						}
					}
					end.y --;
					
					frontBackQuads.add(new Quad(new Vector2i(x, y), new Vector2i(end), atStart.block.getRenderColorForFace(atStart, Direction.SOUTH), z));
				}
			}
		}
		for (Quad rlquad : frontBackQuads) {
			int lx = rlquad.end.x - rlquad.start.x + 1;
			int ly = rlquad.end.y - rlquad.start.y + 1;
			Vector3f start = new Vector3f(chunkWorldPos.x + rlquad.start.x + lx, chunkWorldPos.y + rlquad.start.y, -chunkWorldPos.z - rlquad.y - 1.0f);
			MeshBuilder.addQuad(mesh, new Vector3f(start), new Vector3f(start).add(-lx, 0, 0), new Vector3f(start).add(-lx, ly, 0), new Vector3f(start).add(0, ly, 0), MeshBuilder.forward(), rlquad.color);
		}
	}
	
	private static void meshBottomFaces(MeshData mesh, Chunk chunk, Vector3i chunkWorldPos) {
		int y = 0;
		Set<Quad> bottomUpQuads = new HashSet<>();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
				for (y = World.BLOCKS_PER_CHUNK - 1; y >= 0; y --) {
					// Check that the block exists and is not included in another quad
					if (Quad.contains(bottomUpQuads, new Vector3i(x, y, z)) || checkTransparent(chunk, new Vector3i(x, y, z)) || !checkTransparent(chunk, new Vector3i(x, y - 1, z))) {
						continue;
					}
					
					// Get the block at the start and set the quad endpoint to the start
					BlockState atStart = chunk.getBlockState(new BlockPos(x, y, z));
					Vector2i end = new Vector2i(x, z);
					
					// Find the widest the quad can be
					while (isValidBlockForMeshingBottom(chunk, atStart, bottomUpQuads, new Vector3i(end.x, y, z))) {
						end.x ++;
					}
					end.x --;
					
					// Find the longest that a quad of this width can be
					boolean failed = false;
					while (isValidBlockForMeshingBottom(chunk, atStart, bottomUpQuads, new Vector3i(x, y, end.y))) {
						end.y ++;
						for (int a = x; a <= end.x; a ++) {
							if (!isValidBlockForMeshingBottom(chunk, atStart, bottomUpQuads, new Vector3i(a, y, end.y))) {
								failed = true;
								break;
							}
						}
						if (failed) {
							break;
						}
					}
					end.y --;
					
					bottomUpQuads.add(new Quad(new Vector2i(x, z), new Vector2i(end), atStart.block.getRenderColorForFace(atStart, Direction.DOWN), y));
				}
			}
		}
		for (Quad tdquad : bottomUpQuads) {
			int lx = tdquad.end.x - tdquad.start.x + 1;
			int ly = tdquad.end.y - tdquad.start.y + 1;
			Vector3f start = new Vector3f(chunkWorldPos.x + tdquad.start.x, chunkWorldPos.y + tdquad.y, -chunkWorldPos.z - tdquad.start.y - ly);
			MeshBuilder.addQuad(mesh, new Vector3f(start), new Vector3f(start).add(lx, 0, 0), new Vector3f(start).add(lx, 0, ly), new Vector3f(start).add(0, 0, ly), MeshBuilder.down(), tdquad.color);
		}
	}
	
	private static void meshLeftFaces(MeshData mesh, Chunk chunk, Vector3i chunkWorldPos) {
		int x = 0;
		Set<Quad> leftRightQuads = new HashSet<>();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
				for (x = World.BLOCKS_PER_CHUNK - 1; x >= 0; x --) {
					if (Quad.contains(leftRightQuads, new Vector3i(z, x, y)) || checkTransparent(chunk, new Vector3i(x, y, z)) || !checkTransparent(chunk, new Vector3i(x - 1, y, z))) {
						continue;
					}
					
					BlockState atStart = chunk.getBlockState(new BlockPos(x, y, z));
					Vector2i end = new Vector2i(z, y);
					
					while (isValidBlockForMeshingLeft(chunk, atStart, leftRightQuads, new Vector3i(x, y, end.x))) {
						end.x ++;
					}
					end.x --;
					
					boolean failed = false;
					while (isValidBlockForMeshingLeft(chunk, atStart, leftRightQuads, new Vector3i(x, end.y, z))) {
						end.y ++;
						for (int a = z; a <= end.x; a ++) {
							if (!isValidBlockForMeshingLeft(chunk, atStart, leftRightQuads, new Vector3i(x, end.y, a))) {
								failed = true;
								break;
							}
						}
						if (failed) {
							break;
						}
					}
					end.y --;
					
					leftRightQuads.add(new Quad(new Vector2i(z, y), new Vector2i(end), atStart.block.getRenderColorForFace(atStart, Direction.WEST), x));
				}
			}
		}
		for (Quad rlquad : leftRightQuads) {
			int ly = rlquad.end.y - rlquad.start.y + 1;
			int lz = rlquad.end.x - rlquad.start.x + 1;
			Vector3f start = new Vector3f(chunkWorldPos.x + rlquad.y, chunkWorldPos.y + rlquad.start.y, -chunkWorldPos.z - rlquad.start.x - lz);
			MeshBuilder.addQuad(mesh, new Vector3f(start), new Vector3f(start).add(0, 0, lz), new Vector3f(start).add(0, ly, lz), new Vector3f(start).add(0, ly, 0), MeshBuilder.left(), rlquad.color);
		}
	}
	
	private static void meshRightFaces(MeshData mesh, Chunk chunk, Vector3i chunkWorldPos) {
		int x = 0;
		Set<Quad> rightLeftQuads = new HashSet<>();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
				for (x = World.BLOCKS_PER_CHUNK - 1; x >= 0; x --) {
					if (Quad.contains(rightLeftQuads, new Vector3i(z, x, y)) || checkTransparent(chunk, new Vector3i(x, y, z)) || !checkTransparent(chunk, new Vector3i(x + 1, y, z))) {
						continue;
					}
					
					BlockState atStart = chunk.getBlockState(new BlockPos(x, y, z));
					Vector2i end = new Vector2i(z, y);
					
					while (isValidBlockForMeshingRight(chunk, atStart, rightLeftQuads, new Vector3i(x, y, end.x))) {
						end.x ++;
					}
					end.x --;
					
					boolean failed = false;
					while (isValidBlockForMeshingRight(chunk, atStart, rightLeftQuads, new Vector3i(x, end.y, z))) {
						end.y ++;
						for (int a = z; a <= end.x; a ++) {
							if (!isValidBlockForMeshingRight(chunk, atStart, rightLeftQuads, new Vector3i(x, end.y, a))) {
								failed = true;
								break;
							}
						}
						if (failed) {
							break;
						}
					}
					end.y --;
					
					rightLeftQuads.add(new Quad(new Vector2i(z, y), new Vector2i(end), atStart.block.getRenderColorForFace(atStart, Direction.EAST), x));
				}
			}
		}
		for (Quad rlquad : rightLeftQuads) {
			int ly = rlquad.end.y - rlquad.start.y + 1;
			int lz = rlquad.end.x - rlquad.start.x + 1;
			Vector3f start = new Vector3f(chunkWorldPos.x + rlquad.y + 1.0f, chunkWorldPos.y + rlquad.start.y, -chunkWorldPos.z - rlquad.start.x);
			MeshBuilder.addQuad(mesh, new Vector3f(start), new Vector3f(start).add(0, 0, -lz), new Vector3f(start).add(0, ly, -lz), new Vector3f(start).add(0, ly, 0), MeshBuilder.right(), rlquad.color);
		}
	}
	
	private static void meshTopFaces(MeshData mesh, Chunk chunk, Vector3i chunkWorldPos) {
		int y = 0;
		Set<Quad> topDownQuads = new HashSet<>();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
				for (y = World.BLOCKS_PER_CHUNK - 1; y >= 0; y --) {
					// Check that the block exists and is not included in another quad
					if (Quad.contains(topDownQuads, new Vector3i(x, y, z)) || checkTransparent(chunk, new Vector3i(x, y, z)) || !checkTransparent(chunk, new Vector3i(x, y + 1, z))) {
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
					
					topDownQuads.add(new Quad(new Vector2i(x, z), new Vector2i(end), atStart.block.getRenderColorForFace(atStart, Direction.UP), y));
				}
			}
		}
		for (Quad tdquad : topDownQuads) {
			int lx = tdquad.end.x - tdquad.start.x + 1;
			int ly = tdquad.end.y - tdquad.start.y + 1;
			Vector3f start = new Vector3f(chunkWorldPos.x + tdquad.start.x, chunkWorldPos.y + tdquad.y + 1.0f, -chunkWorldPos.z - tdquad.start.y);
			MeshBuilder.addQuad(mesh, new Vector3f(start), new Vector3f(start).add(lx, 0, 0), new Vector3f(start).add(lx, 0, -ly), new Vector3f(start).add(0, 0, -ly), MeshBuilder.up(), tdquad.color);
		}
	}
	
	private static boolean isValidBlockForMeshingTop(Chunk chunk, BlockState start, Set<Quad> quads, Vector3i pos) {
		return !checkTransparent(chunk, new Vector3i(pos)) && checkTransparent(chunk, new Vector3i(pos.x, pos.y + 1, pos.z)) && start.isSame(chunk.getBlockState(new BlockPos(pos))) && !Quad.contains(quads, pos);
	}
	
	private static boolean isValidBlockForMeshingRight(Chunk chunk, BlockState start, Set<Quad> quads, Vector3i pos) {
		return !checkTransparent(chunk, new Vector3i(pos)) && checkTransparent(chunk, new Vector3i(pos.x + 1, pos.y, pos.z)) && start.isSame(chunk.getBlockState(new BlockPos(pos))) && !Quad.contains(quads, new Vector3i(pos.z, pos.x, pos.y));
	}
	
	private static boolean isValidBlockForMeshingLeft(Chunk chunk, BlockState start, Set<Quad> quads, Vector3i pos) {
		return !checkTransparent(chunk, new Vector3i(pos)) && checkTransparent(chunk, new Vector3i(pos.x - 1, pos.y, pos.z)) && start.isSame(chunk.getBlockState(new BlockPos(pos))) && !Quad.contains(quads, new Vector3i(pos.z, pos.x, pos.y));
	}
	
	private static boolean isValidBlockForMeshingBottom(Chunk chunk, BlockState start, Set<Quad> quads, Vector3i pos) {
		return !checkTransparent(chunk, new Vector3i(pos)) && checkTransparent(chunk, new Vector3i(pos.x, pos.y - 1, pos.z)) && start.isSame(chunk.getBlockState(new BlockPos(pos))) && !Quad.contains(quads, pos);
	}
	
	private static boolean isValidBlockForMeshingBack(Chunk chunk, BlockState start, Set<Quad> quads, Vector3i pos) {
		return !checkTransparent(chunk, new Vector3i(pos)) && checkTransparent(chunk, new Vector3i(pos.x, pos.y, pos.z + 1)) && start.isSame(chunk.getBlockState(new BlockPos(pos))) && !Quad.contains(quads, new Vector3i(pos.x, pos.z, pos.y));
	}
	
	private static boolean isValidBlockForMeshingFront(Chunk chunk, BlockState start, Set<Quad> quads, Vector3i pos) {
		return !checkTransparent(chunk, new Vector3i(pos)) && checkTransparent(chunk, new Vector3i(pos.x, pos.y, pos.z - 1)) && start.isSame(chunk.getBlockState(new BlockPos(pos))) && !Quad.contains(quads, new Vector3i(pos.x, pos.z, pos.y));
	}
	
	private static boolean checkTransparent(Chunk chunk, Vector3i pos) {
		pos.add(World.getBlockFromChunk(chunk.chunkPos).getPos());
		return chunk.world.getIsTransparentAt(new BlockPos(pos));
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
			MeshBuilder.addQuad(mesh, minCorner, MeshBuilder.right(), MeshBuilder.up(), MeshBuilder.backward(), minUv, maxUv);	// Front
		}
		if (sides[1]) {
			MeshBuilder.addQuad(mesh, new Vector3f(minCorner).add(MeshBuilder.right()), MeshBuilder.forward(), MeshBuilder.up(), MeshBuilder.right(), minUv, maxUv);	// Right
		}
		if (sides[2]) {
			MeshBuilder.addQuad(mesh, new Vector3f(minCorner).add(MeshBuilder.right()).add(MeshBuilder.forward()), MeshBuilder.left(), MeshBuilder.forward(), MeshBuilder.backward(), minUv, maxUv);	// Back
		}
		if (sides[3]) {
			MeshBuilder.addQuad(mesh, new Vector3f(minCorner).add(MeshBuilder.forward()), MeshBuilder.backward(), MeshBuilder.up(), MeshBuilder.left(), minUv, maxUv);	// Left
		}
		
		// Top and bottom
		if (sides[4]) {
			MeshBuilder.addQuad(mesh, new Vector3f(minCorner).add(MeshBuilder.up()), MeshBuilder.right(), MeshBuilder.forward(), MeshBuilder.up(), minUv, maxUv);	// Top
		}
		if (sides[5]) {
			MeshBuilder.addQuad(mesh, new Vector3f(minCorner).add(MeshBuilder.forward()), MeshBuilder.right(), MeshBuilder.backward(), MeshBuilder.down(), minUv, maxUv);	// Bottom
		}
	}
	
}