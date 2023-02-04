package xyz.savvamirzoyan.musicplayer.appcore.uistate

data class ButtonState(
    val text: TextValue,
    val isEnabled: Boolean = true,
    val isVisible: Boolean = true
)