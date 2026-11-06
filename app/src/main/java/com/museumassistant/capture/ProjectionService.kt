package com.museumassistant.capture

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder

class ProjectionService : Service() {
    companion object { const val ACTION_START = "start_projection"; const val RESULT_CODE = "result_code"; const val DATA = "data" }
    private var projection: MediaProjection? = null
    private var display: VirtualDisplay? = null
    private var reader: ImageReader? = null
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_START) {
            createChannel(); startForeground(71, Notification.Builder(this, "capture").setContentTitle("Museum Assistant").setContentText("Screen capture active").setSmallIcon(android.R.drawable.ic_menu_view).build())
            val manager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            val data = if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(DATA, Intent::class.java) else @Suppress("DEPRECATION") intent.getParcelableExtra(DATA)
            val code = intent.getIntExtra(RESULT_CODE, Activity.RESULT_CANCELED)
            if (data != null && code == Activity.RESULT_OK) startCapture(manager.getMediaProjection(code, data))
        }
        return START_NOT_STICKY
    }
    private fun createChannel() { if (Build.VERSION.SDK_INT >= 26) getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel("capture", "Screen capture", NotificationManager.IMPORTANCE_LOW)) }
    private fun startCapture(mp: MediaProjection) {
        projection = mp
        val dm = resources.displayMetrics
        reader = ImageReader.newInstance(dm.widthPixels, dm.heightPixels, android.graphics.PixelFormat.RGBA_8888, 2)
        display = mp.createVirtualDisplay("MuseumCapture", dm.widthPixels, dm.heightPixels, dm.densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, reader!!.surface, null, null)
    }
    fun acquireFrame(): Bitmap? {
        val image = reader?.acquireLatestImage() ?: return null
        val plane = image.planes[0]; val buffer = plane.buffer; val pixelStride = plane.pixelStride; val rowStride = plane.rowStride
        val rowPadding = rowStride - pixelStride * image.width
        val bmp = Bitmap.createBitmap(image.width + rowPadding / pixelStride, image.height, Bitmap.Config.ARGB_8888)
        bmp.copyPixelsFromBuffer(buffer); image.close()
        return Bitmap.createBitmap(bmp, 0, 0, image.width, image.height)
    }
    override fun onDestroy() { display?.release(); reader?.close(); projection?.stop(); super.onDestroy() }
}
