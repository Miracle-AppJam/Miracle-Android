package com.appjam.miracle.utiles

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.startAnimationWithShow(context: Context, view: View, id: Int) {
    view.visibility = View.VISIBLE  //애니메이션 전에 뷰를 보이게 한다
    view.startAnimation(AnimationUtils.loadAnimation(context, id)) //애니메이션 설정&시작
}

//사라지기
fun Fragment.startAnimationWithHide(context: Context, view: View, id: Int) {
    val exitAnim = AnimationUtils.loadAnimation(context, id)    //애니메이션 설정
    exitAnim.setAnimationListener(HideAnimListener(view))   //리스너를 통해 애니메이션이 끝나면 뷰를 감춘다
    view.startAnimation(exitAnim) //애니메이션 시작
}

internal fun Context.shortToast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

internal fun Context.longToast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()