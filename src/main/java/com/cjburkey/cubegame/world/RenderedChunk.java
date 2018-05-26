package com.cjburkey.cubegame.world;

import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.object.Component;

public class RenderedChunk extends Component {
	
	public final Chunk chunk;
	
	public RenderedChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	
}