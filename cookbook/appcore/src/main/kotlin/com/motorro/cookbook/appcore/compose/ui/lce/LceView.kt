package com.motorro.cookbook.appcore.compose.ui.lce

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.cookbook.appcore.compose.ui.error.ErrorView
import com.motorro.cookbook.appcore.compose.ui.loading.LoadingView
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.lce.LceState

/**
 * Common LCE screen
 */
@Composable
fun <DATA: Any, ERR: CoreException> LceView(
    state: LceState<DATA, ERR>,
    onErrorAction: () -> Unit,
    modifier: Modifier = Modifier,
    loading: (@Composable (data: DATA?, modifier: Modifier) -> Unit)? = null,
    content: @Composable (data: DATA, error: ERR?, modifier: Modifier) -> Unit
) {
    when(state) {
        is LceState.Loading -> {
            if (null != loading) {
                loading(state.data, modifier)
            } else {
                val data = state.data
                if (null == data) {
                    LoadingView(modifier = modifier)
                } else {
                    content(data, null, modifier)
                }
            }
        }

        is LceState.Error -> {
            val data = state.data
            when {
                null != data -> content(data, state.error, modifier)
                else -> ErrorView(state.error.message, onErrorAction, modifier)
            }
        }

        is LceState.Content -> content(state.data, null, modifier)
    }
}