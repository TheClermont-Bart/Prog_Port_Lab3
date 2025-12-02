package ca.bart.u2430136.lab3

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button

class MainActivity : Activity() {

    companion object {

        const val TAG = "MainActivity"

        const val FRAME_RATE = 100
        const val SEC_PER_FRAME = 1f / FRAME_RATE
        const val MS_PER_FRAME = (1000 * SEC_PER_FRAME).toLong()
        const val NS_PER_FRAME = (1000000000 * SEC_PER_FRAME).toLong()

        const val ROTATION_SPEED_DEG_PER_SEC = 30f
    }


    val handler = Handler(Looper.getMainLooper())
    val runnable = Runnable { update() }

    var previousTimeNS = 0L
    var lagNS = 0L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val triangle = findViewById<CustomView>(R.id.triangle)

    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume()")

        previousTimeNS = System.nanoTime()
        requestUpdate()
    }

    fun requestUpdate() {

        handler.postDelayed(runnable, MS_PER_FRAME)
    }

    fun cancelUpdate() {

        handler.removeCallbacks(runnable)
    }


    fun update() {

        //Log.d(TAG, "update()")

        val currentTimeNS = System.nanoTime()
        val elapsedTimeNS = currentTimeNS - previousTimeNS
        previousTimeNS = currentTimeNS
        lagNS += elapsedTimeNS

        while (lagNS >= NS_PER_FRAME)
        {
            findViewById<CustomView>(R.id.triangle).update()

            lagNS -= NS_PER_FRAME
        }

        requestUpdate()
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onResume()")

        cancelUpdate()
    }
}