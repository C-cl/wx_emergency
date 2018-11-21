package com.wxsoft.emergency.ui.emr

import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.Menu
import android.view.MenuItem
import com.wxsoft.emergency.BR
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.Patient
import com.wxsoft.emergency.di.ViewModelFactory
import com.wxsoft.emergency.result.EventObserver
import com.wxsoft.emergency.ui.detail.fragment.profile.ProfileFragment
import com.wxsoft.emergency.ui.emr.fragment.vitalsigns.VitalSignsFragment
import com.wxsoft.emergency.ui.fragment.patients.EmrAdapter
import com.wxsoft.emergency.ui.BaseActivity
import com.wxsoft.emergency.utils.NfcUtils
import com.wxsoft.emergency.utils.clearDecorations
import com.wxsoft.emergency.utils.viewModelProvider
import com.wxsoft.emergency.widget.PatientDetailDecoration
import com.wxsoft.emergency.widget.TimeLineDecoration
import kotlinx.android.synthetic.main.activity_emr.*
import javax.inject.Inject


class EmrActivity : BaseActivity() {

    companion object {
        val PATIENT_ID="PATIENT_ID"
    }


    @Inject lateinit var factory: ViewModelFactory
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pi:PendingIntent
    lateinit var adapter: EmrAdapter
    private var patientId="1"
    private lateinit var viewModel:EmrViewModel

    private lateinit var mapBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var evalBottomSheetBehavior: BottomSheetBehavior<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        patientId=intent.getStringExtra(PATIENT_ID);
        viewModel=viewModelProvider(factory)

        var binding= DataBindingUtil.setContentView<ViewDataBinding>(this,
                R.layout.activity_emr)

        binding.apply {
            setLifecycleOwner(this@EmrActivity)
            mapBottomSheetBehavior = BottomSheetBehavior.from(maps.view)
            evalBottomSheetBehavior = BottomSheetBehavior.from(evaluations.view)

            binding.setVariable(BR.viewModel,viewModel)
            adapter=EmrAdapter(viewModel)

            list.adapter=adapter

        }

        viewModel.patient.observe(this@EmrActivity, Observer {t->
            adapter.patient=t?: Patient("")

        })
        viewModel.emrItems.observe(this@EmrActivity, Observer { t->

            adapter.related=t?: emptyList()

            list.clearDecorations()
            list.addItemDecoration(PatientDetailDecoration(this@EmrActivity, adapter.merged))
            list.addItemDecoration(TimeLineDecoration(this@EmrActivity, adapter.merged))

        })

        viewModel.navigateToOperationAction.observe(this, EventObserver{ t->

            when(t) {

                "0"->{
                    var dialog=ProfileFragment()
                    dialog.show(supportFragmentManager,ProfileFragment.DIALOG_PROFILE)
                }
                "1" -> {
                    mapBottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
                }
                "6" -> {
                    evalBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

                "7" -> {
                    val dialog = VitalSignsFragment()
                    dialog.show(
                            supportFragmentManager,
                            VitalSignsFragment.DIALOG_VITAL_SIGNS)
                }
            }
        })

        patientId="1"
        viewModel.patientId=patientId

        nfcAdapter= NfcAdapter.getDefaultAdapter(this)

        pi = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

    }

    override fun onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pi, null, null); //启动
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_emr, menu)
        return true
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(NfcAdapter.ACTION_TAG_DISCOVERED==intent!!.action){

            val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val cardId = NfcUtils.toHexString(tagFromIntent.id)

            viewModel.patient.value!!.wristband_Number=cardId
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_edit_patient -> {

                mapBottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}