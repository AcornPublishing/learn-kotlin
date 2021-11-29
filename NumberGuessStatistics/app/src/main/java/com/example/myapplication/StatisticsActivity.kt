package com.example.myapplication

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.statistics.Statistics

class StatisticsActivity : AppCompatActivity() {

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        restoreData(savedInstanceState) // new!
        showData(Statistics.getStatistics())
    }

    fun restoreData(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            getSerializable("statistics.data")?.run {
                Statistics.data.clear()
                Statistics.data.addAll( this as
                        ArrayList<Statistics.GameSessionRecord>)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("statistics.data",
            Statistics.data)
    }

    fun showData(s:List<String>) {
        val container = findViewById<ViewGroup>(
            R.id.statisticsContainer)
        container.removeAllViews()
        s.forEach {line ->
            container.addView(TextView(this).apply {
                text = line
            })
        }
    }
}