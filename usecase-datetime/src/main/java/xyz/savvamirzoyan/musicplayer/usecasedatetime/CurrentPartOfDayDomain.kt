package xyz.savvamirzoyan.musicplayer.usecasedatetime

import xyz.savvamirzoyan.musicplayer.core.mapper.Model

enum class CurrentPartOfDayDomain : Model.Domain {
    MORNING, AFTERNOON, EVENING
}