package com.cjburkey.cubegame.mesh;

import java.util.Arrays;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class MeshHelper {
	
	public static void addCube(MeshData mesh, Vector3f minCorner, Vector2f minUv, Vector2f maxUv) {
		// Sides
		addQuad(mesh, minCorner, right(), up(), minUv, maxUv);	// Front
		addQuad(mesh, new Vector3f(minCorner).add(right()), forward(), up(), minUv, maxUv);	// Right
		addQuad(mesh, new Vector3f(minCorner).add(right()).add(forward()), left(), up(), minUv, maxUv);	// Back
		addQuad(mesh, new Vector3f(minCorner).add(forward()), backward(), up(), minUv, maxUv);	// Left
		
		// Top and bottom
		addQuad(mesh, new Vector3f(minCorner).add(up()), right(), forward(), minUv, maxUv);	// Top
		addQuad(mesh, new Vector3f(minCorner).add(forward()), right(), backward(), minUv, maxUv);	// Bottom
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