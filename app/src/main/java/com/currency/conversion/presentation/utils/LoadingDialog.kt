package com.currency.conversion.presentation.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.currency.conversion.R

class LoadingDialog(private var context: Context) {

    private val dialog: Dialog = Dialog(context)

    init {
        // Set custom view for the dialog
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        dialog.setCancelable(false) // Prevent dismissal by back press or touch outside
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun showDialog(msg: String) {
//        val builder = AlertDialog.Builder(activity)
//        builder.setView(R.layout.dialog_loading)
//        builder.setCancelable(false)
//        dialog = builder.create()
//        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog?.setCancelable(false)
//        dialog?.setContentView(R.layout.dialog_loading)
//        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val gifImageView = dialog.findViewById<ImageView>(R.id.iv_loading)
        val tvMsg = dialog.findViewById<TextView>(R.id.tv_msg)
        val imageViewTarget = DrawableImageViewTarget(gifImageView)

        if (msg.isNullOrEmpty()) {
//            tvMsg?.text = activity.resources.getString(R.string.loading)
            tvMsg?.text = "Loading..."
        } else {
            tvMsg?.text = msg
        }

        Glide.with(context)
            .load(R.drawable.loading)
            .placeholder(R.drawable.loading)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageViewTarget)
        if (!isShowing()) {
            dialog.show()
        }
    }

    private fun isShowing() = dialog.isShowing

    fun hideDialog() {
        if (isShowing()){
            dialog.dismiss()
        }
    }

}