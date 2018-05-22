#version 330 core

layout(location = 0) in vec3 vertPos;
layout(location = 1) in vec2 uvPos;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec2 texPos;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(vertPos, 1.0);
	
	texPos = uvPos;
}