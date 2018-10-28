#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 v_texCoords;
varying vec4 v_color;
uniform sampler2D u_texture;
uniform float u_dissolve;
uniform vec4 u_colorOn;
uniform vec4 u_colorOff;

void main() {
    vec2 tc = v_texCoords;
    vec4 tex = texture2D(u_texture, tc);
    float v = tex.r;
    vec4 mtex = v > u_dissolve ? u_colorOff : u_colorOn;
    gl_FragColor = vec4(mtex.rgb, mtex.a * v_color.a);
}
