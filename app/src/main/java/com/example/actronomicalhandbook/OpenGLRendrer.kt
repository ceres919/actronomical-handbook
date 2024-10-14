package com.example.actronomicalhandbook

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class Shape {
    abstract fun draw(mvpMatrix: FloatArray)
}

class OpenGLRenderer(private var context: Context) : GLSurfaceView.Renderer {
    private lateinit var square: Square
    private lateinit var sun: Sun
    private lateinit var moon: Moon
    private lateinit var planets: List<Planet>
    private lateinit var orbits: List<Orbit>

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        square = Square(context)
        sun = Sun(context, 0.6f, R.drawable.sun)
        planets = listOf(
            Planet(context, 0.15f, R.drawable.mercury, 1.0f, 1.1f, 0.1f),
            Planet(context, 0.19f, R.drawable.venus, 1.7f, 0.5f, 0.1f),
            Planet(context, 0.2f, R.drawable.earth, 2.4f, 0.4f, 3f),
            Planet(context, 0.18f, R.drawable.mars, 3.3f, 0.3f, 3f),
            Planet(context, 0.4f, R.drawable.jupiter, 4.8f, 0.22f, 2f),
            Planet(context, 0.3f, R.drawable.saturn, 6f, 0.15f, 2f),
            Planet(context, 0.28f, R.drawable.uranus, 7f, 0.12f, 2f),
            Planet(context, 0.28f, R.drawable.neptune, 8f, 0.08f, 2f),
        )
        moon = Moon(context, 0.05f, R.drawable.moon, planets[2],0.4f, 1.0f)
        orbits = planets.map { Orbit(it.orbitRadius) }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        square.draw()
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        orbits.forEach { it.draw(mvpMatrix) }
        sun.draw(mvpMatrix)
        planets.forEach { it.draw(mvpMatrix) }
        moon.draw(mvpMatrix)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.setLookAtM(viewMatrix, 0, 0f, 3f, -10f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 50f)
    }
}