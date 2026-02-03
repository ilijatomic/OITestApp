package com.example.iotestapp.ui.transactions

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.iotestapp.R
import com.example.iotestapp.domain.model.Product

private const val CHANNEL_ID = "product_low_stock_notif"
private const val DEFAULT_NOTIFICATION_ID = 1001

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun showTransactionAddedNotification(context: Context, product: Product) {
    createNotificationChannelIfNeeded(context)

    val contentText = context.getString(
        R.string.transaction_notification_text,
        product.name, product.currentStockLevel, product.minimumStockLevel
    )
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.stat_sys_warning)
        .setContentTitle(context.getString(R.string.transaction_notification_title))
        .setContentText(contentText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(product.id?.toInt() ?: DEFAULT_NOTIFICATION_ID, notification)
}

private fun createNotificationChannelIfNeeded(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val channel = NotificationChannel(
        CHANNEL_ID,
        context.getString(R.string.transaction_notification_title),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply { description = context.getString(R.string.transaction_notification_title) }

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
}
