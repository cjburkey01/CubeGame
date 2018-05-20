#version 330 core

layout(location = 0) in vec3 vertPos;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(vertPos, 1.0);
}