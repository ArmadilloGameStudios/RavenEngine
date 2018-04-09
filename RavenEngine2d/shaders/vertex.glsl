#version 400

layout(location = 0) in vec3 vertex_pos;
layout(location = 1) in vec3 vertex_textures_coords;

uniform vec4 rect;

out vec2 coords;

void main(void) {
	gl_Position = vec4(vertex_pos, 1.0);

	coords = vec2(vertex_textures_coords.xy * rect.zw + rect.xy);
}