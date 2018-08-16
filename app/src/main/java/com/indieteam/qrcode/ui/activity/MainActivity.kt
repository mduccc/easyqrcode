package com.indieteam.qrcode.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PowerManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.indieteam.qrcode.R
import com.indieteam.qrcode.R.layout.activity_main
import com.indieteam.qrcode.device.DetectedCamera
import com.indieteam.qrcode.device.GetListCameraDevice
import com.indieteam.qrcode.device.PERMISSIONS_REQUEST_CAMERA
import com.indieteam.qrcode.device.Permission
import com.indieteam.qrcode.process.callback.CameraOpenCallback
import com.indieteam.qrcode.process.callback.CameraPreviewSessionCallback
import com.indieteam.qrcode.process.callback.OnFrameListen
import com.indieteam.qrcode.process.mlkit.barcodes.BarCode
import com.indieteam.qrcode.ui.camera.CameraPreview
import com.indieteam.qrcode.ui.draw.Draw
import com.indieteam.qrcode.ui.fragment.ResultFragment
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
open class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var mBackgroundThread: HandlerThread
    lateinit var mBackgroundHandler: Handler
    lateinit var captureRequestForPreview: CaptureRequest.Builder
    lateinit var cameraCaptureSessionForPreview: CameraCaptureSession
    private lateinit var camID : List<String>
    lateinit var mCamera :CameraDevice
    private lateinit var manager: CameraManager
    private lateinit var characteristics: CameraCharacteristics
    var widthPixels = 0
    var heightPixels = 0
    var camOutputSizeWidth = 0
    var camOutputSizeHeight = 0
    private var sensorOrientation = 0
    var cameraOpen = 0
    lateinit var imageReader: ImageReader
    lateinit var imageOnFrame: Image
    private var quality = 4
    var useCamera = ""
    private lateinit var wakeScreen: PowerManager.WakeLock
    lateinit var draw: Draw
    var barCode = BarCode()
    var checkMlCallback = 1
    val resultFragment = ResultFragment()
    lateinit var cameraPreview: CameraPreview
    var j = 0

    companion object {
        val ORIENTATIONS = SparseIntArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(activity_main)
        val detectedCam = DetectedCamera(this)
        //val getListCam = getListCameraDevice(this)
        val permissions = Permission(this)
        /*--detectedCamera--*/
        if (detectedCam.checkCamereHardware()){
            if (permissions.getPermissionCam()){
                init()
                mytextureView.surfaceTextureListener = TextureListener()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Permission request was denied.
                Toast.makeText(this, "permission not granded", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    inner class TextureListener: TextureView.SurfaceTextureListener{
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            open()
        }
    }

    private fun init(){
        ORIENTATIONS.let {
            it.append(Surface.ROTATION_0, 90)
            it.append(Surface.ROTATION_90, 0)
            it.append(Surface.ROTATION_180, 270)
            it.append(Surface.ROTATION_270, 180)
        }

        val power = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeScreen = power.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "wakeScreen")
        wakeScreen.acquire(10*60*1000L /*10 minutes*/)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthPixels = displayMetrics.widthPixels
        heightPixels = displayMetrics.heightPixels

        manager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val getListCam = GetListCameraDevice(this)
        camID = getListCam.getList()
        useCamera = camID[0]
    }

    fun open(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST_CAMERA)
        }else{
            try {
                manager.openCamera(useCamera, CameraOpenCallback(this), null)
            }catch (e: CameraAccessException){
                Toast.makeText(this, "Err when open", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getSizes(){
        characteristics = manager.getCameraCharacteristics(useCamera)
        val configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val size: Size
        loop@ for (item in configs.getOutputSizes(ImageFormat.JPEG)) {
            when((item.width/4)*3){
                item.height ->{
                    Log.d("ratio ouput size", "${item.width}/${item.height}")
                    size = item
                    camOutputSizeWidth = size.width
                    camOutputSizeHeight = size.height
                    break@loop
                }
                else->{
                    Log.d("ratio ouput size", "${item.width/item.height}")
                    size = item
                    camOutputSizeWidth = size.width
                    camOutputSizeHeight = size.height
                    break@loop
                }
            }
        }
        sensorOrientation = SCREEN_ORIENTATION_PORTRAIT
        imageReader = ImageReader.newInstance(camOutputSizeWidth/quality, camOutputSizeHeight/quality, ImageFormat.YUV_420_888, 1)

        imageReader.setOnImageAvailableListener(OnFrameListen(this), mBackgroundHandler)
    }

    fun done(result: String){
        val bundle = Bundle()
        bundle.putString("result", result)
        resultFragment.arguments = bundle
        if(supportFragmentManager.findFragmentByTag("result_fragment") == null) {
            supportFragmentManager.beginTransaction().add(R.id.rl_main_act, resultFragment, "result_fragment")
                    .addToBackStack(null)
                    .commit()
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.rl_main_act, resultFragment, "result_fragment")
                    .addToBackStack(null)
                    .commit()
        }
        cancel()
    }

    var i = 0
    override fun onResume(){
        super.onResume()
        i++
        start()
        if(supportFragmentManager.findFragmentByTag("result_fragment") == null && i>1) {
            checkMlCallback = 1
            open()
        }
    }

    override fun onPause() {
        super.onPause()
        cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            wakeScreen.release()
            cancel()
        }catch (e: Exception){}
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentByTag("result_fragment") != null){
            super.onBackPressed()
            checkMlCallback = 1
            start()
            open()
        }
    }

    private fun start(){
        mBackgroundThread = HandlerThread("Camera Background")
        mBackgroundThread.start()
        mBackgroundHandler = Handler(mBackgroundThread.looper)
    }

    private fun cancel(){
        try {
            mBackgroundHandler.removeCallbacks(mBackgroundThread)
            mBackgroundThread.quitSafely()
        }catch (e: Exception){

        }
        try{
            mCamera.close()
            cameraOpen = 0
        }catch (e: UninitializedPropertyAccessException){
            e.printStackTrace()
        }
        try {
            mBackgroundThread.join()
        }catch (e: InterruptedException){
            e.printStackTrace()
        }
    }
}
