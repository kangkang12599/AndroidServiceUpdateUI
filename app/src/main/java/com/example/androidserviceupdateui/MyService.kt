package com.example.androidserviceupdateui

import android.app.Service
import android.content.Intent
import android.os.*
import android.os.Process

class MyService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                for (i in 1..10){
                    val intent = Intent()
                    intent.action = "com.example.androidserviceupdateui"
                    intent.putExtra("data_passed", i.toString())
                    sendBroadcast(intent)
                    Thread.sleep(2000)
                }
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}