package com.xp.app.info

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.system.exitProcess

const val TAG = "12345"

class MainActivity : AppCompatActivity() {
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Thread {
                Looper.prepare()
                Toast.makeText(applicationContext, Log.getStackTraceString(e), Toast.LENGTH_LONG)
                    .show()
                Looper.loop()

            }.start()
            Thread.sleep(6000)
            exitProcess(0)
        }


        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1001
        )


        val configuration: Configuration = resources.configuration
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            val default = Locale.getDefault()

            val locales = configuration.locales
            val locale = locales[0]
            Log.i(TAG, "onCreate: 目前的设备语言是1：${locale}")
            Log.i(TAG, "onCreate: 目前的设备语言是：${locale.language}")
        } else {
            val locale = configuration.locale
            val language = locale.language
            Locale.CANADA
            Log.i(TAG, "onCreate: 目前的设备语言是1：${locale}")
            Log.i(TAG, "onCreate: 目前的设备语言是：$language")
        }


    }

    fun doClick(view: View) {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap!!)
            canvas.drawColor(Color.RED)
        }



        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            val insertImage = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "Image.png",
                "This is an image"
            )
            Log.d(TAG, "doClick: insertImage:$insertImage")
            val apply = Intent(Intent.ACTION_SEND).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(Intent.EXTRA_STREAM, Uri.parse(insertImage))
                type = "image/*"
            }


            val queryIntentActivities =
                packageManager.queryIntentActivities(apply, PackageManager.MATCH_ALL)
            if (queryIntentActivities.isNotEmpty()) {
                queryIntentActivities.forEach {
                    Log.d(TAG, "doClick: forEach*************************************")
                    Log.d(
                        TAG,
                        "queryIntentActivities1: ${
                            it.activityInfo.applicationInfo.loadLabel(packageManager)
                        }"
                    )
                    Log.d(
                        TAG,
                        "queryIntentActivities2: ${
                            it.activityInfo.loadLabel(packageManager)
                        }"
                    )
                    Log.d(
                        TAG,
                        "queryIntentActivities3: ${
                            it.loadLabel(packageManager)
                        }"
                    )
                    Log.d(
                        TAG,
                        "queryIntentActivities4: ${
                            it.activityInfo.name
                        }"
                    )
                    if (it.activityInfo.name == "com.tencent.mm.ui.tools.ShareToTimeLineUI") {
                        apply.setClassName(it.activityInfo.packageName, it.activityInfo.name)
                    }
                }
            }


            startActivity(Intent.createChooser(apply, "分享APP"));
        } else {


            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
                put(MediaStore.Images.Media.DISPLAY_NAME, "Image.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.TITLE, "Image.png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test")
            }

            val external: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val resolver: ContentResolver = contentResolver
            val insertUri = resolver.insert(external, values)

            var os: OutputStream? = null
            try {
                if (insertUri != null) {
                    os = resolver.openOutputStream(insertUri)
                }
                if (os != null) {
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 90, os)
                    // write what you want
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    os?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            val apply = Intent(Intent.ACTION_SEND).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(Intent.EXTRA_STREAM, insertUri)
                type = "image/*"
            }

            val queryIntentActivities =
                packageManager.queryIntentActivities(apply, PackageManager.MATCH_ALL)
            if (queryIntentActivities.isNotEmpty()) {
                queryIntentActivities.forEach {
                    Log.d(TAG, "doClick: forEach*************************************")
                    Log.d(
                        TAG,
                        "图片: ${
                            it.activityInfo.applicationInfo.loadLabel(packageManager)
                        }"
                    )
                    Log.d(
                        TAG,
                        "图片: ${
                            it.activityInfo.loadLabel(packageManager)
                        }"
                    )
                    Log.d(
                        TAG,
                        "图片: ${
                            it.loadLabel(packageManager)
                        }"
                    )
                    Log.d(
                        TAG,
                        "图片: ${
                            it.activityInfo.name
                        }"
                    )
                }
            }



            startActivity(Intent.createChooser(apply, "分享APP"));
        }
    }

    fun doClick2(view: View) {
        var pack = "com.ytys.app.gappx.itest"


        val installed = AppUtils.isInstalled(this, pack)
        Log.d(TAG, "doClick2: installed:$installed")


        val intent = Intent(Intent.ACTION_MAIN).apply {
            `package` = pack
            setClassName(pack, "com.ytys.app.gappx.MainActivity")
//            setClassName(pack, "com.ytys.app.gappx.MainActivity2")
//            addCategory(Intent.CATEGORY_LAUNCHER)
        }


        val launchIntentForPackage =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)

        Log.d(TAG, "doClick2: ${launchIntentForPackage.size}")


        launchIntentForPackage?.map {
            Log.d(TAG, "doClick2: ${it.activityInfo.name}")
        }


        val resolveActivity = intent.resolveActivity(packageManager)
        Log.d(TAG, "doClick2: resolveActivity:${resolveActivity.className}")

        val createChooser = Intent.createChooser(intent, "titlevalue")

        Log.d(TAG, "doClick2: createChooser:${createChooser?.extras?.toString()}")
        Log.d(TAG, "doClick2: createChooser:${createChooser?.clipData}")


        try {
            startActivityForResult(intent, 1001)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "doClick2: 打开失败${e.message}")
        }
//        launchIntentForPackage?.run {
//            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivityForResult(this, 1001)
//        }


//        val apply = Intent(Intent.ACTION_SEND).apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            putExtra(Intent.EXTRA_STREAM, insertUri)
//            type = "image/*"
//        }
//        startActivity(Intent.createChooser(apply, "分享APP"));
    }

    fun doClick3(view: View) {

        val apply = Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_TEXT, "我是要分享的文案")
            type = "text/*"
        }
        val createChooser = Intent.createChooser(apply, "分享APP文案")
        Log.d(TAG, "doClick3: createChooser:$createChooser")
        Log.d(TAG, "doClick3: createChooser:${createChooser.categories}")

        val queryIntentActivities =
            packageManager.queryIntentActivities(apply, PackageManager.MATCH_ALL)
        if (queryIntentActivities.isNotEmpty()) {
            queryIntentActivities.forEach {
                Log.d(TAG, "doClick: forEach*************************************")
                Log.d(
                    TAG,
                    "文字: ${
                        it.activityInfo.applicationInfo.packageName
                    }"
                )
                Log.d(
                    TAG,
                    "文字: ${
                        it.activityInfo.applicationInfo.loadLabel(packageManager)
                    }"
                )
                Log.d(
                    TAG,
                    "文字: ${
                        it.activityInfo.loadLabel(packageManager)
                    }"
                )
                Log.d(
                    TAG,
                    "文字: ${
                        it.loadLabel(packageManager)
                    }"
                )
                Log.d(
                    TAG,
                    "文字: ${
                        it.activityInfo.name
                    }"
                )
            }
        }
        startActivity(Intent.createChooser(apply, "分享APP文案"));


    }

    fun doClick4(view: View) {
        startActivity(Intent(this, SlideActivity::class.java))
    }
}