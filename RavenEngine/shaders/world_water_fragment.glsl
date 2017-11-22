#version 430

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_glow;
layout(location = 2) out vec3 frag_id;

uniform sampler2D refractColorTexture;
uniform sampler2D refractDepthTexture;
uniform sampler2D reflectColorTexture;
uniform sampler2D reflectDepthTexture;

uniform sampler2D waterUVMapTexture;

uniform mat4 P;
const mat4 IP = inverse(P);

uniform vec2 waterUVOffset;

in vec3 water_color;
in vec2 coord;

in vec3 camera_vector;

void main(void) {
    vec2 coord2 = vec2(gl_FragCoord.xy / vec2(1920, 1080));

    vec4 unprojected = IP * vec4(0, 0, gl_FragCoord.z, 1.0);
    float water_depth = (unprojected / unprojected.w).z;

    vec3 water_uv = texture(waterUVMapTexture, coord * .05 + waterUVOffset).rgb;
    vec2 water_dudv = water_uv.rg * 2.0 - 1.0;

    unprojected = IP * vec4(0, 0, texture(refractDepthTexture, coord2).r, 1.0);
    float ground_depth = (unprojected / unprojected.w).z;

    float diff = max(0.0, min(1.0, (water_depth - ground_depth) * .375 + .3));

    float ripple_mag = min((water_depth - ground_depth) * .03, .05);

    vec3 refract_color = texture(refractColorTexture, coord2 + water_dudv * ripple_mag).rgb;
    vec3 reflect_color = texture(reflectColorTexture, coord2 + water_dudv * ripple_mag).rgb;

    refract_color = water_color * diff + refract_color * (1.0 - diff);

    float refract_percent = pow(dot(normalize(camera_vector).xyz, vec3(0,1,0)), .5);

    frag_color = vec4(refract_color * refract_percent + reflect_color * (1.0 - refract_percent), 1.0);

//    frag_color = vec4(water_uv, 1.0);
    frag_id = vec3(0);
}