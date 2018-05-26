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
	ambientC = vec4(color, 1.0);
	diffuseC = ambientC;
	speculrC = ambientC;
	vec4 diffuseSpecularComp = calcDirectionalLight(dirLightColor, dirLightIntensity, -dirLightDirection, mvVertexPos, mvVertexNormal);
	
	fragColor = ambientC * vec4(ambientLight, 1.0) + diffuseSpecularComp;
}