package com.appjam.miracle.feature.draw

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController

import com.appjam.miracle.R
import com.appjam.miracle.databinding.FragmentDrawBinding
import com.appjam.miracle.utiles.shortToast
import com.appjam.miracle.utiles.startAnimationWithHide
import com.appjam.miracle.utiles.startAnimationWithShow
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DrawFragment: Fragment(), UploadDialogInterface {
    private var myView: MyPaintView? = null
    private var penState: Boolean = true
    private var penColor: Int = Color.RED
    private var dialog: UploadDialog? = null
    var count = 0

    private lateinit var binding: FragmentDrawBinding
    private val viewModel by viewModels<DrawViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDrawBinding.inflate(inflater, container, false)
//        title = "간단 그림판"
        myView = MyPaintView(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.drawSideEffect.collect {
                    when (it) {
                        is DrawSideEffect.SUCCESS -> {
                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                with(binding) {
                                    layoutDraw.visibility = View.GONE
                                    layoutLoading.visibility = View.GONE
                                }
                                requireContext().shortToast("성공")
                            }
                        }
                        is DrawSideEffect.LOADING -> {

                        }
                        is DrawSideEffect.FAILED -> {
                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                with(binding) {
                                    layoutDraw.visibility = View.VISIBLE
                                    layoutLoading.visibility = View.GONE
                                }
                                requireContext().shortToast("로딩에 실패했어요")
                            }
                        }
                    }
                }
            }
        }

        binding.paintLayout.addView(myView)
//        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
//            when (checkedId) {
//                R.id.btnRed -> myView!!.mPaint.color = Color.RED
//                R.id.btnGreen -> myView!!.mPaint.color = Color.GREEN
//                R.id.btnBlue -> myView!!.mPaint.color = Color.BLUE
//            }
//        }
//        val btnTh = findViewById<Button>(R.id.btnTh)
        binding.imageRainbowCricle.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setTitle("색상 선택")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.colorPickerCheckButton),
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d("TAG", "onCreate: ${envelope.color}")
                        if (!penState) {
                            penColor = envelope.color
                        } else {
                            myView!!.mPaint.color = envelope.color
                        }
                    }
                )
                .setNegativeButton(
                    "취소"
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }

        binding.imagePenEraser.setOnClickListener {
            if (!penState) {
                penState = true
                myView!!.mPaint.color = penColor
                binding.imagePenEraser.setImageResource(R.drawable.ic_pen)
            } else {
                penState = false
                penColor = myView!!.mPaint.color
                myView!!.mPaint.color = requireContext().getColor(R.color.background)
                binding.imagePenEraser.setImageResource(R.drawable.ic_eraser)

            }
        }

        binding.textUpload.setOnClickListener {
            dialog = UploadDialog(this)
            dialog?.show(this.childFragmentManager, "UploadDialog")
        }

//        btnTh.setOnClickListener {
//            if (count % 2 == 1) {
//                btnTh.text = "Thin"
//                myView!!.mPaint.strokeWidth = 10f
//                count++
//            } else {
//                btnTh.text = "Thick"
//                myView!!.mPaint.strokeWidth = 20f
//                count++
//            }
//        }

//        (findViewById<View>(R.id.btnClear) as Button).setOnClickListener {
//            myView!!.mBitmap!!.eraseColor(Color.TRANSPARENT)
//            myView!!.invalidate()
//        }
        return binding.root
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

    override fun onYesButtonClick() {
        dialog?.dismiss()

        binding.textLoading.text = "상담을 위해 사진을 \n분석하고있어요"
        binding.layoutDraw.visibility = View.GONE
        binding.layoutLoading.visibility = View.VISIBLE
        startAnimationWithShow(requireContext(), binding.layoutLoading, R.anim.enter)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            startAnimationWithHide(requireContext(), binding.textLoading, R.anim.exit)
            delay(400)
            binding.textLoading.text = "대화를 준비하고있어요"
            binding.textLoading.visibility = View.VISIBLE
            startAnimationWithShow(requireContext(), binding.textLoading, R.anim.enter)
        }

        viewModel.saveImage(
            context = requireContext(),
            bitmap = myView?.mBitmap!!
        )
        viewModel.postImage(
            context = requireContext(),
            bitmap = myView?.mBitmap!!
        )

    }
}