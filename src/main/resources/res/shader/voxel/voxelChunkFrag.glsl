#version 330 core

in vec3 vpos;

out vec4 fragColor;

uniform sampler2D sampler;

void main() {
	//fragColor = vec4(0.4, 0.6, 1.0, 1.0);
	//fragColor = vec4(vpos, 1.0);
	fragColor = texture(sampler, vpos.xz);
}