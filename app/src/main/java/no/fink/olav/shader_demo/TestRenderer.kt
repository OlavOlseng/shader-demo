package no.fink.olav.shader_demo

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix


class TestRenderer(val context: Context) : GLSurfaceView.Renderer {

    private val Z_NEAR = 1f
    private val Z_FAR = 40f

    var mCube: TestCube? = null //Needs GLContext initilized

    private var mWidth: Int = 0
    private var mHeight: Int = 0


    private val mMVPMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)

    public var mAngle = 0f
    public var mTransY = 1f
    public var mTransX = 1f


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        Log.d(LOG_TAG, "SURFACE CREATED!!!")
        GLES20.glClearColor(.9f, .9f, .9f, 1.0f)
        mCube = TestCube(context)
        Log.d(LOG_TAG, "CREATED CUBE!!!")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        Log.d(LOG_TAG, "SURFACE CHANGED!")
        mWidth = width
        mHeight = height
        // Set the viewport
        GLES20.glViewport(0, 0, mWidth, mHeight)
        val aspect = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        Matrix.perspectiveM(mProjectionMatrix, 0, 50f, aspect, Z_NEAR, Z_FAR)
    }


    override fun onDrawFrame(void: GL10?) {
        Log.d(LOG_TAG, "ON DRAW FRAME")
        if (mCube == null) {
            return
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        //need this otherwise, it will over right stuff and the cube will look wrong!
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        // Set the camera position (View matrix)  note Matrix is an include, not a declared method.
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -8f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Create a rotation and translation for the cube
        Matrix.setIdentityM(mModelMatrix, 0)

        //move the cube up/down and left/right
        Matrix.translateM(mModelMatrix, 0, mTransX, mTransY, 0f)

        //mangle is how fast, x,y,z which directions it rotates.
        Matrix.rotateM(mModelMatrix, 0, mAngle, 1.0f, 1.0f, 1.0f)

        // combine the model with the view matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)

        // combine the model-view with the projection matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0)

        mCube?.apply {
            draw(mMVPMatrix)
        }

        //change the angle, so the cube will spin.
        mAngle += .4f
    }

    companion object {

        const val LOG_TAG = "TestRenderer.Companion"

        fun loadShader(type: Int, shaderSrc: String): Int {
            val shaderHandle: Int?

            shaderHandle = GLES20.glCreateShader(type)

            GLES20.glShaderSource(shaderHandle, shaderSrc)
            GLES20.glCompileShader(shaderHandle)

            val compiled = IntArray(1) { 0 }

            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compiled, 0)

            if (compiled[0] == 0) {
                Log.e(LOG_TAG, "Error!!")
                Log.e(LOG_TAG, GLES20.glGetShaderInfoLog(shaderHandle))
                Log.e(LOG_TAG, shaderSrc)
                GLES20.glDeleteShader(shaderHandle)
                return 0
            }
            return shaderHandle
        }
    }
}
