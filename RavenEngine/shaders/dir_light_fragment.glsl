#version 400
#define NUM_SAMPLES 4

layout (std140) uniform DirectionalLight
{
    mat4 view;
    mat4 projection;
    vec3 color;
    float intensity;
    vec3 direction;
    float shadow_transparency;
    vec3 ambient;
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

layout(location = 0) out vec3 frag_light;

uniform sampler2D colorTexture;
uniform sampler2D normalTexture;
uniform sampler2D depthTexture;

uniform sampler2D shadowTexture;

in vec2 coord;

//const int shadowSampleCount = 3;
//const float shadowStep = .0005 * 3.0 / shadowSampleCount;

ivec2 int_coord = ivec2(gl_FragCoord.xy);

void main(void) {
    // unpack data
    vec4 ct = texture(colorTexture, coord).rgba;
    vec3 color = ct.rgb;

    vec4 nt = texture(normalTexture, coord).rgba;
    vec3 normal = nt.xyz * 2.0 - 1.0;

    float depth = texture(depthTexture, coord).r;
    vec3 coord2 = vec3(coord, depth) * 2.0 - 1.0;
    vec4 projectedPos = vec4(coord2, 1f);

    vec4 worldPos = matrix.inverse_projection_view * projectedPos;
    worldPos = worldPos.xyzw / worldPos.w;

    // check if lit
    vec4 shadowCoord = (light.projection * light.view * worldPos.xyzw) * .5 + .5;
    float percentage = 0;
    float distance = 0.0;

    int shadowSampleCount = 3;
    float shadowStep = .00025 * 3.0 / shadowSampleCount;
    vec2 shadowOffset = vec2(-shadowStep * (shadowSampleCount * .5 - .5));
    int blockers = 0;

    for (int x = 0; x < shadowSampleCount; x++) {
        shadowOffset.y = -shadowStep * (shadowSampleCount * .5 - .5);

        for (int y = 0; y < shadowSampleCount; y++) {

            float z = texture(shadowTexture, shadowCoord.xy + shadowOffset).z;

            if (z < shadowCoord.z) {
                blockers++;
                distance += z;
            }

            shadowOffset.y += shadowStep;
        }

        shadowOffset.x += shadowStep;
    }

    // if lit
    if (blockers != 0) {
        distance /= blockers;

        distance = min(.001, .005 * (shadowCoord.z - distance) / distance);

        shadowSampleCount = 3;
        float shadowStep2 = distance * 3.0 / shadowSampleCount;
        shadowOffset = vec2(-shadowStep2 * (shadowSampleCount * .5 - .5));
        for (int x = 0; x < shadowSampleCount; x++) {
            shadowOffset.y = -shadowStep2 * (shadowSampleCount * .5 - .5);

            for (int y = 0; y < shadowSampleCount; y++) {

                percentage += texture(shadowTexture, shadowCoord.xy + shadowOffset).r > shadowCoord.z ? 1.0 : 0.0;

                shadowOffset.y += shadowStep2;
            }

            shadowOffset.x += shadowStep2;
        }

        percentage /= (shadowSampleCount * shadowSampleCount);
    } else {
        percentage = 1.0;
    }

    // light
	float NdotL = dot(normalize((matrix.view * vec4(normal, 0.0)).xyz), normalize(matrix.view * vec4(light.direction, 0.0)).xyz);

    percentage = mix(NdotL, percentage, 1.0 - NdotL * light.shadow_transparency);

    frag_light = color * light.ambient + max(vec3(0.0), color * light.color * NdotL * percentage * light.intensity);
}