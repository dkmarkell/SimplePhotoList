package com.dkmarkell.rakutentakehome.photo.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dkmarkell.rakutentakehome.AppViewModel
import com.dkmarkell.rakutentakehome.R
import com.dkmarkell.rakutentakehome.databinding.FragmentPhotosListBinding
import com.dkmarkell.rakutentakehome.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotosListFragment : Fragment() {

    private var _binding: FragmentPhotosListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoListViewModel by viewModels()
    private val appViewModel: AppViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    private var animState = ANIM_STATE_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Register as a listener to get the title from the details screen
         */
        setFragmentResultListener(Constants.TITLE_REQUEST_KEY) { requestKey, bundle ->
            when (requestKey) {
                Constants.TITLE_REQUEST_KEY -> {
                    bundle.getString(Constants.TITLE_BUNDLE_KEY)?.let {
                        viewModel.setTitle(it)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosListBinding.inflate(layoutInflater, container, false)

        val listAdapter = PhotoPreviewAdapter { photo ->
            findNavController().navigate(
                PhotosListFragmentDirections.actionPhotosListFragmentToPhotoDetailsFragment(photo.id)
            )
        }
        binding.imageList.adapter = listAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appViewModel.loading.collect {
                    binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appViewModel.error.collect { stringResource ->
                    stringResource?.let {
                        appViewModel.onErrorProcessed()
                        Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.title.collect { title ->
                    val titleString = title ?: getString(R.string.title_default)
                    binding.title.text = titleString
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photoPreviews.collect { previews ->
                    listAdapter.submitList(previews)
                }
            }
        }

        binding.imageList.setOnScrollListener(object : AbsListView.OnScrollListener {
            /**
             * The first visible item position the last time onScroll() was called
             */
            private var previousFirstVisiblePosition: Int = 0

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                /**
                 * When scrolling down through the list, we hide the quick return
                 * view. When scrolling up, we want to show it.
                 *
                 * To avoid some back and forth stutter, we use a threshold of greater / less
                 * than 1. This means the we only consider hiding / showing when the current
                 * top most visible item is 2 or more elements away from the previous top most.
                 *
                 * When a show / hide occurs, we reset our previousFirstVisiblePosition to the
                 * position that triggered the show / hide
                 */


                if ((firstVisibleItem - previousFirstVisiblePosition) > 1) {
                    if (!isOrWillBeHidden()) hide()

                    previousFirstVisiblePosition = firstVisibleItem
                } else if ((firstVisibleItem - previousFirstVisiblePosition) < -1) {
                    if (!isOrWillBeShown()) show()

                    previousFirstVisiblePosition = firstVisibleItem
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Helper function that lets us know if the quick return view
     * is or is in the process of being hidden. When true, we know
     * not to try re-hiding it.
     */
    private fun isOrWillBeHidden(): Boolean {
        return if (binding.quickReturn.visibility == View.VISIBLE) {
            animState == ANIM_STATE_HIDING
        } else {
            animState != ANIM_STATE_SHOWING
        }
    }

    /**
     * Helper function that lets us know if the quick return view
     * is or in the process of being made visible. When true, we know
     * not to try re-showing it.
     */
    private fun isOrWillBeShown(): Boolean {
        return if (binding.quickReturn.visibility != View.VISIBLE) {
            animState == ANIM_STATE_SHOWING
        } else {
            animState != ANIM_STATE_HIDING
        }
    }

    /**
     * Hides the quick return view by animating it up by its height,
     * simulating it scrolling off the screen. Once the animation has
     * finished we hide the view by setting its visibility to GONE
     */
    private fun hide() {
        binding.quickReturn.animate().cancel()

        binding.quickReturn.animate()
            .translationY(-binding.quickReturn.height.toFloat())
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                private var isCanceled = false
                override fun onAnimationStart(animation: Animator) {
                    animState = ANIM_STATE_HIDING
                    isCanceled = false
                    binding.quickReturn.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {
                    isCanceled = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    animState = ANIM_STATE_NONE
                    if (!isCanceled) {
                        binding.quickReturn.visibility = View.GONE
                    }
                }
            })
    }

    /**
     * Shows the quick return view by first making the view visible
     * and then animating it down by its height, simulating it scrolling
     * into the screen.
     */
    private fun show() {
        binding.quickReturn.animate().cancel()

        binding.quickReturn.animate()
            .translationY(0f)
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator) {
                    animState = ANIM_STATE_SHOWING
                    binding.quickReturn.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animator: Animator) {
                    animState = ANIM_STATE_NONE
                }
            })
    }

    inner class PhotoPreviewAdapter(
        private val onClick: (PhotoPreview) -> Unit
    ) : BaseAdapter() {

        private var previewList: List<PhotoPreview> = emptyList()

        override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
            val view: View = convertView ?: layoutInflater.inflate(R.layout.photo_preview_item, container, false)

            val rootLayout = view.findViewById<View>(R.id.root_layout)
            val titleView = view.findViewById<TextView>(R.id.title)
            val imageView = view.findViewById<ImageView>(R.id.thumbnail)
            val item = previewList[position]

            rootLayout.setOnClickListener { onClick(item) }
            titleView.text = item.title
            Picasso.get().load(item.url).into(imageView);

            return view
        }

        override fun getCount() = previewList.size

        override fun getItem(position: Int): Any = previewList[position]

        override fun getItemId(position: Int) = previewList[position].id

        fun submitList(newList: List<PhotoPreview>) {
            previewList = newList
            this.notifyDataSetChanged()
        }
    }

    companion object {
        private const val ANIM_STATE_NONE = 0
        private const val ANIM_STATE_HIDING = 1
        private const val ANIM_STATE_SHOWING = 2
    }

}

