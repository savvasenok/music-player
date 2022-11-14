package xyz.savvamirzoyan.musicplayer.core.mapper

sealed interface Model {
    interface Ui : Model
    interface Domain : Model
    sealed interface Data : Model {
        interface Local : Data
        interface Cloud : Data
    }
}