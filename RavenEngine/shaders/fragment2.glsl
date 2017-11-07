#version 430
#define pi 3.1415926535897932384626433832795

layout(location = 0) out vec3 frag_color;

uniform sampler2DMS worldColorTexture;
uniform sampler2DMS worldDepthTexture;
uniform sampler2D waterColorTexture;
uniform sampler2D waterDepthTexture;

uniform sampler2D glowTexture;

uniform vec2 bloomStep;

in vec2 coord;

const int num_samples = 4;

void main(void) {
    ivec2 int_coord = ivec2(coord.x * 1424, coord.y * 856);

	// vec4 world_color = texture(worldColorTexture, coord).rgba;
	vec3 water_color = texture(waterColorTexture, coord).rgb;
	float water_depth = texture(waterDepthTexture, coord).r;

    vec3 color = vec3(0.0);

//	vec3 glow = texture(glowTexture, coord).rgb;
//
//	vec3 bloomGlow = glow / 3.0;
//
//	for (float i = 1; i < 5.0; i++) {
//		bloomGlow += (texture(glowTexture, coord + bloomStep * i * 2.0).rgb +
//		             texture(glowTexture, coord - bloomStep * i * 2.0).rgb) / (4.0 * i);
//	}
//
//	float glowStrength = min(max(max(bloomGlow.r, bloomGlow.g), bloomGlow.b), 1.0);

    vec3 world_color = vec3(0.0);
    float world_depth = 0.0;

    for (int i = 0; i < num_samples; i++) {
        // world_color += texelFetch(worldColorTexture, int_coord, i).rgb;
	    world_depth = texelFetch(worldDepthTexture, int_coord, i).r;

//	    if (water_depth > world_depth) {
//            color += texelFetch(worldColorTexture, int_coord, i).rgb;
//        } else {
//            float diff = max(min(1.0, (world_depth - water_depth) * 50.0 + .3), 0.0);
//            color += water_color * diff + (texelFetch(worldColorTexture, int_coord, i).rgb * (1.0 - diff));
//        }

        float under_water = (atan((world_depth - water_depth) * 1000000f) * 2f / pi + 1f) / 2f;
        float diff = max(min(1.0, (world_depth - water_depth) * 50.0 + .3), 0.0);

        color += texelFetch(worldColorTexture, int_coord, i).rgb * (1f - under_water) +
                 (water_color * diff + (texelFetch(worldColorTexture, int_coord, i).rgb * (1.0 - diff))) * (under_water);

        // color +=
    }

    color /= num_samples;
    world_depth /= num_samples;

//    if (water_depth > world_depth) {
//        color = world_color;
//    } else {
//        float diff = max(min(1.0, (world_depth - water_depth) * 50.0 + .3), 0.0);
//        color = water_color * diff + (world_color * (1.0 - diff));
//    }

//    vec3 world_color = texelFetch(worldColorTexture, int_coord, 0 ).rgb;
//	float world_depth = texelFetch(worldDepthTexture, int_coord, 0).r;
//
//    if (water_depth > world_depth) {
//            color += world_color.rgb;
//    } else {
//            float diff = min(1.0, (world_depth - water_depth) * 50.0 + .3);
//            color += water_color * diff + (world_color.rgb * (1.0 - diff));
//            // color += water_color.rgb;
//    }



	// frag_color = color; // color * (1.0 - glowStrength) + bloomGlow;
	frag_color = vec3(color);
}