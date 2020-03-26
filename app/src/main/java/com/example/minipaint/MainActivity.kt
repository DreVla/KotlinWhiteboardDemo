package com.example.minipaint

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.yavski.fabspeeddial.FabSpeedDial
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var fabMenu: FabSpeedDial
    private lateinit var mCanvas: MyCanvasView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCanvas = findViewById(R.id.whiteboard_canvas)
        fabMenu = findViewById(R.id.fab_menu)
        fabMenu.setMenuListener(object : SimpleMenuListenerAdapter() {

            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when(menuItem?.itemId){
                    R.id.tool_erase_all ->{
                        Toast.makeText(applicationContext,"Erase Whiteboard", Toast.LENGTH_SHORT).show()
                        mCanvas.clearCanvas()}
                    R.id.tool_brush ->{
                        Toast.makeText(applicationContext,"Brush", Toast.LENGTH_SHORT).show()
                        mCanvas.changeToBrush()
                    }
                    R.id.tool_pencil ->{
                        Toast.makeText(applicationContext,"Pencil", Toast.LENGTH_SHORT).show()
                        mCanvas.changeToPencil()
                    }
                    R.id.tool_arrow ->{
                        Toast.makeText(applicationContext,"Arrow", Toast.LENGTH_SHORT).show()
                    }
                }
                return false
            }
        })
    }
}
