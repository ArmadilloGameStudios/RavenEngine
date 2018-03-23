#version 400

layout (std140) uniform DirectionalLight
{
    mat4 view;
    mat4 projection;
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
    mat4 inverse_projection_view;
} matrix;

layout (std140) uniform Animation
{
    mat4 bone[2];
} animation;

layout(location = 0) in vec3 vertex_pos;

void main(void)
{
    vec4 world_pos = matrix.model * animation.bone[1] * vec4(vertex_pos, 1.0);

	gl_Position = sunLight.projection * sunLight.view * world_pos;
}