package com.appjam.miracle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.appjam.miracle.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var myView: MyPaintView? = null
    var count = 0
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "간단 그림판"
        myView = MyPaintView(this)
        R.id.paintLayout


        binding.paintLayout.addView(myView)
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.btnRed -> myView!!.mPaint.color = Color.RED
                R.id.btnGreen -> myView!!.mPaint.color = Color.GREEN
                R.id.btnBlue -> myView!!.mPaint.color = Color.BLUE
            }
        }
        val btnTh = findViewById<Button>(R.id.btnTh)
        btnTh.setOnClickListener {
            if (count % 2 == 1) {
                btnTh.text = "Thin"
                myView!!.mPaint.strokeWidth = 10f
                count++
            } else {
                btnTh.text = "Thick"
                myView!!.mPaint.strokeWidth = 20f
                count++
            }
        }
        (findViewById<View>(R.id.btnClear) as Button).setOnClickListener {
            myView!!.mBitmap!!.eraseColor(Color.TRANSPARENT)
            myView!!.invalidate()
        }
    }

    private class MyPaintView(context: Context?) : View(context) {
        var mBitmap: Bitmap? = null
        private var mCanvas: Canvas? = null
        private val mPath: Path
        val mPaint: Paint

        init {
            mPath = Path()
            mPaint = Paint()
            mPaint.color = Color.RED
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = 10f
            mPaint.style = Paint.Style.STROKE
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

            mCanvas = Canvas(mBitmap!!)
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawBitmap(mBitmap!!, 0f, 0f, null) //지금까지 그려진 내용
            canvas.drawPath(mPath, mPaint) //현재 그리고 있는 내용
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mPath.reset()
                    mPath.moveTo(x.toFloat(), y.toFloat())
                }

                MotionEvent.ACTION_MOVE -> mPath.lineTo(x.toFloat(), y.toFloat())
                MotionEvent.ACTION_UP -> {
                    mPath.lineTo(x.toFloat(), y.toFloat())
                    mCanvas!!.drawPath(mPath, mPaint) //mBitmap 에 기록
                    mPath.reset()
                }
            }
            this.invalidate()
            return true
        }
    }
}