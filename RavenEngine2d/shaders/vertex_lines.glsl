#version 410

layout(location = 0) in vec2 vertex_pos;

uniform vec4 rect;

void main(void) {
	gl_Position = vec4(vertex_pos, 0.0, 1.0);
}
