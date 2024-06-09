package com.example.easymovie.interfaces

import android.view.KeyEvent

interface OnFragmentKeyListener {
    fun onKeyEvent(keyCode: Int, event: KeyEvent): Boolean
}