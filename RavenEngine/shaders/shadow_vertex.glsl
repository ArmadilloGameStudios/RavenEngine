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
    mat4 bone[4];
} animation;

layout(location = 0) in vec3 vertex_pos;
layout(location = 4) in int[4] vertex_bones;
layout(location = 5) in float[4] vertex_weights;

void main(void)
{
    mat4 aMat = (animation.bone[vertex_bones[0]] * vertex_weights[0] +
                 animation.bone[vertex_bones[1]] * vertex_weights[1] +
                 animation.bone[vertex_bones[2]] * vertex_weights[2] +
                 animation.bone[vertex_bones[3]] * vertex_weights[3])
                 / (vertex_weights[0] + vertex_weights[1] + vertex_weights[2] + vertex_weights[3]);


    vec4 world_pos = matrix.model * aMat * vec4(vertex_pos, 1.0);

	gl_Position = sunLight.projection * sunLight.view * world_pos;
}