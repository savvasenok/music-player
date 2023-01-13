package xyz.savvamirzoyan.musicplayer.appcore

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class CoreFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB
    protected val coreActivity: CoreActivity
        get() = (requireActivity() as CoreActivity)

    private fun coroutine(lifecycle: Lifecycle.State, function: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(lifecycle, function) }
    }

    private fun launchWhenCreated(function: suspend CoroutineScope.() -> Unit) {
        coroutine(Lifecycle.State.CREATED, function)
    }

    protected fun <T> collect(flow: Flow<T>, function: suspend (T) -> Unit) {
        launchWhenCreated { flow.collect { function(it) } }
    }
}