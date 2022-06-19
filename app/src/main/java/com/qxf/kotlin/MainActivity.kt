package com.qxf.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qxf.kotlin.bean.RobotConfigInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData();
    }

     fun  getData() :MutableList<RobotConfigInfo>{
         var robotConfigInfoList: MutableList<RobotConfigInfo> = ArrayList();
         for(index in  1..10){
             var robotConfigInfo : RobotConfigInfo = RobotConfigInfo();
             robotConfigInfo.buildingId = "100010"
             robotConfigInfo.mallName = "虹桥"
             robotConfigInfo.id = 100;
         robotConfigInfoList.add(robotConfigInfo);
         }
        return robotConfigInfoList;
    }
}
