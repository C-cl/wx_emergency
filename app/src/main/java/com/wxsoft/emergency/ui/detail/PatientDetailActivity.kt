package com.wxsoft.emergency.ui.detail

import android.app.PendingIntent
import android.content.Intent
import android.databinding.DataBindingUtil
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wxsoft.emergency.R
import com.wxsoft.emergency.databinding.ActivityPatientDetailBinding
import com.wxsoft.emergency.di.ViewModelFactory
import com.wxsoft.emergency.ui.BaseActivity
import com.wxsoft.emergency.ui.detail.dialog.BindRfidDialog
import com.wxsoft.emergency.ui.detail.fragment.map.MapFragment
import com.wxsoft.emergency.ui.detail.fragment.timeline.TimeLineFragment
import com.wxsoft.emergency.utils.NfcUtils
import com.wxsoft.emergency.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_action_bar_tab.*
import javax.inject.Inject


class PatientDetailActivity : BaseActivity() {

    companion object {
        val PATIENT_ID = "PATIENT_ID"
        private val COUNT = 3 // Agenda
        private val TIME_LINE_POSITION=0
        private val PAPER_POSITION=1
        private val MAP_POSITION=2
    }


    @Inject lateinit var factory: ViewModelFactory

    private var nfcAdapter: NfcAdapter?=null
    private lateinit var pi:PendingIntent
    private var patientId=""
    private lateinit var dialog:BindRfidDialog
    private lateinit var viewModel:PatientDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        patientId=intent.getStringExtra(PATIENT_ID)?:""
        viewModel=viewModelProvider(factory)

        var binding:ActivityPatientDetailBinding= DataBindingUtil.setContentView(this,
                R.layout.activity_patient_detail)

        binding.apply {
            setLifecycleOwner(this@PatientDetailActivity)

        }

        tabs.setupWithViewPager(binding.viewpager)
        binding.viewpager.adapter=PageAdapter(supportFragmentManager)
        binding.viewModel=viewModel
        //patientId="1"
        viewModel.patientId=patientId

        nfcAdapter= NfcAdapter.getDefaultAdapter(this)

        pi = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

        dialog=BindRfidDialog()



    }

    override fun onResume() {
        super.onResume();
        nfcAdapter?.enableForegroundDispatch(this, pi, null, null); //启动
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(this); //启动
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(NfcAdapter.ACTION_TAG_DISCOVERED==intent!!.action){

            val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val cardId = NfcUtils. toHexString(tagFromIntent.id)

            dialog.rfid=cardId
            dialog.show(supportFragmentManager,BindRfidDialog.DIALOG_BIND_RFID)
//            viewModel.patient.value!!.card=cardId
        }
    }


    inner class PageAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        override fun getCount() = COUNT

        override fun getItem(position: Int): Fragment {
            return when (position) {
                TIME_LINE_POSITION -> TimeLineFragment()
                MAP_POSITION->MapFragment()
                else->TimeLineFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                TIME_LINE_POSITION -> "时间线"
                PAPER_POSITION -> "详细病情"
                MAP_POSITION -> "来院GPS"
                else -> ""
            }
        }
    }
}