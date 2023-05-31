package com.example.accessibilityservice
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.KeyEvent.KEYCODE_VOLUME_UP
import android.view.accessibility.AccessibilityEvent


 class MyAccessibilityService : AccessibilityService() {
     private val TAG = "MyAccessibilityService"
     //private lateinit var mainActivity: MainActivity

     override fun onInterrupt() {
         Log.v(TAG, "onInterrupt: something went wrong")
     }

     override fun onDestroy() {
         Log.v(TAG, "Accessibility service off")

     }

//     override fun onKeyEvent(event: KeyEvent?): Boolean {
//         if(event==null) {Log.v(TAG,"null get")}
//         var keycode = event?.keyCode
//         Log.v(TAG,"Key-event Detected")
//         Log.v(TAG,"Keycode:$keycode")
//         if (keycode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//             if (event?.action == KeyEvent.ACTION_UP) {
//                 Log.v(TAG, "Volume Down button pressed")
//
//             }
//             return true
//         } else if (keycode == KeyEvent.KEYCODE_VOLUME_UP) {
//             if (event?.action == KeyEvent.ACTION_UP) {
//                 Log.v(TAG, "Volume Up button pressed")
//
//             }
//             return true
//         }
//         return super.onKeyEvent(event)
//     }
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
            //Log.v(TAG, "app info is : $applicationInfo")
            val applicationLabel: CharSequence =
                packageManager.getApplicationLabel(applicationInfo)
            Log.v(TAG, "app name is : $applicationLabel")


            if (packageName=="com.whatsapp")
             {// classify the screen of whatsapp
                if(event.className=="com.whatsapp.HomeActivity"){
                    Log.v(TAG,"Current screen is: Home")
                }

                if(event.className=="com.whatsapp.Conversation"){
                    Log.v(TAG,"Current screen is: Chat")
                }

                if(event.className=="com.whatsapp.chatinfo.ContactInfoActivity"
                    ||event.className=="com.whatsapp.group.GroupChatInfoActivity"){
                    Log.v(TAG,"Current screen is: Profile Details")
                }
             }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

     }

     override fun onServiceConnected() {
         val info = AccessibilityServiceInfo()
         info.apply {
             eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
             //or AccessibilityEvent.TYPE_VIEW_FOCUSED or AccessibilityEvent.TYPE_VIEW_CLICKED
             feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
             notificationTimeout = 1000
             flags =
                 AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                         AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS or AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
         }

         this.serviceInfo = info
         Log.v(TAG, "OnServiceConnected:")

     }
 }





