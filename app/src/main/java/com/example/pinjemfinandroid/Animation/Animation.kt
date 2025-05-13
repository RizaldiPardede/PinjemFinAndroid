package com.example.pinjemfinandroid.Animation

import android.animation.ObjectAnimator
import android.view.View

class Animation {
    fun animationslideright(view : View){
        val slideInFromLeft = ObjectAnimator.ofFloat(view, "translationX", -1000f, 0f)
        slideInFromLeft.duration = 500
        slideInFromLeft.start()
    }

    fun animationslideleft(view : View){
        val slideInFromRight = ObjectAnimator.ofFloat(view, "translationX", 1000f, 0f)
        slideInFromRight.duration = 500
        slideInFromRight.start()
    }

    fun animationslidebottom(view : View){
        val slideInFromBottom = ObjectAnimator.ofFloat(view, "translationY", 1000f, 0f)
        slideInFromBottom.duration = 500
        slideInFromBottom.start()

    }

    fun animationPopup(view: View) {
        view.alpha = 0f
        view.scaleX = 0f
        view.scaleY = 0f

        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(400)
            .start()
    }

    fun animationFadeIn(view: View) {
        view.alpha = 0f
        view.visibility = View.VISIBLE

        view.animate()
            .alpha(1f)
            .setDuration(400)
            .start()
    }


}