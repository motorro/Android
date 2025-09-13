import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.motorro.statemachine.statemachine.App
import com.motorro.statemachine.statemachine.MainViewModel
import com.motorro.statemachine.statemachine.R
import com.motorro.statemachine.statemachine.counter.CounterViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Screen1Destination

@Serializable
data object Screen2Destination

@Serializable
data object Screen3Destination

fun NavController.composeAppNavGraph(onFinish: () -> Unit): NavGraph = createGraph(Screen1Destination) {
    composable<Screen1Destination> {
        val viewModel: CounterViewModel = viewModel()
        val count by viewModel.count.collectAsStateWithLifecycle()
        CounterScreen(stringResource(R.string.title_counter_1), count)
    }

    composable<Screen2Destination> {
        val viewModel: MainViewModel = viewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        App(uiState, viewModel::process) {
            onFinish()
        }
    }

    composable<Screen3Destination> {
        val viewModel: CounterViewModel = viewModel()
        val count by viewModel.count.collectAsStateWithLifecycle()
        CounterScreen(stringResource(R.string.title_counter_2), count)
    }
}