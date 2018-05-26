#version 330 core

layout(location = 0) in vec3 vert;
layout(location = 1) in vec3 vertColor;
layout(location = 2) in vec3 normal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec3 color;
out vec3 mvVertexPos;
out vec3 mvVertexNormal;

void main() {
	vec4 mvPos = modelMatrix * vec4(vert, 1.0);
	
    gl_Position = projectionMatrix * viewMatrix * mvPos;
    
    mvVertexNormal = normalize(modelMatrix * vec4(normal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
    
    color = vertColor;
}