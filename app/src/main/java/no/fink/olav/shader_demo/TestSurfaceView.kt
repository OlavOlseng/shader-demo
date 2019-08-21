package no.fink.olav.shader_demo

import android.content.Context
import android.opengl.GLSurfaceView
import android.support.v4.view.ViewCompat.setY
import android.support.v4.view.ViewCompat.setX
import android.view.MotionEvent



public class TestSurfaceView(context: Context) : GLSurfaceView(context) {

    val renderer = TestRenderer(context)

    init {
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    private val TOUCH_SCALE_FACTOR = 0.0085f
    private var mPreviousX: Float = 0.toFloat()
    private var mPreviousY: Float = 0.toFloat()

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x = e.x
        val y = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                val dx = x - mPreviousX
                //subtract, so the cube moves the same direction as your finger.
                //with plus it moves the opposite direction.
                renderer.mTransX = renderer.mTransX - dx * TOUCH_SCALE_FACTOR

                val dy = y - mPreviousY
                renderer.mTransY = renderer.mTransY - dy * TOUCH_SCALE_FACTOR
            }
        }

        mPreviousX = x
        mPreviousY = y
        return true
    }

}