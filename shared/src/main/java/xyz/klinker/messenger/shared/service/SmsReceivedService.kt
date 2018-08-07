package xyz.klinker.messenger.shared.service

import android.app.IntentService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import xyz.klinker.messenger.shared.R
import xyz.klinker.messenger.shared.data.ColorSet
import xyz.klinker.messenger.shared.service.notification.NotificationConstants
import xyz.klinker.messenger.shared.util.*

class SmsReceivedService : IntentService("SmsReceivedService") {

    override fun onHandleIntent(intent: Intent?) {
        if (AndroidVersionUtil.isAndroidO) {
            startForeground()
        }

        SmsReceivedHandler(this).newSmsRecieved(intent)

        if (AndroidVersionUtil.isAndroidO) {
            stopForeground(true)
        }
    }

    private fun startForeground() {
        val notification = NotificationCompat.Builder(this,
                NotificationUtils.SILENT_BACKGROUND_CHANNEL_ID)
                .setContentTitle(getString(R.string.receiving_a_message))
                .setSmallIcon(R.drawable.ic_stat_notify_group)
                .setLocalOnly(true)
                .setColor(ColorSet.DEFAULT(this).color)
                .setOngoing(true)
                .build()

        startForeground(NotificationConstants.FOREGROUND_NOTIFICATION_ID, notification)
    }

    companion object {
        fun start(context: Context, intent: Intent) {
            intent.component = ComponentName("xyz.klinker.messenger",
                    "xyz.klinker.messenger.shared" + ".service.SmsReceivedService")

            if (AndroidVersionUtil.isAndroidO) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}