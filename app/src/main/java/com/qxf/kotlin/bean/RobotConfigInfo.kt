package com.qxf.kotlin.bean

import com.qxf.kotlin.dbflows.MyDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = MyDatabase::class, allFields = true)
class RobotConfigInfo : BaseModel() {
    @PrimaryKey(autoincrement = true)
    var id: Long = 0
    @Column
    var robotCode: String? = null
        get() = if (field == null) "" else field
    @Column
    var robotName: String? = null
        get() = if (field == null) "" else field
    @Column
    var mallId: String? = null
        get() = if (field == null) "" else field
    @Column
    var mallName: String? = null
        get() = if (field == null) "" else field
    @Column
    var buildingId: String? = null
        get() = if (field == null) "" else field
    @Column
    var floorId: String? = null
        get() = if (field == null) "" else field
    @Column
    var mapName: String? = null
        get() = if (field == null) "" else field

}