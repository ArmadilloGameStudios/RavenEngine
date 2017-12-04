#version 430

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

layout(location = 0) in vec3 vertex_pos;
layout(location = 1) in vec3 vertex_normal;
layout(location = 2) in vec3 vertex_color;

out vec3 light, lightGlow, color;
out float gl_ClipDistance[1];

out float murk;

void main(void)
{
    vec4 world_pos = matrix.model * vec4(vertex_pos, 1.0);

	gl_Position = matrix.projection * matrix.view * world_pos;

	float ambiantLight = .2;

	float NdotL = dot(normalize((matrix.view * matrix.model * vec4(vertex_normal, 0.0)).xyz), normalize(matrix.view * vec4(sunLight.direction, 0.0)).xyz);
	float light_magnitude = max(0.0, NdotL) * .8;

	light = ambiantLight + light_magnitude * sunLight.color * sunLight.intensity;

	color = vertex_color;

	float water_camera_distance = (world_pos.y / normalize(world_pos.xyz - matrix.inverse_view[3].xyz).y);

    murk = -world_pos.y * .3 + water_camera_distance * .2 + .2;

    gl_ClipDistance[0] = (matrix.model * vec4(vertex_pos, 0)).y * -1.0 + .3;
}