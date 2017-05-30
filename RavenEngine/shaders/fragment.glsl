#version 430
layout(location = 0) out vec3 frag_color;
layout(location = 1) out vec3 frag_glow;
layout(location = 2) out vec3 frag_id;

uniform vec3 id;

in vec3 light, lightGlow, color;

void main(void) {
    frag_color = color * light;
	frag_glow = lightGlow;
	frag_id = id;
}