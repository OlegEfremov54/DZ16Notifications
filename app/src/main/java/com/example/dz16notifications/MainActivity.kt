package com.example.dz16notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.dz16notifications.databinding.ActivityMainBinding
import android.Manifest
import androidx.core.app.Person


class MainActivity : AppCompatActivity() {

    private lateinit var toolbarMain: Toolbar
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val Notification_ID = 1
        const val CHANNEL_ID = "channelID"
    }

    var notificationManager: NotificationManager? = null

    private var notificationCounter = 1

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        ActivityResultCallback { permissions ->
            permissions.forEach { permission, isGranted ->
                if (isGranted) Log.d("AAA", "Permission $permission is granted")
                else Log.v("AAA", "Permission $permission is NOT granted")
            }
        }
    )

    private fun checkAndRequestPermission(vararg permissionCodes: String): Boolean {

        var permissionIsGranted = false
        val permissionList = mutableListOf<String>()

        for (permission in permissionCodes) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("aaa", "checkAndRequestPermission: $permission is already granted")
                permissionIsGranted = true
            } else {
                permissionList.add(permission)
            }
        }

        if (permissionList.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionList.toTypedArray())
        }

        return permissionIsGranted
    }

    private fun getBitmapFromResource(drawableResource: Int) : Bitmap {
        val drawable = resources.getDrawable(drawableResource)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0,0,drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)

        return bitmap
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Тулбар
        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        title = " Notifications video"
        toolbarMain.subtitle = " Версия 1.Главный экран"
        toolbarMain.setLogo(R.drawable.diplomamini)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //toolbarMain.setNavigationOnClickListener { onBackPressed() }

        val intent = Intent(this, MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Уведомление", importance)
            notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        binding.removeNotificationBtn.setOnClickListener {
            notificationCounter--
            notificationManager?.cancel(notificationCounter)
        }

        binding.removeNotificationBtn.setOnLongClickListener() {
            notificationCounter = Notification_ID
            notificationManager?.cancelAll()
            true
        }

        binding.simpleNotificationBtn.setOnClickListener {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_simple_notification)
                .setLargeIcon(getBitmapFromResource(R.drawable.ic_simple_notification))
                .setContentTitle(getString(R.string.simple_notification_btn))
                .setContentText("Тут текст обычного уведомления!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(applicationContext)) {
                if (checkAndRequestPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    notify(notificationCounter++, builder.build())
                }
            }
        }

        binding.bigTextStyleNotificationBtn.setOnClickListener {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bigtext_style_notification)
                .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.big_text)))
                .setContentTitle(getString(R.string.big_text_style_notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(applicationContext)) {
                if (checkAndRequestPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    notify(notificationCounter++, builder.build())
                }
            }
        }

        binding.bigPictureStyleNotificationBtn.setOnClickListener {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bigpicture_style_notification)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(getBitmapFromResource(R.drawable.ic_bigpicture_style_notification))
                    .showBigPictureWhenCollapsed(true))
                .setContentTitle(getString(R.string.big_picture_style_notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(applicationContext)) {
                if (checkAndRequestPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    notify(notificationCounter++, builder.build())
                }
            }
        }

        binding.inboxStyleNotificationBtn.setOnClickListener {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_inbox_notification)
                .setContentText("Внутри какой-то список")
                .setSubText("Список")
                .setStyle(NotificationCompat.InboxStyle()
                    .addLine("строка №1")
                    .addLine("строка №2")
                    .addLine("строка №3")
                    .addLine("строка №4"))
                .setContentTitle(getString(R.string.inbox_style_notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(applicationContext)) {
                if (checkAndRequestPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    notify(notificationCounter++, builder.build())
                }
            }
        }

        binding.messagingStyleNotificationBtn.setOnClickListener {
            val user = Person.Builder().setName("Вы").build()
            val personOne = Person.Builder().setName("Леша").build()


            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_messaging_style_notification)
                .setContentText("Внутри какой-то список")
                .setSubText("Список")
                .setStyle(NotificationCompat.MessagingStyle(user)
                    .setConversationTitle("Какая-нибудь переписка")
                    .addMessage("Привет!", System.currentTimeMillis(), personOne)
                    .addMessage("Здорова", System.currentTimeMillis(), user)
                    .addMessage("Как дела?", System.currentTimeMillis(), user)
                    .addMessage("Отлично", System.currentTimeMillis(), personOne)
                )
                .setContentTitle(getString(R.string.messaging_style_notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(applicationContext)) {
                if (checkAndRequestPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    notify(notificationCounter++, builder.build())
                }
            }
        }


        binding.actionNotificationBtn.setOnClickListener {

            val deleteNotificationIntent = Intent(
                this,
                DeleteNotificationReceiver::class.java
            )
            deleteNotificationIntent.apply {
                action = "com.kodiiiofc.urbanuniversity.natificationslecture.ACTION_DISMISS_NOTIFICATION"
                putExtra("NOTIFICATION_ID", notificationCounter)
            }

            val deletePendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                deleteNotificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_action_notification)
                .setLargeIcon(getBitmapFromResource(R.drawable.ic_action_notification))
                .setContentTitle(getString(R.string.action_notification))
                .setContentText("Вы хотите открыть приложение, от которого пришло уведомление?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_check, "Принять", pendingIntent)
                .addAction(R.drawable.ic_cancel, "Отменить", deletePendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(applicationContext)) {
                if (checkAndRequestPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    notify(notificationCounter++, builder.build())
                }
            }
        }

    }

    //Инициализация Меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.infoMenuMain -> {
                Toast.makeText(
                    applicationContext, "Автор Ефремов О.В. Создан 8.1.2025",
                    Toast.LENGTH_LONG
                ).show()
            }

            R.id.exitMenuMain -> {
                Toast.makeText(
                    applicationContext, "Работа приложения завершена",
                    Toast.LENGTH_LONG
                ).show()
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}