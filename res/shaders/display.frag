#version 450 core

out vec4 outColor;

uniform vec2 resolution;

uniform sampler2D worldImage;

void main(void) {
    vec2 pixelPosition = gl_FragCoord.xy / resolution;
    pixelPosition.y = 1 - pixelPosition.y;

    outColor = texture(worldImage, pixelPosition) * 255.0;
}