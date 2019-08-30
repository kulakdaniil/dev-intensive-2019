package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ImageView

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private var avatarSize: Int = 0
    private var rect: Rect = Rect()
    private var pathR: Path = Path()
    private lateinit var paintText: Paint
    private lateinit var paintBorder: Paint
    private var borderWidth: Float = DEFAULT_BORDER_WIDTH
    private var borderColor: Int = DEFAULT_BORDER_COLOR
    private var initials: String? = null
    private val bgColors = arrayOf(
        "#7BC862",
        "#E17076",
        "#FAA774",
        "#6EC9CB",
        "#65AADD",
        "#A695E7",
        "#EE7AAE"
    )

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2f
        private const val DEFAULT_BORDER_COLOR = 0
    }

    fun setInitials(initials: String) {
        // Here magical things happen
        // ¯\_(ツ)_/¯
    }
}






