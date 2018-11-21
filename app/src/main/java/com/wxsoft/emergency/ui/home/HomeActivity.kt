package com.wxsoft.emergency.ui.home

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.Patient
import com.wxsoft.emergency.databinding.ActivityHomeBinding
import com.wxsoft.emergency.ui.BaseActivity
import com.wxsoft.emergency.ui.emr.EmrActivity
import com.wxsoft.emergency.ui.fragment.patients.HomeAdapter
import com.wxsoft.emergency.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel:HomeViewModel

    lateinit var adapter: HomeAdapter

    lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(viewModelFactory)

        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
                .apply {
                    setLifecycleOwner(this@HomeActivity)
                }
        binding.viewModel=viewModel

        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_new_patient -> {
                toDetail("")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun toDetail(id:String){
        var intent=Intent(this, EmrActivity::class.java)
        intent.putExtra(EmrActivity.PATIENT_ID,id)
        startActivity(intent)
    }


}


@BindingAdapter(value = ["patientItems"])
fun patientItems(recyclerView: RecyclerView, list: List<Patient>?) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = HomeAdapter()

    }

    (recyclerView.adapter as HomeAdapter).apply { submitList(list?: emptyList()) }
}