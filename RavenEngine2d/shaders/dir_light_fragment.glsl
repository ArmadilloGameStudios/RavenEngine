#version 400

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

layout(location = 0) out vec3 frag_light;

uniform sampler2D colorTexture;
uniform sampler2D normalTexture;
uniform sampler2D depthTexture;

uniform sampler2DShadow shadowTexture;

in vec2 coord;

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

    vec4 shadowCoord = (light.projection * light.view * worldPos.xyzw) * .5 + .5;

    // check if lit
    float percentage = texture(shadowTexture, shadowCoord.xyz);

    // light
	float NdotL = max(0.0,
	    dot(
	        normalize((matrix.view * vec4(normal, 0.0)).xyz),
	        normalize(matrix.view * vec4(light.direction, 0.0)).xyz));

    percentage = mix(NdotL, percentage, 1.0 - NdotL * light.shadow_transparency * 2.0);

    frag_light = max(vec3(0.0), color * (light.ambient + light.color * light.intensity * NdotL * percentage));
}