package xyz.savvamirzoyan.musicplayer.appcore

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class CoreFragment<VB : ViewBinding> : Fragment() {

    private var toolbarHeight: Float = 0f
    protected lateinit var binding: VB
    protected val coreActivity: CoreActivity
        get() = (requireActivity() as CoreActivity)

    @Suppress("SameParameterValue")
    private fun coroutine(lifecycle: Lifecycle.State, function: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(lifecycle, function) }
    }

    private fun launchWhenCreated(function: suspend CoroutineScope.() -> Unit) {
        coroutine(Lifecycle.State.CREATED, function)
    }

    private fun launchWhenStarted(function: suspend CoroutineScope.() -> Unit) {
        coroutine(Lifecycle.State.STARTED, function)
    }

    protected fun <T> collectStarted(flow: Flow<T>, function: suspend CoroutineScope.(T) -> Unit) {
        launchWhenStarted {
            flow.collect {
                function(it)
            }
        }
    }

    protected fun <T> collect(flow: Flow<T>, function: suspend CoroutineScope.(T) -> Unit) {
        launchWhenCreated {
            flow.collect {
                function(it)
            }
        }
    }

    fun setupDefaultFlows(viewModel: CoreViewModel) {
        collect(viewModel.loadingFlow) { isLoading ->
            when (isLoading) {
                true -> coreActivity.startLoading()
                false -> coreActivity.stopLoading()
            }
        }

        collect(viewModel.navigationDeeplinkFlow) { uri ->
            val navOptions = NavOptions.Builder()
                .setEnterAnim(com.google.android.material.R.anim.abc_fade_in)
                .setExitAnim(com.google.android.material.R.anim.abc_fade_out)
                .setPopExitAnim(com.google.android.material.R.anim.abc_fade_out)
                .setPopEnterAnim(com.google.android.material.R.anim.abc_fade_in)
                .build()
            val request = NavDeepLinkRequest.Builder
                .fromUri(uri)
                .build()
            findNavController().navigate(request = request, navOptions = navOptions)
        }
    }

    fun setupFragment(appbar: AppBarLayout, toolbar: CollapsingToolbarLayout) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            toolbarHeight = insets.top.toFloat()

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val alpha = 1 + (verticalOffset / toolbarHeight)
            toolbar.alpha = alpha
        })
    }

    fun setupFragmentWithTopInsects() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setupFragmentWithTopBottomInsects() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                bottomMargin = insets.bottom
            }

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setupFragmentWithBottomInsects() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setupFragment(toolbar: MaterialToolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            WindowInsetsCompat.CONSUMED
        }
    }
}