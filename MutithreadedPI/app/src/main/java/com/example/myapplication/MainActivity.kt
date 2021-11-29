package com.example.myapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    var points = 0L
    var insideCircle = 0L
    var totalIters = 0L

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.run {
            points = getLong("points")
            insideCircle = getLong("insideCircle")
            totalIters = getLong("totalIter")
        }

        val cores = Runtime.getRuntime().
            availableProcessors()
        procs.setText(cores.toString())
    }

    override
    fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.run {
            putLong("points",points)
            putLong("insideCircle",insideCircle)
            putLong("totalIter", totalIters)
            report()
        }
    }

    fun calc(v: View) {
        val t1 = System.currentTimeMillis()
        val nThreads = threads.text.toString().
            takeIf { it != "" }?.toInt()?:1
        val itersNum = iters.text.toString().
            takeIf { it != "" }?.toInt()?:10000
        val itersPerThread = itersNum / nThreads
        val srvc = Executors.newFixedThreadPool(nThreads)
        val callables = (1..nThreads).map {
            object : Callable<Pair<Int, Int>> {
                override fun call(): Pair<Int, Int> {
                    var i = 0
                    var p = 0
                    (1..itersPerThread).forEach {
                        val x = Math.random()
                        val y = Math.random()
                        val r = x*x + y*y
                        i++
                        if(r < 1.0) p++
                    }
                    return Pair(i, p)
                }
            }
        }
        val futures = srvc.invokeAll(callables)
        futures.forEach{ f ->
            val p = f.get()
            points += p.first
            insideCircle += p.second
        }

        val t2 = System.currentTimeMillis()
        calcTime.setText((t2-t1).toString())

        report()
    }

    fun reset(v:View) {
        points = 0
        insideCircle = 0
        report()
    }

    private fun report() {
        cumulIters.setText(points.toString())
        if(points > 0) {
            val pipi = 1.0 * insideCircle / points * 4
            pi.setText(pipi.toString())
        } else {
            pi.setText("")
        }
    }
}

