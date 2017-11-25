#version 430
#define PI 3.141592653589793238462643383279502884197169399375105820974

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
uniform mat4 V;
uniform mat4 M;

uniform vec2 waterUVOffset;

in vec3 water_color;
in vec2 coord;

in vec3 camera_vector;

vec3 light_vector = normalize(vec3(10, 1, 0));

void main(void) {
    vec2 coord2 = vec2(gl_FragCoord.xy / vec2(1920, 1080));

    light_vector = normalize(vec3(0, cos(waterUVOffset.x* 200.0), -sin(waterUVOffset.x * 200.0)));

    vec4 unprojected = IP * vec4(0, 0, gl_FragCoord.z, 1.0);
    float water_depth = (unprojected / unprojected.w).z;

    float c = (cos(-waterUVOffset.x * 500.0 + coord.y * 1) + 1) / 2;
    float a = cos(waterUVOffset.x * 2000.0 + coord.y * 10) * c;
    float b = sin(waterUVOffset.x * 2000.0 + coord.y * 10) * (1.0 - c);
    float f = (cos(-waterUVOffset.x * 500.0 + coord.x * 1) + 1) / 2;
    float d = cos(waterUVOffset.x * 1000.0 + coord.x * .2) * f;
    float e = sin(waterUVOffset.x * 1000.0 + coord.x * .2) * (1.0 - f);

    float distance_smooth = (1.0 - pow(gl_FragCoord.z, 5));

    vec3 water_uv = normalize(vec3((d + e) * .04 * distance_smooth, 2, (b + a) * .2 * distance_smooth));
//    water_uv = vec3(0,1,0);

    vec2 water_dudv = water_uv.xz;

    unprojected = IP * vec4(0, 0, texture(refractDepthTexture, coord2).r, 1.0);
    float ground_depth = (unprojected / unprojected.w).z;

    float diff = max(0.0, min(1.0, (water_depth - ground_depth) * .375 + .3));

    float ripple_mag = min((water_depth - ground_depth) * .3, .6);

    vec3 refract_color = texture(refractColorTexture, coord2 + water_dudv * ripple_mag).rgb;
    vec3 reflect_color = texture(reflectColorTexture, coord2 + water_dudv * ripple_mag).rgb;

    refract_color = water_color * diff + refract_color * (1.0 - diff);

    float refract_percent = pow(dot(normalize(camera_vector), vec3(0,1,0)), .5);

    frag_color = vec4(refract_color * refract_percent + reflect_color * (1.0 - refract_percent), 1.0);

    float materialShininess = 1000;
    float lightIntesities = 3;

    vec3 reflected_light_vector = reflect(light_vector, water_uv);
    float cosAngle = max(0.0, dot(normalize(camera_vector), reflected_light_vector));
    float specularCoefficient = pow(cosAngle, materialShininess);

    vec3 specularComponent = specularCoefficient * vec3(1) * lightIntesities;

    frag_color += vec4(specularComponent, 1);

    frag_id = vec3(0);
}