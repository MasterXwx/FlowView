package com.weex.flowview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.reset -> resetOrientation()
            R.id.add -> addItem()
            R.id.setup -> setUp()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reset.setOnClickListener(this)
        add.setOnClickListener(this)
        setup.setOnClickListener(this)
    }

    private fun resetOrientation() {
        flowView.changeOrientation()
    }

    private fun addItem() {
        flowView.addItem("showItem")
    }

    private fun setUp() {
        flowView.setUpData(listOf("one", "two", "three", "four", "five"))
    }

}