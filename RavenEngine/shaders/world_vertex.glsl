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

void main(void)
{
	gl_Position = matrix.projection * matrix.view * matrix.model * vec4(vertex_pos, 1.0);

	float ambiantLight = .2;

	float NdotL = dot(normalize((matrix.view * matrix.model * vec4(vertex_normal, 0.0)).xyz), normalize(matrix.view * vec4(sunLight.direction, 0.0)).xyz);
	float light_magnitude = max(0.0, NdotL) * .8;

	light = ambiantLight + light_magnitude * sunLight.color * sunLight.intensity;

	color = vertex_color;

    gl_ClipDistance[0] = dot(matrix.model * vec4(vertex_pos, 1.0), vec4(0, 1, 0, 0));
}