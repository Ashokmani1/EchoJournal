package com.sample.echojournal.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.sample.echojournal.MainActivity
import com.sample.echojournal.R

class RecordWidgetProvider : AppWidgetProvider() {
    
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Initialize widget resources
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(PREF_WIDGET_ENABLED, true)
            .apply()
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Cleanup widget resources
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(PREF_WIDGET_ENABLED)
            .apply()
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        // Clean up individual widget instances
        appWidgetIds.forEach { widgetId ->
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(getWidgetPrefKey(widgetId))
                .apply()
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_QUICK_RECORD -> handleQuickRecord(context)
            ACTION_WIDGET_REFRESH -> refreshWidgets(context)
            AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED -> handleOptionsChanged(context, intent)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val quickRecordIntent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_QUICK_RECORD
            putExtra(EXTRA_START_RECORDING, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId,
            quickRecordIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val views = RemoteViews(context.packageName, R.layout.widget_record).apply {
            setOnClickPendingIntent(R.id.recordButton, pendingIntent)
            updateWidgetAppearance(context, appWidgetId)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun handleQuickRecord(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_START_RECORDING, true)
        }
        context.startActivity(intent)
    }

    private fun refreshWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            android.content.ComponentName(context, RecordWidgetProvider::class.java)
        )
        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun handleOptionsChanged(context: Context, intent: Intent)
    {
        val appWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId)
        }
    }

    private fun RemoteViews.updateWidgetAppearance(context: Context, widgetId: Int)
    {
        // Update widget appearance based on preferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean(getWidgetPrefKey(widgetId), true)
        setInt(R.id.recordButton, "setEnabled", if (isEnabled) 1 else 0)
    }

    private fun getWidgetPrefKey(widgetId: Int) = "widget_$widgetId"

    companion object {
        const val PREFS_NAME = "com.sample.echojournal.widget"
        const val PREF_WIDGET_ENABLED = "widget_enabled"
        const val ACTION_QUICK_RECORD = "com.sample.echojournal.action.QUICK_RECORD"
        const val ACTION_WIDGET_REFRESH = "com.sample.echojournal.action.WIDGET_REFRESH"
        const val EXTRA_START_RECORDING = "start_recording"
    }
}