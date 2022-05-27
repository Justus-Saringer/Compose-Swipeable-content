your.package.name

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SwipeableActionCard(
    dismissState: DismissState,
    leftSwipeBackgroundComposable: (@Composable () -> Unit)? = null,
    rightSwipeBackgroundComposable: (@Composable () -> Unit)? = null,
    onLeftSwiped: (() -> Unit)? = null,
    onRightSwiped: (() -> Unit)? = null,
    fractionalThreshold: FractionalThreshold = FractionalThreshold(0.5f),
    dismissDirections: Set<DismissDirection>,
    content: @Composable () -> Unit
) {

    SwipeToDismiss(
        state = dismissState,
        directions = dismissDirections,
        dismissThresholds = { fractionalThreshold },
        background = {

            when {
                (leftSwipeBackgroundComposable != null && rightSwipeBackgroundComposable != null) &&
                        (dismissDirections.contains(DismissDirection.StartToEnd) && dismissDirections.contains(DismissDirection.EndToStart)) -> if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
                    leftSwipeBackgroundComposable()
                } else {
                    rightSwipeBackgroundComposable()
                }

                leftSwipeBackgroundComposable != null && dismissDirections.contains(DismissDirection.EndToStart) -> leftSwipeBackgroundComposable()

                rightSwipeBackgroundComposable != null && dismissDirections.contains(DismissDirection.StartToEnd) -> rightSwipeBackgroundComposable()
            }
        },
        dismissContent = { content() }
    )

    when (dismissState.currentValue) {
        DismissValue.DismissedToStart -> {
            onLeftSwiped?.invoke()
        }

        DismissValue.DismissedToEnd -> {
            onRightSwiped?.invoke()
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEFEFEF
)
@Composable
private fun SwipableActionCardPreview() {

    val state by remember { mutableStateOf(DismissState(DismissValue.Default) { true }) }

        SwipeableActionCard(
            dismissState = state,
            rightSwipeBackgroundComposable = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    backgroundColor = Color(0xFF0075D2)
                ) {
                    Text(text = "Good evening")
                }
            },
            leftSwipeBackgroundComposable = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    backgroundColor = Color(0xFFFFA157)
                ) {
                    Text(text = "Good morning")
                }
            },
            dismissDirections =  setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                backgroundColor = Color(0xFF545454)
            ) {
                Text(text = "Swipe me!")
            }
        }
    }