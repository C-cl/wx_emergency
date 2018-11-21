package com.wxsoft.emergency.ui.detail.fragment.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationData
import com.wxsoft.emergency.databinding.FragmentEmrMapBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_emr_map.*
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.GpsLocation
import com.wxsoft.emergency.di.ViewModelFactory
import com.wxsoft.emergency.ui.detail.PatientDetailViewModel
import com.wxsoft.emergency.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.activity_patient_detail.*
import javax.inject.Inject


class MapFragment : DaggerFragment() {


    @Inject lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentEmrMapBinding

    private var baiduMap:BaiduMap?=null

    private lateinit var viewModel:PatientDetailViewModel
    @Inject
    lateinit var locationClient: LocationClient

    private lateinit var locationListener:BDAbstractLocationListener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentEmrMapBinding.inflate(inflater, container, false).
            apply {
                setLifecycleOwner (this@MapFragment)
            }


        return binding.root
    }

    override fun onDestroy() {
        if(mapView!=null) {

            locationClient.unRegisterLocationListener(locationListener)
            mapView.onDestroy()
        }


        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        if(baiduMap==null) {
            baiduMap = mapView.map
            baiduMap!!.setIndoorEnable(true)
            baiduMap!!.isMyLocationEnabled = true
            var marker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo)
            var config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, marker)
            baiduMap!!.setMyLocationConfiguration(config)
            locationListener=object :BDAbstractLocationListener(){

                override fun onReceiveLocation(location: BDLocation?) {

                    if(location!=null ) {
                        // 设置定位数据
                        val locData = MyLocationData.Builder()
                            .accuracy(location.radius)
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100f).latitude(location.latitude)
                            .longitude(location.longitude).build()

                        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
                        baiduMap!!.setMyLocationData(locData)

                        viewModel.uploadGpsLocation(GpsLocation(viewModel.patientId,"测试车辆",location.longitude,location.latitude))

                    }
                }
            }
            locationClient.registerLocationListener(locationListener)
            locationClient.start()
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

}

