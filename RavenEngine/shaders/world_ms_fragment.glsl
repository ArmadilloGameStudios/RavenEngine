#version 400
#define NUM_SAMPLES 4

layout(location = 0) out vec3 frag_color;
layout(location = 1) out vec3 frag_id;

layout (std140) uniform DirectionalLight
{
    mat4 view;
    mat4 projection;
    vec3 color;
    float intensity;
    vec3 direction;
    float length;
    vec3 ambient;
    float shadow_transparency;
} light;

layout (std140) uniform Matrices
{
    mat4 model;
    mat4 view;
    mat4 reflect_view;
    mat4 inverse_view;
    mat4 projection;
    mat4 inverse_projection;
    mat4 inverse_projection_view;
} matrix;

uniform vec3 id;
uniform vec4 highlight;

uniform sampler2DShadow shadowTexture;

in vec3 color, normal;
in vec4 world_pos;

const int squared_samples = 8;

void main(void) {
    // shadow
    float percentage = 0;
    for (int i = 0; i < squared_samples; i++) {
        for (int j = 0; j < squared_samples; j++) {
            float io = i - (squared_samples / 2 + .5);
            float jo = j - (squared_samples / 2 + .5);
            float ko = 0;

            // TODO adjust with the normal
            vec3 sampleOffset = (vec3(
                        io * normal.y - jo * normal.x - ko * normal.z,
                        ko * normal.y - io * normal.x - jo * normal.z,
                        jo * normal.y - ko * normal.x - io * normal.z)) *
                .1 / squared_samples;

            // position in the shadow coords
            vec4 shadowCoords = (
                    light.projection *
                    light.view *
                    (world_pos.xyzw + vec4(sampleOffset, 0))) *
                .5 + .5;

            // check if lit
            percentage += texture(shadowTexture, shadowCoords.xyz);
        }
    }

    percentage /= squared_samples*squared_samples;

    // light
	float NdotL = max(0.0,
	    dot(
	        normalize((matrix.view * vec4(normal, 0.0)).xyz),
	        normalize(matrix.view * vec4(light.direction, 0.0)).xyz));

    percentage = mix(NdotL, percentage, 1.0 - NdotL * light.shadow_transparency * 2.0);

	frag_color = max(vec3(0.0), color * (light.ambient + light.color * light.intensity * NdotL * percentage));

    frag_color = mix(frag_color, highlight.xyz, highlight.w);

	frag_id = id;
}