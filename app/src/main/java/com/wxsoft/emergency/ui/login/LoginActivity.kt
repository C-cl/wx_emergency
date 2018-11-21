package com.wxsoft.emergency.ui.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import com.wxsoft.emergency.R
import com.wxsoft.emergency.databinding.ActivityLoginBinding
import com.wxsoft.emergency.di.ViewModelFactory
import com.wxsoft.emergency.ui.BaseActivity
import com.wxsoft.emergency.ui.main.MainActivity
import com.wxsoft.emergency.utils.viewModelProvider
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(){

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding= DataBindingUtil.setContentView<ActivityLoginBinding>(this,
            R.layout.activity_login)

        viewModel=viewModelProvider(factory)
        binding.apply {
            setLifecycleOwner(this@LoginActivity)

        }

        binding.viewModel=viewModel
//        binding.password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
//            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                attemptLogin()
//                return@OnEditorActionListener true
//            }
//            false
//        })
        binding.emailSignInButton.setOnClickListener {

            binding.viewModel?.login()

        }

        viewModel.account.observe(this, Observer {
            if(it!=null){
                var intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.viewModel?.jpushAccountId=JPushInterface.getRegistrationID(this)

    }



    /**
     * Shows the progress UI and hides the login form.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private fun showProgress(show: Boolean) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
//
//            login_form.visibility = if (show) View.GONE else View.VISIBLE
//            login_form.animate()
//                .setDuration(shortAnimTime)
//                .alpha((if (show) 0 else 1).toFloat())
//                .setListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator) {
//                        login_form.visibility = if (show) View.GONE else View.VISIBLE
//                    }
//                })
//
//            login_progress.visibility = if (show) View.VISIBLE else View.GONE
//            login_progress.animate()
//                .setDuration(shortAnimTime)
//                .alpha((if (show) 1 else 0).toFloat())
//                .setListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator) {
//                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
//                    }
//                })
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            login_progress.visibility = if (show) View.VISIBLE else View.GONE
//            login_form.visibility = if (show) View.GONE else View.VISIBLE
//        }
//    }
}
