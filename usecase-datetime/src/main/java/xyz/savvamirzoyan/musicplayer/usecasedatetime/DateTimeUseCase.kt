package xyz.savvamirzoyan.musicplayer.usecasedatetime

import java.util.*
import javax.inject.Inject

interface DateTimeUseCase {

    fun getCurrentPartOfTheDay(): CurrentPartOfDayDomain

    class Base @Inject constructor() : DateTimeUseCase {

        override fun getCurrentPartOfTheDay(): CurrentPartOfDayDomain {

            val current = Calendar.getInstance()
            val currentMillis = current.timeInMillis

            current.set(Calendar.HOUR_OF_DAY, 0)
            current.set(Calendar.MINUTE, 0)
            current.set(Calendar.SECOND, 0)
            current.set(Calendar.MILLISECOND, 0)

            val response = when ((currentMillis - current.timeInMillis) / 1000) {
                in 18_000..43_200 -> CurrentPartOfDayDomain.MORNING
                in 43_200..75_600 -> CurrentPartOfDayDomain.AFTERNOON
                else -> CurrentPartOfDayDomain.EVENING
            }

            return response
        }
    }
}