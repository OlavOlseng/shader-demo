package no.fink.olav.shader_demo

import android.content.Context
import android.content.res.Resources
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

public class TestResourceLoader() {

    companion object {
        public fun loadShaderResource(context: Context, resourceId: Int): String {
                    return readTextFileFromResource(context, resourceId)
        }

        private fun readTextFileFromResource(context: Context,
                                     resourceId: Int): String {
            val body = StringBuilder()

            try {
                val inputStream = context.resources.openRawResource(resourceId)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)

                bufferedReader.forEachLine {
                    body.append(it)
                    body.append('\n')
                }
            } catch (e: IOException) {
                throw RuntimeException(
                        "Could not open resource: $resourceId", e)
            } catch (nfe: Resources.NotFoundException) {
                throw RuntimeException("Resource not found: $resourceId", nfe)
            }

            return body.toString()
        }
    }
}
