#version 430

layout(location = 0) out vec3 frag_color;

uniform sampler2DMS worldColorTexture;

uniform sampler2D glowTexture;

uniform vec2 bloomStep;

in vec2 coord;

const ivec2 int_coord = ivec2(gl_FragCoord.xy);

const int num_samples = @NUM_SAMPLES;

void main(void) {
    vec3 color = vec3(0.0f);

    for (int i = 0; i < num_samples; i++) {
        color += texelFetch(worldColorTexture, int_coord, i).rgb;
    }

    color /= num_samples;

	frag_color = vec3(color);
}