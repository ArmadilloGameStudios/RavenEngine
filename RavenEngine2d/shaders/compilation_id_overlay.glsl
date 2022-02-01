#version 410

layout(location = 0) out vec4 frag_color;

const float xUnit = 1.0 / 640.0;
const float yUnit = 1.0 / 360.0;
const float step = .2;

uniform sampler2D colorTexture;
uniform vec3 id;

in vec2 texture_coords;

float bump(vec4 color_test, vec4 color) {
    if (color_test != color) {
        return step;
    } else {
        return -step;
    }
}

void main() {
    vec4 color = texture(colorTexture, texture_coords);
    vec4 color_r = texture(colorTexture, texture_coords + vec2(xUnit, 0));
    vec4 color_l = texture(colorTexture, texture_coords + vec2(-xUnit, 0));
    vec4 color_t = texture(colorTexture, texture_coords + vec2(0, yUnit));
    vec4 color_b = texture(colorTexture, texture_coords + vec2(0, -yUnit));
    vec4 color_rt = texture(colorTexture, texture_coords + vec2(xUnit, yUnit));
    vec4 color_lt = texture(colorTexture, texture_coords + vec2(-xUnit, yUnit));
    vec4 color_rb = texture(colorTexture, texture_coords + vec2(xUnit, -yUnit));
    vec4 color_lb = texture(colorTexture, texture_coords + vec2(-xUnit, -yUnit));

    vec3 outline = vec3(1);

//    if (color.a <= 0) {
//        discard;
//    }

    if (color.rgb != id &&
        color_r.rgb != id &&
        color_l.rgb != id &&
        color_t.rgb != id &&
        color_b.rgb != id &&
        color_rt.rgb != id &&
        color_lt.rgb != id &&
        color_rb.rgb != id &&
        color_lb.rgb != id) {
        outline = vec3(0);
    }

    float a = 0;


    if (color_r != color) {
        a += step;
    } else if (color_rt != color && color_t != color ) {
        a += step;
    }

    //    a += bump(color_r, color);
//    a += bump(color_r, color);
//    a += bump(color_l, color);
//    a += bump(color_t, color);
//    a += bump(color_b, color);
//    a += bump(color_rt, color);
//    a += bump(color_lt, color);
//    a += bump(color_rb, color);
//    a += bump(color_lb, color);

//    a = max(0, a);

//    if (a == 0 && color_r != color) {
//        a = step;
//    }

    frag_color = vec4(outline, a);
}