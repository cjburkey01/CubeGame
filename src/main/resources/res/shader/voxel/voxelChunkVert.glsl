#version 330 core

layout(location = 0) in vec3 vertPos;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 vpos;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(vertPos, 1.0);
	
	vpos = vertPos;
}