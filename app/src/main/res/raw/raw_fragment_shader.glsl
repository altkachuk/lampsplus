#ifdef GL_ES
precision highp float;
#else
#define lowp
#define mediump
#define highp
#endif

uniform sampler2D u_TextureUnit;

void main()
{
	gl_FragColor = vec4(0.5, 0.5, 0.5, 0.1);
}