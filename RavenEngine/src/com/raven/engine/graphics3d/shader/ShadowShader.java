package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameProperties;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.GL43.glCopyImageSubData;

/**
 * Created by cookedbird on 12/9/17.
 */
public class ShadowShader extends Shader {

    private static final int size = 128;

    public static final int
            DEPTH = getNextTexture(),
            DEPTH_COPY = getNextTexture();

    private int framebuffer_handel, depth_texture, depth_copy_texture;
    private int quality = 16;

    public ShadowShader() {
        super("shadow_vertex.glsl", "shadow_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        int blockIndex = glGetUniformBlockIndex(getProgramHandel(), "DirectionalLight");
        glUniformBlockBinding(getProgramHandel(), blockIndex, LIGHT);

        blockIndex = glGetUniformBlockIndex(getProgramHandel(), "Matrices");
        glUniformBlockBinding(getProgramHandel(), blockIndex, MATRICES);

        framebuffer_handel = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);

        // Depth
        depth_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D, depth_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32,
                size * quality,
                size * quality,
                0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depth_texture, 0);

        // copy to texture
        depth_copy_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + DEPTH_COPY);
        glBindTexture(GL_TEXTURE_2D, depth_copy_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32,
                size * quality,
                size * quality,
                0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glActiveTexture(GL_TEXTURE0);

        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("World Shader Failed: 0x"
                    + Integer.toHexString(glCheckFramebufferStatus(GL_FRAMEBUFFER)));
        }
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);

        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D, depth_texture);
        glActiveTexture(GL_TEXTURE0 + DEPTH_COPY);
        glBindTexture(GL_TEXTURE_2D, depth_copy_texture);

        glViewport(0, 0, size * quality, size * quality);

        glClearColor(0f,0f,0f,0f);
        glClear(GL_DEPTH_BUFFER_BIT);

        glEnable(GL_DEPTH_TEST);

        glEnableVertexAttribArray(0);

//        glCullFace(GL_FRONT);
//
//        glEnable(GL_POLYGON_OFFSET_FILL);
//        glPolygonOffset(1.1f, 4.0f);
    }


    @Override
    public void endProgram() {
        glCopyImageSubData(
                depth_texture, GL_TEXTURE_2D, 0, 0, 0, 0,
                depth_copy_texture, GL_TEXTURE_2D, 0, 0, 0, 0,
                size * quality, size * quality, 1);

        glActiveTexture(GL_TEXTURE0);

//        glCullFace(GL_BACK);
//        glDisable(GL_POLYGON_OFFSET_FILL);
    }
}
