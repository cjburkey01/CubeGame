package com.cjburkey.cubegame.block;

import org.joml.Vector3f;

public class Blocks {
	
	public static final Block blockStone = new BlockBasic("cubegame", "blocks/stone", new Vector3f(175.0f / 255.0f, 175.0f / 255.0f, 175.0f / 255.0f));
	public static final Block blockGrass = new BlockBasic("cubegame", "blocks/grass", new Vector3f(35.0f / 255.0f, 224.0f / 255.0f, 76.0f / 255.0f));
	public static final Block blockDirt = new BlockBasic("cubegame", "blocks/dirt", new Vector3f(191.0f / 255.0f, 133.0f / 255.0f, 34.0f / 255.0f));
	
}