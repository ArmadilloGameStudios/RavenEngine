#version 430

layout(location = 0) in vec3 vertex_pos;
layout(location = 1) in vec3 vertex_normal;
layout(location = 2) in vec3 vertex_color;

uniform mat4 P;
uniform mat4 V;
uniform mat4 M;

out vec3 light, lightGlow, color;
out float gl_ClipDistance[1];

vec3 lightDirection = normalize(vec3(1, 3, 2));

void main(void)
{
	gl_Position = P * V * M * vec4(vertex_pos, 1.0);

	float ambiantLight = .5;

	float NdotL = dot(normalize((V * M * vec4(vertex_normal, 0.0)).xyz), normalize(V * vec4(lightDirection, 0.0)).xyz);
	float directionalLight = max(0.0, NdotL) * .5;

	light = vec3(ambiantLight + directionalLight);
	color = vertex_color;

    gl_ClipDistance[0] = dot(M * vec4(vertex_pos, 1.0), vec4(0, 1, 0, 0.0));
}