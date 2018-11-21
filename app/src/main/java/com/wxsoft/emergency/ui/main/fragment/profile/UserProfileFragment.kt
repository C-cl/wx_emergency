package com.wxsoft.emergency.ui.main.fragment.profile

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.databinding.FragmentUserProfileBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UserProfileFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    lateinit var binding: FragmentUserProfileBinding

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentUserProfileBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner (this@UserProfileFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
