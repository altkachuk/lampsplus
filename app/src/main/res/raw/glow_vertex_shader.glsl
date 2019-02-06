#ifdef GL_ES
precision highp float;
#else
#define lowp
#define mediump
#define highp
#endif

attribute vec4 a_Position;
attribute vec2 a_Texture;
attribute vec4 a_Normal;
uniform mat4 u_Matrix;


void main()
{
    vec4 tPosition =  u_Matrix * a_Position;
    gl_Position = tPosition;
}