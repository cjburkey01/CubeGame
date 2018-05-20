#version 330 core

layout(location = 0) in vec3 vertPos;

uniform mat4 transformation;

void main() {
	gl_Position = transformation * vec4(vertPos, 0.0);
}