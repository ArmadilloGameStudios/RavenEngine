#version 430

layout(location = 0) in vec3 vertex_pos;
layout(location = 1) in vec3 vertex_normal;
layout(location = 2) in vec3 vertex_color;

uniform mat4 P;
uniform mat4 V;
uniform mat4 M;

const mat4 IV = inverse(V);

out vec3 water_color;
out vec2 coord;

out vec3 camera_vector;
out vec3 camera_vector_2;

vec3 lightDirection = normalize(vec3(1, 3, 2));

void main(void)
{
    vec4 world_pos = M * vec4(vertex_pos, 1.0);

	gl_Position = P * V * world_pos;

    camera_vector =  IV[3].xyz - world_pos.xyz;

	water_color = vertex_color;
	coord = (vertex_pos.xy + 1.0) / 2.0;

}