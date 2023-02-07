package xyz.savvamirzoyan.musicplayer

import android.content.Context
import android.content.Intent
import android.util.Log
import kotlin.system.exitProcess

class GlobalExceptionHandler private constructor(
    private val applicationContext: Context,
    private val defaultHandler: Thread.UncaughtExceptionHandler,
    private val activityToBeLaunched: Class<*>
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        try {
            launchActivity(applicationContext, activityToBeLaunched, p1)
            exitProcess(0)
        } catch (e: Exception) {
            defaultHandler.uncaughtException(p0, p1)
        }
    }

    private fun launchActivity(
        context: Context,
        activity: Class<*>,
        exception: Throwable
    ) {
        val crashedIntent = Intent(context, activity)
            .also { it.putExtra(INTENT_DATA_NAME, "TEST TEST") }

        crashedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        crashedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(crashedIntent)
    }

    companion object {
        private const val INTENT_DATA_NAME = "CrashData"

        fun initialize(
            context: Context,
            activityToBeLaunched: Class<*>
        ) {
            val handler = GlobalExceptionHandler(
                context,
                Thread.getDefaultUncaughtExceptionHandler() as Thread.UncaughtExceptionHandler,
                activityToBeLaunched
            )

            Thread.setDefaultUncaughtExceptionHandler(handler)
        }

        fun getThrowable(intent: Intent): Throwable? {
            return try {
                Log.d("GlobalExceptionHandler", "nothing")
                null
//                Gson().fromJson(intent.getStringExtra(INTENT_DATA_NAME), Throwable::class.java)
            } catch (e: Exception) {
                Log.e("GlobalExceptionHandler", "getThrowable: ", e)
                null
            }
        }
    }
}