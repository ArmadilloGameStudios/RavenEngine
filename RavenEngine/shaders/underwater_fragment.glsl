#version 430

layout(location = 0) out vec3 frag_color;

uniform sampler2DMS worldColorTexture;
uniform sampler2DMS worldDepthTexture;

in vec2 coord;

const int num_samples = 4;

void main(void) {
    ivec2 int_coord = ivec2(coord.x * 1424, coord.y * 856);

}