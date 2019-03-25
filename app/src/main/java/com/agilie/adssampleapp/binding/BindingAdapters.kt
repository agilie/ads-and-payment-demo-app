package com.agilie.adssampleapp.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.agilie.adssampleapp.glide.GlideApp

@BindingAdapter("loadImage")
fun loadImage(view: AppCompatImageView, imageUrl: String?) {
    if (imageUrl != null) {
        GlideApp.with(view)
            .load(imageUrl)
            .centerCrop()
            .into(view)
    }
}