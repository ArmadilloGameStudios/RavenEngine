#version 430
layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_glow;
layout(location = 2) out vec3 frag_id;

uniform sampler2D waterColorTexture;
uniform sampler2D waterDepthTexture;

uniform mat4 P;

const mat4 IP = inverse(P);

in vec3 water_color;

in vec2 coord;

void main(void) {
    vec2 coord2 = vec2(gl_FragCoord.xy / vec2(1920, 1080));

    vec4 unprojected = IP * vec4(0, 0, gl_FragCoord.z, 1.0);
    float water_depth = (unprojected / unprojected.w).z;

    vec3 ground_color = texture(waterColorTexture, coord2).rgb;

    unprojected = IP * vec4(0, 0, texture(waterDepthTexture, coord2).r, 1.0);
    float ground_depth = (unprojected / unprojected.w).z;

    float diff = max(0.0, min(1.0, (water_depth - ground_depth) * .4 + .3));

    frag_color = vec4(water_color * diff + ground_color * (1.0 - diff), 1.0);
    frag_id = vec3(0);
}