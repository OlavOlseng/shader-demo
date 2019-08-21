uniform mat4 uMVPMatrix;
uniform vec4 uColor;

attribute vec4 aPosition;

varying vec4 vColor;

void main()
{
    vColor = uColor;
    gl_Position = uMVPMatrix * aPosition;
}