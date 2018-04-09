#version 400

layout(location = 0) out vec3 frag_color;

uniform sampler2D spriteSheet;

in vec2 coords;

void main() {
    vec4 sprite = texture(spriteSheet, coords);
    frag_color = sprite.rgb * sprite.a;
}
