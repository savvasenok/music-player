package xyz.savvamirzoyan.musicplayer.appcore.mapper

import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import javax.inject.Inject

interface TimeToStringMapper : Mapper {

    fun map(seconds: Int): String

    class Base @Inject constructor() : TimeToStringMapper {

        override fun map(seconds: Int): String {
            val minutesLeft = seconds / 60
            val secondsLeft = seconds - minutesLeft * 60

            return String.format("%d:%02d", minutesLeft, secondsLeft)
        }
    }
}