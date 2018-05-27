package com.cjburkey.cubegame.mesh;

import java.util.Arrays;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class MeshBuilder {
	
	public static void addQuad(MeshData mesh, Vector3f bLCorner, Vector3f right, Vector3f up, Vector3f normal, Vector2f minUv, Vector2f maxUv) {
		addQuad(mesh, bLCorner, new Vector3f(bLCorner).add(right), new Vector3f(bLCorner).add(right).add(up), new Vector3f(bLCorner).add(up), normal, minUv, maxUv);
	}
	
	public static void addQuad(MeshData mesh, Vector3f bLCorner, Vector3f bRCorner, Vector3f tRCorner, Vector3f tLCorner, Vector3f normal, Vector2f minUv, Vector2f maxUv) {
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
		
		// Normals
		for (int a = 0; a < 4; a ++) {
			mesh.normals.add(normal);
		}
	}
	
	public static void addQuad(MeshData mesh, Vector3f bLCorner, Vector3f bRCorner, Vector3f tRCorner, Vector3f tLCorner, Vector3f normal, Vector3f color, float colorRandomness) {
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
		
		for (int a = 0; a < 4; a ++) {
			// Colors
			mesh.colors.add(new Vector3f(color));
			
			// Color randomness
			mesh.colorVariability.add(colorRandomness);
			
			// Normals
			mesh.normals.add(normal);
		}
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