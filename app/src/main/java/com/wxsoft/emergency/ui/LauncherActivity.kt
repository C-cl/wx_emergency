package com.wxsoft.emergency.ui

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import com.wxsoft.emergency.result.EventObserver
import com.wxsoft.emergency.ui.home.HomeActivity
import com.wxsoft.emergency.ui.onboarding.OnboardingActivity
import com.wxsoft.emergency.utils.checkAllMatched
import com.wxsoft.emergency.utils.viewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class LauncherActivity:DaggerAppCompatActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val viewModel: LaunchViewModel = viewModelProvider(viewModelFactory)
        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.HOME_ACTIVITY -> startActivity(Intent(this, HomeActivity::class.java))
                LaunchDestination.ONBOARDING -> startActivity(Intent(this, OnboardingActivity::class.java))
            }.checkAllMatched
            finish()
        })
    }
}