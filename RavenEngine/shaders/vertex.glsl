#version 430

layout(location = 0) in vec3 vertex_pos;
layout(location = 1) in vec3 vertex_normal;
layout(location = 2) in vec3 vertex_color;
layout(location = 3) in vec3 vertex_glow;

uniform mat4 P;
uniform mat4 V;
uniform mat4 M;

out vec3 light, lightGlow, color;

vec3 lightDirection = normalize(vec3(5, 5, 10));

void main(void) 
{
	gl_Position = P * V * M * vec4(vertex_pos, 1.0);

	float ambiantLight = .3; 
	
	float NdotL = dot(normalize((V * M * vec4(vertex_normal, 0.0)).xyz), lightDirection);
	float directionalLight = max(0.0, NdotL) * .3;

	light = vec3(ambiantLight + directionalLight);
	lightGlow = vertex_glow;
	color = vertex_color;
}