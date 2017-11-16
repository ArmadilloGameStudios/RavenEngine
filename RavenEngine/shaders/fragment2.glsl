#version 430
#define pi 3.1415926535897932384626433832795

layout(location = 0) out vec3 frag_color;

uniform sampler2DMS worldColorTexture;
uniform sampler2DMS worldDepthTexture;
uniform sampler2D waterColorTexture;
uniform sampler2D waterDepthTexture;

uniform sampler2D glowTexture;

uniform mat4 P;
mat4 IP = inverse(P);

uniform vec2 bloomStep;

in vec2 coord;

const int num_samples = 4;

void main(void) {
    ivec2 int_coord = ivec2(coord.x * 1920, coord.y * 1080);

	vec3 water_color = texture(waterColorTexture, coord).rgb;

    // get true water depth
    vec4 unprojected = IP * vec4(0, 0, texture(waterDepthTexture, coord).r, 1.0);
    float water_depth = (unprojected / unprojected.w).z;

    vec3 color = vec3(0.0f);

    for (int i = 0; i < num_samples; i++) {
        // get true word depth
        unprojected = IP * vec4(0, 0, texelFetch(worldDepthTexture, int_coord, i).r, 1.0);
        float world_depth = (unprojected / unprojected.w).z;

        float under_water = (atan((water_depth - world_depth) * 1000000f) * 2f / pi + 1f) / 2f;
        float diff = min(1.0, (water_depth - world_depth) * .4 + .3);
        vec3 world_color = texelFetch(worldColorTexture, int_coord, i).rgb;

        color += world_color * (1f - under_water) +
                      (water_color * diff + (world_color * (1.0 - diff))) * (under_water);
    }

    color /= num_samples;

	frag_color = vec3(color);
}