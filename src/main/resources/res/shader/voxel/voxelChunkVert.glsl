#version 330 core

layout(location = 0) in vec3 vert;
layout(location = 1) in vec3 vertColor;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 color;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(vert, 1.0);
	
	color = vertColor;
}