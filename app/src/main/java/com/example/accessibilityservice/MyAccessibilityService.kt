package com.example.accessibilityservice
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.AccessibilityServiceInfo.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi


class MyAccessibilityService : AccessibilityService() {
    private val TAG = "MyAccessibilityService"



    override fun onInterrupt() {
        Log.v(TAG, "onInterrupt: something went wrong")
    }

    override fun onDestroy() {
        Log.v(TAG, "Accessibility service off")

    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        var keycode = event?.keyCode
        var Activescreen:String? = " "
        var message : String?
        var time :String?
        var Me:Int=0
        var groupchat = 0
        if (keycode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (event?.action == KeyEvent.ACTION_UP) {
                Log.v(TAG, "Volume Down button pressed")
                val currentNode = rootInActiveWindow
                if (currentNode != null) {
                    //Log.v(TAG, "currentnode = $currentNode")

                    val packageName = currentNode.packageName?.toString()
                    Log.v(TAG, "Package Name: $packageName")
                    val packageManager = this.packageManager
                    if (packageName != null) {
                        try {
                            val applicationInfo: ApplicationInfo =
                                packageManager.getApplicationInfo(packageName, 0)
                            //Log.v(TAG, "app info is : $applicationInfo")
                            val applicationLabel: CharSequence =
                                packageManager.getApplicationLabel(applicationInfo)
                            Log.v(TAG, "app name is : $applicationLabel")
                                if (packageName == "com.whatsapp") {
                                    //Contact Profile
                                    if (currentNode.getChild(0).className == "android.widget.ListView") {
                                        val ContactInfo = mutableListOf<String>()
                                        currentNode.getChild(0).getChild(1).text?.toString()?.let { ContactInfo.add(it) }
                                        currentNode.getChild(0).getChild(2).text?.toString()?.let { ContactInfo.add(it) }
                                        Log.v(TAG, "Current Active Screen : Profile Screen")
                                        Log.v(TAG, "Contact Details : $ContactInfo")

                                    } else if (currentNode.childCount >= 2 && currentNode.getChild(1)?.text == "WhatsApp") {
                                        if (currentNode.getChild(6).isSelected) {
                                            //Chat Screen
                                            Activescreen = currentNode.getChild(6).getChild(0).text?.toString()

                                        } else if (currentNode.getChild(7).isSelected) {
                                            //Status Screen
                                            Activescreen = currentNode.getChild(7).getChild(0).text?.toString()

                                        } else if (currentNode.getChild(8).isSelected) {
                                            //Call Screen
                                            Activescreen = currentNode.getChild(8).getChild(0).text?.toString()
                                        }
                                        Log.v(TAG, "Current Active Screen :$Activescreen")

                                    } else if (currentNode.childCount >= 2 && currentNode.getChild(1)?.text == null) {
                                        Activescreen = "Chat Window"

                                        if (currentNode.getChild(1).childCount >= 1) {
                                            val Name = currentNode.getChild(1).getChild(0)?.text?.toString()
                                            Log.v(TAG, "Current Active Screen :$Activescreen")
                                            Log.v(TAG, "Person or Group Name :$Name")

                                            for (l in 0 until currentNode.childCount) {
                                                if (currentNode.getChild(l).className == "android.widget.ListView") {
                                                    val superchildnum = currentNode.getChild(l).childCount

                                                    for (i in 0 until superchildnum) {
                                                        groupchat = 0
                                                        val superchildNode = currentNode.getChild(l).getChild(i)
                                                        message ="null"
                                                        time ="null"
                                                        Me = 0
                                                        val Info = mutableListOf<String>()
                                                        var left = 0

                                                        if (superchildNode.className == "android.view.ViewGroup") {
                                                            val hyperchildnum = superchildNode.childCount

                                                            for (j in 0 until hyperchildnum) {
                                                                val hyperchildNode = superchildNode.getChild(j)

                                                                val boundsInScreen = Rect()
                                                                hyperchildNode.getBoundsInScreen(boundsInScreen)

                                                                if(hyperchildNode.contentDescription == "Read" ||
                                                                    hyperchildNode.contentDescription == "Sent"||
                                                                    hyperchildNode.contentDescription == "Delivered"){
                                                                    Me=1
                                                                }
                                                                if (hyperchildNode.viewIdResourceName == "com.whatsapp:id/name_in_group") {
                                                                    groupchat = 1
                                                                    val subhyperchildcount = hyperchildNode.childCount
                                                                    for (k in 0 until subhyperchildcount) {
                                                                        val subhyperchild = hyperchildNode.getChild(k)

                                                                        if (subhyperchild.className == "android.widget.TextView") {
                                                                            subhyperchild.text?.toString()?.let { Info.add(it) }
                                                                        }
                                                                    }
                                                                } else if (hyperchildNode.className == "android.widget.TextView" &&
                                                                  hyperchildNode.viewIdResourceName == "com.whatsapp:id/message_text") {
                                                                    message = hyperchildNode.text?.toString()
                                                                    left = boundsInScreen.left
                                                                    //Log.v(TAG, "Bounds in Screen = left:$left, top:$top, right:$right, bottom:$bottom")
                                                                } else if (hyperchildNode.className == "android.widget.TextView" &&
                                                                    hyperchildNode.viewIdResourceName == "com.whatsapp:id/date"
                                                                ) {
                                                                    time = hyperchildNode.text?.toString()
                                                                    //Log.v(TAG, "Bounds in Screen = left:$left, top:$top, right:$right, bottom:$bottom")
                                                                }
                                                            }
                                                            //showing chats
                                                            if(Me==1){
                                                              Log.v(TAG, "[Me] : $message")
                                                            }
                                                            else if(groupchat == 1){
                                                                Log.v(TAG, "$Info : $message")
                                                            }
                                                            else if(left == 50){
                                                                Log.v(TAG, "[$Name] : $message")
                                                            }
                                                            else Log.v(TAG, "$message")

                                                            Log.v(TAG,"[Time] : $time")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            return super.onKeyEvent(event)
        }
        // for other key event return false
        return false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }
    // for dynamic control it will just override the accessibility_service_config
    override fun onServiceConnected() {
//        val info = AccessibilityServiceInfo()
//
//        info.apply {
//            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//            feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
//            notificationTimeout = 1000
//            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS or
//                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
//        }
//        this.serviceInfo = info
        Log.v(TAG, "OnServiceConnected:")
    }

}