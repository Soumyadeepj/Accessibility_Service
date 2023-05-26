package com.example.myaccessibilityservice
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.KeyEvent.KEYCODE_VOLUME_UP
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MyAccessibilityService : AccessibilityService() {
    private val TAG = "MyAccessibilityService"
    private var a: Int = 0

    override fun onInterrupt() {
        Log.v(TAG, "onInterrupt: something went wrong")
    }


    override fun onKeyEvent(event: KeyEvent): Boolean {
        var keycode = event.keyCode
        Log.v(TAG,"Keycode:$keycode")
        if (keycode == KEYCODE_VOLUME_DOWN) {
            if (event.action == KeyEvent.ACTION_UP) {
                Log.v(TAG, "Volume Down button pressed")

            }
        } else if (keycode == KEYCODE_VOLUME_UP) {
            if (event.action == KeyEvent.ACTION_UP) {
                Log.v(TAG, "Volume Up button pressed")

            }

        }


        return super.onKeyEvent(event);
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        Log.v(TAG, "onAccessibilityEvent: $event ")

        val packageName = event?.packageName?.toString()
        Log.v(TAG, "Package Name: $packageName")
        val packageManager = this.packageManager
        if (packageName == null)
            return
        try {
            val applicationInfo: ApplicationInfo =
                packageManager.getApplicationInfo(packageName, 0)
            Log.v(TAG, "app info is : $applicationInfo")
            val applicationLabel: CharSequence =
                packageManager.getApplicationLabel(applicationInfo)
            Log.v(TAG, "app name is : $applicationLabel")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

//        // Check if the event package name is WhatsApp
//        if (packageName != "com.whatsapp") {
//            return
//        }
//    val nodeInfo: AccessibilityNodeInfo? = event.source
//
//    try {
//        val findAccessibilityNodeInfosByViewId = nodeInfo?.findAccessibilityNodeInfosByViewId("com.whatsapp:id/conversation_contact_name")
//
//        if (findAccessibilityNodeInfosByViewId != null && findAccessibilityNodeInfosByViewId.isNotEmpty()) {
//            val parent = findAccessibilityNodeInfosByViewId[0]
//            val contactName = parent.text?.toString()
//
//            if (contactName != null && contactName.isNotEmpty()) {
//                // Your code to handle the contact name goes here
//                Log.v(TAG,"contact name:$contactName")
//            }
//        }
//    } catch (e: Exception) {
//        // Handle any exceptions that may occur during the process
//    }






}


    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_VIEW_FOCUSED or AccessibilityEvent.TYPE_VIEW_CLICKED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
            notificationTimeout= 1000
        }

        this.serviceInfo = info
        Log.v(TAG, "OnServiceConnected:")
    }
}

