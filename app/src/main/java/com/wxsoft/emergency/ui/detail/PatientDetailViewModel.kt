package com.wxsoft.emergency.ui.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.os.Message
import com.wxsoft.emergency.data.entity.*
import com.wxsoft.emergency.data.entity.chest.VitalSign
import com.wxsoft.emergency.data.remote.*
import com.wxsoft.emergency.exception.ServerException
import com.wxsoft.emergency.result.Event
import com.wxsoft.emergency.result.Resource
import com.wxsoft.emergency.result.succeeded
import com.wxsoft.emergency.ui.emr.EventActions
import com.wxsoft.emergency.utils.DateTimeUtils
import com.wxsoft.emergency.utils.getAfromB
import com.wxsoft.emergency.utils.map
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class PatientDetailViewModel @Inject constructor(private val patientApi: PatientApi,
                                                 private val  dictEnumApi: DictEnumApi,
                                                 private val evaluationApi: EvaluationApi,
                                                 private val vitalSignApi: VitalSignApi,
                                                 private val operationApi: OperationMenuApi,
                                                 private val emrLogApi: EmrLogApi,
                                                 private val gpsApi: GpsApi): ViewModel(), EventActions {


    private var _attackTime:Long=0


    private val _navigateToOperationAction = MutableLiveData<Event<String>>()

    /**
     * ClickActions
     */
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction


    override fun onOpen(id: String) {

        _navigateToOperationAction.value= Event(id)
    }

    /**
     * 病人id
     */
    var patientId:String=""
        set(value)  {
            field=value
            if(value=="")return
            onSwipeRefresh()
        }

    /**
     * 是否正在夹在数据
     */
    val isLoading: LiveData<Boolean>

    private val handle:Handler
    private var runnable:Runnable

    /**
     * 病人信息
     */
    val patient:LiveData<Patient>

    /**
     * 病人病情评估
     */
    val dictionary: LiveData<List<Dictionary>>

    /**
     * 急救流程信息
     */
    val emrItems:LiveData<List<EmrItem>>
    val menuItems:LiveData<List<OperationMenu>>

    /**
     *获取病人信息
     */
    private val loadPatientResult= MediatorLiveData<Resource<Patient>>()
    private val bindRfidResult= MediatorLiveData<Resource<String>>()

    /**
     *获取病情评估字典
     */
    private val loadEvaluationDictEnumResult=MediatorLiveData<Resource<List<Dictionary>>>()

    private val loadMenuResult=MediatorLiveData<Resource<List<OperationMenu>>>()

    init {



        onSwipeRefresh()

        isLoading=loadPatientResult.map { it==Resource.Loading }

        patient=loadPatientResult.map { it ->

            (it as? Resource.Success)?.data?:Patient("")
        }

        dictionary=loadEvaluationDictEnumResult.map {
            var list=(it as? Resource.Success)?.data ?: emptyList()
            if(patient.value!=null) {
                for (dic in list) {

                    dic.checked = patient.value!!.evaluations.any { it.code == dic.itemCode }
                }
            }
            return@map list
        }

        handle=object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)

                if(patient.value==null||patient.value!!.attack_Time.isEmpty()) return
                patient.value?.attackClock="距离呼救："+ getAfromB(_attackTime,System.currentTimeMillis())
                //patient.value?.attackProgress= (((System.currentTimeMillis()-_attackTime)/1000)%3600).toInt()
            }
        }

        runnable= object :Runnable {
            override fun run() {
                //需要判断一下病人状态
                handle.sendEmptyMessage(1)
                handle.postDelayed(this,1000)
            }
        }


        menuItems=loadMenuResult.map { (it as?  Resource.Success)?.data?: emptyList() }
        emrItems=MediatorLiveData()
        emrItems.value=getCommands()
        bindRfidResult.map {

            var rf = (it as? Resource.Success)?.data ?: ""
            patient.value?.wristband_Number =rf
            return@map rf
        }

    }


    fun onSwipeRefresh() {

        if(patientId==""){
            var newPatient=Patient("")
            newPatient.createrId="1"
            newPatient.createrName="allen"
            newPatient.create_Date="2018-10-25 11:12:11"
            loadPatientResult.value=Resource.Success(newPatient)
        }else {
            loadPatientDetail()
        }
    }


    /**
     * 获取病人病例
     */
    private fun loadPatientDetail(){

        val flyable = Flowable.defer {
            dictEnumApi.loadDictEvaluations()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { result -> loadEvaluationDictEnumResult.value=Resource.Success(result) }
                    .flatMap {
                        patientApi.patient(patientId)

                            .subscribeOn(Schedulers.computation())}
        }

        flyable.map<Resource<Patient>> { Resource.Success(it) }
                .onErrorReturn { Resource.Error(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.Loading)
                .subscribe { result ->
                    loadPatientResult.value=result

                    if(!result.succeeded)return@subscribe
                    var p=result  as Resource.Success
                    if(!p.data.attack_Time.isEmpty()) {
                        _attackTime=DateTimeUtils.formatter.parse(p.data.attack_Time.replace("T"," ")).time
                        handle.postDelayed(runnable,1000)
                    }
                }
    }


    /**
     * 保存病人基本系信息
     */
    fun savePatientInfo(){

        if(patient.value!!.createrId==""){

        }
            //TODO 添加本人信息
        if(patient.value!!.name!="") {
            patientApi.save(patient.value!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { loadPatientResult.value = Resource.Loading }
                    .subscribe({ result ->patientId=result},
                            { throwable -> loadPatientResult.value = Resource.Error(throwable) }
                    )
        }

    }

    fun getMenus(){

        if(!patient.value!!.id.isEmpty()){
            operationApi.getMenuByUserAndPatient("2",patient.value!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadMenuResult.value = Resource.Loading }
                .subscribe(
                    { result ->loadMenuResult.value=Resource.Success(result)},
                    { throwable -> loadPatientResult.value = Resource.Error(throwable) }
                )
        }
    }

    fun insertVitalSign(){
        var  v=VitalSign("","","")
        v.patientId=patientId

        vitalSignApi.insert(v)
    }

    fun bindRfid(id:String){
        patientApi.bindRfid(patientId,id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Resource<String>> {
                if (it.success) {
                    if (it.result != null) {
                        Resource.Success(it.result!!)
                    } else {
                        Resource.Error(ServerException(it.msg))
                    }
                } else
                    Resource.Error(ServerException(it.msg))
            }
            .onErrorReturn { Resource.Error(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(Resource.Loading)
            .subscribe { result ->
                if (result is Resource.Success) {

                    patient.value?.wristband_Number = result.data
                }
            }
    }

    fun uploadGpsLocation(location: GpsLocation){
        gpsApi.save(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Resource<String>> {
                Resource.Success(it)
            }
            .onErrorReturn { Resource.Error(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(Resource.Loading)
            .subscribe { result ->
//                if (result is Resource.Success) {
//
//                    patient.value?.wristband_Number = result.data
//                }
            }
    }


    fun getCommands():List<EmrItem>{
        var list=ArrayList<EmrItem>()

        var command0=EmrItem("0")
        command0.userCase="120救援"
        command0.name="绑定腕带"
        command0.create_Time="08:07:23"
        list.add(command0)

        var command=EmrItem("1")
        command.userCase="120救援"
        command.name="发车"
        command.create_Time="08:17:23"
        list.add(command)
//        list.add(command.copy("11"))

        var command1=EmrItem("2")
        command1.userCase="120救援"
        command1.name="到达"
        command1.create_Time="08:27:23"
        list.add(command1)
//        list.add(command1.copy("12"))


        var command2=EmrItem("3")
        command2.userCase="120救援"
        command2.name="返回"
        command2.create_Time="08:32:23"
        list.add(command2)
//        list.add(command2.copy("13"))


        var command3=EmrItem("4")
        command3.userCase="120救援"
        command3.name="到达医院大门"
        command3.create_Time="08:34:23"
        list.add(command3)
//        list.add(command3.copy("14"))

        var command4=EmrItem("5")
        command4.userCase="首次接触"
        command4.name="心电图"
        list.add(command4)
//        list.add(command4.copy("15"))

        var command5=EmrItem("6")
        command5.userCase="首次接触"
        command5.name="病情评估"
        list.add(command5)
//        list.add(command5.copy("16"))

        var command6=EmrItem("7")
        command6.userCase="首次接触"
        command6.name="生命体征"
        list.add(command6)
//        list.add(command6.copy("17"))
        var command7=EmrItem("8")
        command7.userCase="首次接触"
        command7.name="转往CUU"
        list.add(command7)
//        list.add(command7.copy("18"))

        var command8=EmrItem("9")
        command8.userCase="首次接触"
        command8.name="启动导管室"
        list.add(command8)

        var command9=EmrItem("10")
        command9.userCase="首次接触"
        command9.name="ACG给药"
        list.add(command9)

        var command11=EmrItem("11")
        command11.userCase="首次接触"
        command11.name="GRACE评分"
        list.add(command11)

        return list
    }
}