package com.wxsoft.emergency.data.entity


import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.wxsoft.emergency.BR
import com.wxsoft.emergency.data.entity.chest.Evaluation

/**
 * 病人信息
 */
//@Entity(tableName = "list")
data class Patient constructor(@Bindable var id:String=""): BaseObservable() {


    @Bindable var name: String  =""
    set(value) {
        field=value
        notifyPropertyChanged(BR.name)
    }

    /**
     * 年龄
     */
    @Bindable var age_Value:String="1"
        set(value) {
            field=value
            notifyPropertyChanged(BR.age_Value)
        }

    /**
     * 年龄单位
     */
    @Bindable var age_Unit:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.age_Unit)
        }

    /**
     * 性别 1男2女
     */
     var gender:Int=1


    @Bindable
    @Expose
    var man:Boolean=true
    set(value) {
        field=value
        gender=if(field)1 else 2
        notifyPropertyChanged(BR.man)
    }
    /**
     * 医院id
     */
    @Bindable var hospital_Id:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.hospital_Id)
        }

    /**
     * 身份证号
     */
    @Bindable var idcard:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.idcard)
        }

    /**
     * 联系电话
     */
    @Bindable var contact_Phone:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.contact_Phone)
        }

    /**
     * 门诊id
     */
    @Bindable var outpatient_Id:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.outpatient_Id)
        }
    /**
     * 住院id
     */
    @Bindable var inpatient_Id:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.inpatient_Id)
        }
    /**
     * 发病地址
     */
    @Bindable var attack_Address:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.attack_Address)
        }

    /**
     * 腕带
     */
    @Bindable var wristband_Number:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.wristband_Number)
        }
    /**
     * 发病时间
     */
    @Bindable var attack_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.attack_Time)
        }
    /**
     * 发病时间无法精确到分 1是0否
     */
    @Bindable var is_Null_Attack_Detail_Time:Boolean =true
        set(value) {
            field=value
            notifyPropertyChanged(BR.is_Null_Attack_Detail_Time)
        }
    /**
     * 创建账户
     */
    @Bindable var createrId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrId)
        }
    /**
     * 创建人姓名
     */
    @Bindable var createrName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrName)
        }
    /**
     * 创建时间
     */
    @Bindable var create_Date:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.create_Date)
        }    /**
     * 更新者姓名
     */
    @Bindable var update_Name:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.update_Name)
        }
    /**
     * 更新时间
     */
    @Bindable var update_Date:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.update_Date)
        }
    /**
     * 登记时间
     */
    @Bindable var register_Date:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.register_Date)
        }
    /**
     * 呼救时间
     */
    @Bindable var help_Date:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.help_Date)
        }

    /**
     * 呼救来源
     */

    @SerializedName("is_Help")
    @Bindable var helped:Boolean=false
        set(value) {

            field=value
            notifyPropertyChanged(BR.helped)
        }
    /**
     * 活动索引号
     */
    @Bindable var ehr_Id:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.ehr_Id)
        }
    /**
     * 来源
     */
    @Bindable var source:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.source)
        }
    /**
     *序列号
     */
    @Bindable var unique_Id:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.unique_Id)
        }

    @Bindable var evaluations=ArrayList<Evaluation>()
        set(value) {

            field=value
            notifyPropertyChanged(BR.evaluations)
        }

    /**
     * 1卒中2胸痛
     */
    @Bindable var symptom_Code:Int=2
        set(value) {

            field=value
            notifyPropertyChanged(BR.symptom_Code)
        }

    /**
     * 1卒中2胸痛
     */
    @Bindable var attackClock:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.attackClock)
        }

    /**
     * 1卒中2胸痛
     */
    @Bindable var attackProgress:Int=0
        set(value) {

            field=value
            notifyPropertyChanged(BR.attackProgress)
        }

}



// /**
//     * 持续性胸闷/胸痛
//     */
//    @Bindable var persistent:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.persistent)
//        }
//
//    /**
//     * 间断性胸闷/胸痛
//     */
//    @Bindable var intermittent:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.intermittent)
//        }
//    /**
//     * 胸痛症状已缓解
//     */
//    @Bindable var laxation:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.laxation)
//        }
//    /**
//     * 腹痛
//     */
//    @Bindable var bellyache:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.bellyache)
//        }
//    /**
//     * 呼吸困难
//     */
//    @Bindable var dyspnea:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.dyspnea)
//        }
//    /**
//     * 休克
//     */
//    @Bindable var shock:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.shock)
//        }
//    /**
//     * 心衰
//     */
//    @Bindable var heart_attack:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.heart_attack)
//        }
//
//    /**
//     * 恶性心律失常
//     */
//    @Bindable var malignant_arrhythmia:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.malignant_arrhythmia)
//        }
//    /**
//     * 心肺复苏
//     */
//    @Bindable var cpr:Int=0
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.cpr)
//        }
//    /**
//     * 合并出血
//     */
//    @Bindable var hemorrhage:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.hemorrhage)
//        }
//    /**
//     * 其它
//     */
//    @Bindable var other:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.other)
//        }