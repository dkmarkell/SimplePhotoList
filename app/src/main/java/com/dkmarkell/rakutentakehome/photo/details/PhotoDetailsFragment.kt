package com.dkmarkell.rakutentakehome.photo.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dkmarkell.rakutentakehome.R
import com.dkmarkell.rakutentakehome.databinding.FragmentPhotoDetailsBinding
import com.dkmarkell.rakutentakehome.util.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            /**
             * Set the title as this fragment's result
             */
            setFragmentResult(Constants.TITLE_REQUEST_KEY, bundleOf(Constants.TITLE_BUNDLE_KEY to viewModel.title.value))
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailsBinding.inflate(layoutInflater, container, false)

        binding.root.doOnLayout {
            /**
             * Wait for the screen to be laid out and then get
             * the ImageView's width
             */
            viewModel.onImageWidthMeasured(binding.photo.width)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.url.collect { pair ->
                    /**
                     * Only load the image when the URL is valid (not empty) and
                     * the width of the ImageView is known (greater than 0)
                     */
                    if (pair.first.isNotEmpty() && pair.second > 0) {
                        Picasso.get()
                            .load(pair.first)
                            .placeholder(R.drawable.ic_launcher_background)
                            .resize(pair.second, 0)
                            .into(binding.photo)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFamily.collect {
                    if (it > 0) {
                        binding.familyText.text = getString(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPublic.collect {
                    if (it > 0) {
                        binding.publicText.text = getString(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFriend.collect {
                    if (it > 0) {
                        binding.friendText.text = getString(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.title.collect {
                    binding.titleText.text = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photoId.collect {
                    binding.photoIdText.text = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.farm.collect {
                    binding.farmText.text = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.owner.collect {
                    binding.ownerText.text = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.server.collect {
                    binding.serverText.text = it
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}