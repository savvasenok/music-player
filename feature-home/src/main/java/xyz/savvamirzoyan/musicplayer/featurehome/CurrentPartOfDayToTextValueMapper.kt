package xyz.savvamirzoyan.musicplayer.featurehome

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.usecasedatetime.CurrentPartOfDayDomain
import javax.inject.Inject

interface CurrentPartOfDayToTextValueMapper : Mapper {

    fun map(model: CurrentPartOfDayDomain): TextValue

    class Base @Inject constructor() : CurrentPartOfDayToTextValueMapper {

        override fun map(model: CurrentPartOfDayDomain) = when (model) {
            CurrentPartOfDayDomain.MORNING -> TextValue.AsResource(R.string.greetings_morning)
            CurrentPartOfDayDomain.AFTERNOON -> TextValue.AsResource(R.string.greetings_afternoon)
            CurrentPartOfDayDomain.EVENING -> TextValue.AsResource(R.string.greetings_evening)
        }
    }
}
