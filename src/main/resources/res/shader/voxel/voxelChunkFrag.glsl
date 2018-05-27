#version 330 core

in vec3 color;
in vec3 mvVertexPos;
in vec3 mvVertexNormal;

uniform vec3 ambientLight;
uniform vec3 dirLightDirection;
uniform vec3 dirLightColor;
uniform float dirLightIntensity;

out vec4 fragColor;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
	vec4 diffuseColour = vec4(0, 0, 0, 0);
	vec4 specColour = vec4(0, 0, 0, 0);
	
	// Diffuse Light
	float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
	diffuseColour = diffuseC * vec4(light_colour, 1.0) * light_intensity * diffuseFactor;
	
	return diffuseColour;
}

vec4 calcDirectionalLight(vec3 light_colour, float light_intensity, vec3 light_direction, vec3 position, vec3 normal) {
	return calcLightColour(light_colour, light_intensity, position, normalize(light_direction), normal);
}

void main() {
	float epsilona = 0.025;
	float epsilonb = 0.0001;
	
	ambientC = vec4(color, 1.0);
	diffuseC = ambientC;
	speculrC = ambientC;
	vec4 diffuseSpecularComp = calcDirectionalLight(dirLightColor, dirLightIntensity, -dirLightDirection, mvVertexPos, mvVertexNormal);
	
	fragColor = ((ambientC * vec4(ambientLight, 1.0)) + diffuseSpecularComp);
	
	// To draw outlines around every block
	// Doesn't work, but would look nice if it did
	float xx = fract(mvVertexPos.x);
	float yy = fract(mvVertexPos.y);
	float zz = fract(mvVertexPos.z);
	bool nearX = (xx >= -epsilona && xx <= epsilona);
	bool nearY = (yy >= -epsilona && yy <= epsilona);
	bool nearZ = (zz >= -epsilona && zz <= epsilona);
	bool overX = (xx >= -epsilonb && xx <= epsilonb);
	bool overY = (yy >= -epsilonb && yy <= epsilonb);
	bool overZ = (zz >= -epsilonb && zz <= epsilonb);
	if ((nearX && !overX) || (nearY && !overY) || (nearZ && !overZ)) {
		fragColor = vec4(color * vec3(0.3, 0.3, 0.3), 1.0);
	}
}