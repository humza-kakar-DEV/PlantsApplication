package com.example.plantsservicefyp.activity

import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.ActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        blurBitmap(
//            BitmapFactory.decodeResource(resources, R.drawable.plant_image_7)
//        )?.apply {
//            binding.backgroundImage2.setImageBitmap(this)
//            binding.backgroundImage2.scaleType = ImageView.ScaleType.CENTER_CROP
//        }

    }

    //    blurring bitmaps
    fun blurBitmap(bitmap: Bitmap): Bitmap? {

//Let’s create an empty bitmap with the same size of the bitmap we want to blur
        val outBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)


//Instantiate a new Renderscript
        val rs = RenderScript.create(applicationContext)


//Create an Intrinsic Blur Script using the Renderscript
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))


//Create the in/out Allocations with the Renderscript and the in/out bitmaps
        val allIn: Allocation = Allocation.createFromBitmap(rs, bitmap)
        val allOut: Allocation = Allocation.createFromBitmap(rs, outBitmap)


//Set the radius of the blur
        blurScript.setRadius(25f)


//Perform the Renderscript
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)


//Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap)


//recycle the original bitmap
        bitmap.recycle()


//After finishing everything, we destroy the Renderscript.
        rs.destroy()
        return outBitmap
    }
}