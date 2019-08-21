package no.fink.olav.shader_demo

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class TestCube(context: Context) {

    private val LOG_TAG: String = "TestCube"

    private var mProgramHandle: Int = 0
    private var mMVPMatrixHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mVertices: FloatBuffer

    private val BYTES_PER_VERTICE = 4

    //initial size of the cube.  set here, so it is easier to change later.
    var size = 0.4f

    var mVerticesData = floatArrayOf(
            ////////////////////////////////////////////////////////////////////
            // FRONT
            ////////////////////////////////////////////////////////////////////
            // Triangle 1
            -size, size, size, // top-left
            -size, -size, size, // bottom-left
            size, -size, size, // bottom-right
            // Triangle 2
            size, -size, size, // bottom-right
            size, size, size, // top-right
            -size, size, size, // top-left
            ////////////////////////////////////////////////////////////////////
            // BACK
            ////////////////////////////////////////////////////////////////////
            // Triangle 1
            -size, size, -size, // top-left
            -size, -size, -size, // bottom-left
            size, -size, -size, // bottom-right
            // Triangle 2
            size, -size, -size, // bottom-right
            size, size, -size, // top-right
            -size, size, -size, // top-left

            ////////////////////////////////////////////////////////////////////
            // LEFT
            ////////////////////////////////////////////////////////////////////
            // Triangle 1
            -size, size, -size, // top-left
            -size, -size, -size, // bottom-left
            -size, -size, size, // bottom-right
            // Triangle 2
            -size, -size, size, // bottom-right
            -size, size, size, // top-right
            -size, size, -size, // top-left
            ////////////////////////////////////////////////////////////////////
            // RIGHT
            ////////////////////////////////////////////////////////////////////
            // Triangle 1
            size, size, -size, // top-left
            size, -size, -size, // bottom-left
            size, -size, size, // bottom-right
            // Triangle 2
            size, -size, size, // bottom-right
            size, size, size, // top-right
            size, size, -size, // top-left

            ////////////////////////////////////////////////////////////////////
            // TOP
            ////////////////////////////////////////////////////////////////////
            // Triangle 1
            -size, size, -size, // top-left
            -size, size, size, // bottom-left
            size, size, size, // bottom-right
            // Triangle 2
            size, size, size, // bottom-right
            size, size, -size, // top-right
            -size, size, -size, // top-left
            ////////////////////////////////////////////////////////////////////
            // BOTTOM
            ////////////////////////////////////////////////////////////////////
            // Triangle 1
            -size, -size, -size, // top-left
            -size, -size, size, // bottom-left
            size, -size, size, // bottom-right
            // Triangle 2
            size, -size, size, // bottom-right
            size, -size, -size, // top-right
            -size, -size, -size // top-left
    )

    init {
        mVertices = ByteBuffer
                .allocateDirect(mVerticesData.size * BYTES_PER_VERTICE)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mVerticesData)
        mVertices.position(0)
        mProgramHandle = createCubeProgram(context)

    }

    private fun createCubeProgram(context: Context): Int {
        val vertexShaderSource = TestResourceLoader.loadShaderResource(context, R.raw.cube_vertex_shader)
        val fragmentShaderSource = TestResourceLoader.loadShaderResource(context, R.raw.cube_fragment_shader)

        val vertexShaderHandle: Int = TestRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource)
        val fragmentShaderHandle: Int = TestRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)

        val programHandle: Int = GLES20.glCreateProgram()

        GLES20.glAttachShader(programHandle, vertexShaderHandle)
        GLES20.glAttachShader(programHandle, fragmentShaderHandle)

        GLES20.glBindAttribLocation(programHandle, 0, "aPosition") //see aPosition in cube_vertex_shader

        GLES20.glLinkProgram(programHandle)

        val linked = IntArray(1)

        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linked, 0)

        if (linked[0] == 0) {
            Log.e(LOG_TAG, "Error linking program:")
            Log.e(LOG_TAG, GLES20.glGetProgramInfoLog(programHandle))
            GLES20.glDeleteProgram(programHandle)
            return 0
        }
        return programHandle
    }

    public fun draw(mvpMatrix: FloatArray) {

        GLES20.glUseProgram(mProgramHandle)

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "uMVPMatrix")
        mColorHandle = GLES20.glGetUniformLocation(mProgramHandle, "uColor")

        val VERTEX_POSITION_INDEX = 0
        mVertices.position(VERTEX_POSITION_INDEX)

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)

        GLES20.glVertexAttribPointer(
                VERTEX_POSITION_INDEX,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                mVertices)

        GLES20.glEnableVertexAttribArray(VERTEX_POSITION_INDEX)

        var bufferPosition = 0
        val verticesPerFace = 6

        GLES20.glUniform4fv(mColorHandle, 1, TestColors.RED, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, bufferPosition, verticesPerFace)
        bufferPosition += verticesPerFace

        GLES20.glUniform4fv(mColorHandle, 1, TestColors.BLUE, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, bufferPosition, verticesPerFace)
        bufferPosition += verticesPerFace

        GLES20.glUniform4fv(mColorHandle, 1, TestColors.GREEN, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, bufferPosition, verticesPerFace)
        bufferPosition += verticesPerFace

        GLES20.glUniform4fv(mColorHandle, 1, TestColors.YELLOW, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, bufferPosition, verticesPerFace)
        bufferPosition += verticesPerFace

        GLES20.glUniform4fv(mColorHandle, 1, TestColors.CYAN, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, bufferPosition, verticesPerFace)
        bufferPosition += verticesPerFace

        GLES20.glUniform4fv(mColorHandle, 1, TestColors.GREY, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, bufferPosition, verticesPerFace)
    }
}