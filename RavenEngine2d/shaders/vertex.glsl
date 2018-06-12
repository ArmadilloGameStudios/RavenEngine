#version 400

layout(location = 0) in vec3 vertex_pos;
layout(location = 1) in vec2 vertex_textures_coords;

uniform vec4 rect;

out vec2 texture_coords;
out float depth;

void main(void) {
	gl_Position = vec4(vertex_pos, 1.0);

    depth = vertex_textures_coords.y * rect.w / 1080f / 2f;
	texture_coords = vec2(vertex_textures_coords * rect.zw + rect.xy);
}