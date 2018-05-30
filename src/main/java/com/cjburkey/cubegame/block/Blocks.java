package com.cjburkey.cubegame.block;

import org.joml.Vector3f;
import com.cjburkey.cubegame.Debug;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.event.EventSystem;
import com.cjburkey.cubegame.event.game.EventGameInit;
import com.cjburkey.cubegame.event.registry.RegistryEventBlock;
import com.cjburkey.cubegame.registry.Registry;
import com.cjburkey.cubegame.registry.RegistryName;

@EventListener
public class Blocks {
	
	private static final Registry<Block> blockRegistry = new Registry<>();
	
	public static final Block blockStone = new BlockColored(new RegistryName("cubegame", "blocks/stone"), new Vector3f(0.69f, 0.69f, 0.69f));
	public static final Block blockGrass = new BlockColored(new RegistryName("cubegame", "blocks/grass"), new Vector3f(0.14f, 0.88f, 0.3f));
	public static final Block blockDirt = new BlockColored(new RegistryName("cubegame", "blocks/dirt"), new Vector3f(0.75f, 0.52f, 0.13f));
	
	@EventHandler
	private void onRegisterBlocks(EventGameInit e) {
		Debug.log("Registering blocks");
		
		// Register the game blocks first
		blockRegistry.register(blockStone);
		blockRegistry.register(blockGrass);
		blockRegistry.register(blockDirt);
		
		// Register other blocks
		EventSystem.MAIN_HANDLER.triggerEvent(new RegistryEventBlock(blockRegistry));
		
		Debug.log("Registered blocks");
	}
	
}