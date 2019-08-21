package no.fink.olav.shader_demo

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log


public class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val glesVersion = if (supportsOpenGLES20()) {
            "v2.0"
        } else {
            "not v2.0"
        }

        Log.d("MainActivity", glesVersion)

        setContentView(TestSurfaceView(this))

    }

    private fun supportsOpenGLES20(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = am.deviceConfigurationInfo
        return info.reqGlEsVersion >= 0x20000
    }

}

