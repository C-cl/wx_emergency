package com.wxsoft.emergency.utils

import java.text.SimpleDateFormat

class DateTimeUtils constructor() {


    companion object {
        var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}

fun getAfromB(start:Long,end:Long):String{
    var a2b=""
    var c=end-start

    a2b+=if(c>3600*1000)  String.format("%02d", c/3600000) else "00"
    c %= (3600 * 1000)
    a2b+=":"+if(c>60*1000) String.format("%02d", c/60000)   else  "00"
    c %= (60 * 1000)
    a2b+=":"+if(c>1000) String.format("%02d", c/1000) else "00"
    return a2b
}

