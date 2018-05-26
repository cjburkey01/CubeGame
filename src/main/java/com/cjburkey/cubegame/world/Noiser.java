package com.cjburkey.cubegame.world;

public final class Noiser {
	
	public static double getNoise(int x, int z, double base, GeneratorOctave[] genOcts) {
		double noiseOut = base;
		for (GeneratorOctave generatorOctave : genOcts) {
			noiseOut += (SimplexNoise.noise(x * generatorOctave.frequency, z * generatorOctave.frequency) * generatorOctave.scale);
		}
		return noiseOut;
	}
	
}