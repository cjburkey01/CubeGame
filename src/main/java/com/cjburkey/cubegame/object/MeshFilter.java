package com.cjburkey.cubegame.object;

import com.cjburkey.cubegame.mesh.Mesh;

public class MeshFilter extends Component {
	
	private Mesh mesh;
	
	public void onRender() {
		if (mesh != null && mesh.getHasInit()) {
			mesh.render();
		}
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
}