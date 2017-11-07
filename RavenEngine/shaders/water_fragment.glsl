#version 430
layout(location = 0) out vec3 frag_color;
layout(location = 1) out vec3 frag_glow;

in vec3 light, lightGlow, color;

void main(void) {
    frag_color = color * light;
	frag_glow = lightGlow;
}