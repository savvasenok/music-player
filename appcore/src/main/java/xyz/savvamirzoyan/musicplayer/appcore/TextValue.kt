package xyz.savvamirzoyan.musicplayer.appcore

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

class TextValue private constructor() {

    private var stringValue: String? = null

    @StringRes
    private var stringId: Int? = null

    @PluralsRes
    private var pluralId: Int? = null
    private var quantity = 0

    private var textValuesAfter: List<TextValue>? = null

    private var stringArgs: Array<out Any> = emptyArray()

    constructor(string: String) : this() {
        stringValue = string
    }

    constructor(@StringRes stringId: Int) : this() {
        this.stringId = stringId
    }

    constructor(@StringRes stringId: Int, args: Array<Any>) : this() {
        this.stringId = stringId
        this.stringArgs = args
    }

    constructor(@PluralsRes pluralId: Int, quantity: Int, vararg args: Any) : this() {
        this.pluralId = pluralId
        this.stringArgs = args
        this.quantity = quantity
    }

    constructor(vararg textValues: TextValue) : this() {
        textValuesAfter = textValues.toList()
    }

    fun getString(context: Context): String = stringValue
        ?: stringId?.let { context.getString(it, *stringArgs) }
        ?: pluralId?.let { context.resources.getQuantityString(it, quantity, *stringArgs) }
        ?: textValuesAfter?.joinToString("") { it.getString(context) }
        ?: ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextValue) return false

        if (stringValue != other.stringValue) return false
        if (stringId != other.stringId) return false
        if (pluralId != other.pluralId) return false
        if (quantity != other.quantity) return false
        if (textValuesAfter != other.textValuesAfter) return false
        if (!stringArgs.contentEquals(other.stringArgs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stringValue?.hashCode() ?: 0
        result = 31 * result + (stringId ?: 0)
        result = 31 * result + (pluralId ?: 0)
        result = 31 * result + quantity
        result = 31 * result + (textValuesAfter?.hashCode() ?: 0)
        result = 31 * result + stringArgs.contentHashCode()
        return result
    }
}