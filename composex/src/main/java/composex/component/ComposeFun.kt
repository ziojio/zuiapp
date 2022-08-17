package composex.component

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Int.pxToDp(): Dp {
    return LocalDensity.current.run { toDp() }
}

@Composable
fun getRealDp(context: Context, value: Double): Double {
    val density = LocalDensity.current.density
    val screenWidth = context.resources.displayMetrics.widthPixels
    return (screenWidth / 750.0) * 2 * value / density
}
