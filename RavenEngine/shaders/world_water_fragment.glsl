#version 400
#define NUM_SAMPLES 4
#define PI 3.141592653589793238462643383279502884197169399375105820974

layout (std140) uniform DirectionalLight
{
    vec3 color;
    float intensity;
    vec3 direction;
} sunLight;

layout (std140) uniform Matrices
{
    mat4 model;
    mat4 view;
    mat4 reflect_view;
    mat4 inverse_view;
    mat4 projection;
    mat4 inverse_projection;
} matrix;

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec4 frag_normal;
layout(location = 2) out vec3 frag_id;
layout(location = 3) out vec3 frag_complex;

uniform sampler2D refractColorTexture;
uniform sampler2D refractDepthTexture;
uniform sampler2D reflectColorTexture;
uniform sampler2D reflectDepthTexture;

uniform float time;

in vec3 water_color, light;
in vec2 coord;

in vec3 camera_vector;

const int sampleMask = (1 << NUM_SAMPLES) - 1;

void main(void) {
    // create a uv map for the water ripples
    float distance_smooth = (1.0 - pow(gl_FragCoord.z, 5));

    float c = (cos(-time * 500.0 + coord.y * 1) + 1) / 2;
    float a = cos(time * 2000.0 + coord.y * 10) * c;
    float b = sin(time * 2000.0 + coord.y * 10) * (1.0 - c);
    float f = (cos(-time * 500.0 + coord.x * 1) + 1) / 2;
    float d = cos(time * 1000.0 + coord.x * .2) * f;
    float e = sin(time * 1000.0 + coord.x * .2) * (1.0 - f);

    vec4 unprojected = matrix.inverse_projection * vec4(0, 0, gl_FragCoord.z, 1.0);
    float water_depth = (unprojected / unprojected.w).z;

    unprojected = matrix.inverse_projection * vec4(0, 0, texture(refractDepthTexture, coord2).r, 1.0);
    float ground_depth = (unprojected / unprojected.w).z;

    float ripple_mag = min((water_depth - ground_depth) * .275, .6);

    vec3 water_uv = normalize(vec3(
        (d + e) * .03 * distance_smooth * ripple_mag,
        2,
        (b + a) * .06 * distance_smooth * ripple_mag));

    vec4 refract_color_texture = texture(refractColorTexture, coord + water_uv.xz);
    vec3 refract_color = refract_color_texture.rgb;
    float diff = refract_color_texture.a;

    vec3 reflect_color = texture(reflectColorTexture, coord + water_uv.xz * .4).rgb;

    refract_color = (light * water_color) * diff + refract_color * (1.0 - diff);

    float refract_percent = pow(dot(normalize(camera_vector), vec3(0,1,0)), .5);

    frag_color = vec4(refract_color * refract_percent + reflect_color * (1.0 - refract_percent), 1.0);

    float waterShininess = 2000;

    vec3 reflected_light_vector = reflect(-sunLight.direction,  water_uv);
    float cosAngle = max(0.0, dot(normalize(camera_vector), reflected_light_vector));
    float specularCoefficient = pow(cosAngle, waterShininess);

    vec3 specularComponent = specularCoefficient * sunLight.color * sunLight.intensity;

    frag_color += vec4(specularComponent, 0.0);
    frag_normal = vec4(0,1,0,0);
    frag_id = vec3(0);
    frag_complex = vec3((gl_SampleMaskIn[0] != sampleMask) ? 1.0 : 0.0);
}