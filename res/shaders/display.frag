#version 450 core
#extension GL_ARB_bindless_texture : require

out vec4 outColor;

uniform vec2 resolution;
uniform int chunkSize;

layout(std430, binding = 0) readonly buffer ChunkBuffer {
    uvec2 textures[];
} chunkBuffer;

int visibleChunkWidth = int(resolution.x / chunkSize);

layout(std430, binding = 1) readonly buffer MaterialColors {
    vec4 list[];
} materialColors;

int getChunkIDX(ivec2 pos) {
    return pos.x + pos.y * visibleChunkWidth;
}

void main(void) {
    ivec2 pixelPosition = ivec2(gl_FragCoord.xy);
    pixelPosition.y = int(resolution.y) - pixelPosition.y;

    const ivec2 chunkPos = ivec2(pixelPosition / chunkSize);
    const ivec2 chunkLocalPixel = pixelPosition % chunkSize;

    const vec2 scaledPixel = chunkLocalPixel / float(chunkSize);

    const int material = int(texture(sampler2D(chunkBuffer.textures[getChunkIDX(chunkPos)]), scaledPixel).r * 255);

    outColor = materialColors.list[material] / 255.0;

    //    if (material == 1) {
    //        outColor = vec3(1, 0, 0);
    //    } else if (material == 2) {
    //        outColor = vec3(0, 1, 0);
    //    } else if (material == 3) {
    //        outColor = vec3(0, 0, 1);
    //    }
}