/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wxsoft.emergency.ui


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.wxsoft.emergency.data.prefs.SharedPreferenceStorage
import com.wxsoft.emergency.result.Event
import com.wxsoft.emergency.result.Resource
import com.wxsoft.emergency.utils.map
import io.reactivex.*
import javax.inject.Inject

/**
 * Logic for determining which screen to send users to on app launch.
 */
class LaunchViewModel @Inject constructor(private val sharedPreferenceStorage: SharedPreferenceStorage)
    : ViewModel() {

    private val onboardingCompletedResult = MutableLiveData<Resource<Boolean>>()
    val launchDestination: LiveData<Event<LaunchDestination>>

    init {


        launchDestination = onboardingCompletedResult.map {
            // If this check fails, prefer to launch main activity than show onboarding too often
            if ((it as? Resource.Success)?.data == false) {
                Event(LaunchDestination.ONBOARDING)
            } else {
                Event(LaunchDestination.HOME_ACTIVITY)
            }
        }

        Single.just(sharedPreferenceStorage.onboardingCompleted)
                .subscribe({ result -> onboardingCompletedResult.value=Resource.Success(result) }
                        , { throwable -> onboardingCompletedResult.value= Resource.Error(throwable) })

    }

}

enum class LaunchDestination {
    ONBOARDING,
    HOME_ACTIVITY
}
