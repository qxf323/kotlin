package com.qxf.kotlin.dbflows

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.util.Preconditions
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.DatabaseConfig.OpenHelperCreator
import com.raizlabs.android.dbflow.config.DatabaseConfig.TransactionManagerCreator
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.config.TableConfig
import com.raizlabs.android.dbflow.runtime.ModelNotifier
import com.raizlabs.android.dbflow.runtime.TableNotifierRegister
import com.raizlabs.android.dbflow.sql.language.CursorResult
import com.raizlabs.android.dbflow.sql.language.SQLOperator
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.ModelAdapter
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction
import java.util.*

class Dbflows internal constructor(private val mBuilder: Builder) {
    fun dropDatabase() {
        FlowManager.getDatabase(MyDatabase.NAME).destroy()
    }

    /**
     * insert bean
     * bean extends BaseModel is not allow
     *
     * @param bean
     * @param <T>
     * @return
    </T> */
    fun <T : Any> insert(bean: T): Long {
        try {
            val adapter: ModelAdapter<T> = FlowManager.getModelAdapter(bean.javaClass)
            return adapter.insert(bean)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * insert beans
     *
     * @param beans beans
     * @param <T>   Entity Class
    </T> */
    @SuppressLint("RestrictedApi")
    fun <T : Any> insert(beans: List<T>) {
        try {
            Preconditions.checkNotNull(beans)
            if (beans.isEmpty()) {
                return
            }
            val adapter: ModelAdapter<T> = FlowManager.getModelAdapter(beans.get(0).javaClass)
            adapter.insertAll(beans)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * update bean
     * bean extends BaseModel is not allow
     *
     * @param bean bean
     * @param <T>  Entity Class
     * @return success
    </T> */
    fun <T : Any> update(bean: T): Boolean {
        try {
            val adapter: ModelAdapter<T> = FlowManager.getModelAdapter(bean.javaClass)
            return adapter.update(bean)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * update beans
     *
     * @param beans beans
     * @param <T>   Entity Class
    </T> */
    @SuppressLint("RestrictedApi")
    fun <T : Any> update(beans: List<T>) {
        try {
            Preconditions.checkNotNull(beans)
            if (beans.isEmpty()) {
                return
            }
            val adapter: ModelAdapter<T> = FlowManager.getModelAdapter(beans.get(0).javaClass)
            adapter.updateAll(beans)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T : Any> delete(bean: T): Boolean {
        try {
            val adapter: ModelAdapter<T> = FlowManager.getModelAdapter(bean.javaClass)
            return adapter.delete(bean)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    fun <T : Any> delete(models: List<T>) {
        try {
            Preconditions.checkNotNull(models)
            if (models.isEmpty()) {
                return
            }
            val adapter: ModelAdapter<T> = FlowManager.getModelAdapter(models.get(0).javaClass)
            adapter.deleteAll(models)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 代码直接使用SQLite即可
     */
    @Deprecated("")
    fun <T> query(bean: Class<T>?): List<T> {
        try {
            return SQLite.select()
                .from((bean)!!)
                .queryList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    @Deprecated("")
    fun <T> query(bean: Class<T>?, conditions: SQLOperator?): List<T> {
        try {
            return SQLite.select()
                .from((bean)!!)
                .where(conditions)
                .queryList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    /**
     * query databse data async
     * and handle the result in Flows callback
     *
     * @param bean
     * @param conditions
     * @param flows
     * @param <T>
    </T> */
    fun <T> query(
        bean: Class<T>?,
        conditions: SQLOperator?,
        flows: Flows<List<T>?>
    ) {
        try {
            SQLite.select()
                .from((bean)!!)
                .where(conditions)
                .async()
                .queryResultCallback({ transaction: QueryTransaction<T>?, result: CursorResult<T> ->
                    try {
                        val data: List<T> = result.toList()
                        flows.accept(data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        result.close()
                    }
                })
                .error(
                    { transaction: Transaction?, error: Throwable ->
                        // handle any errors
                        Log.e(
                            TAG,
                            "dbFlows Runtime Exception:" + error.getLocalizedMessage()
                        )
                    })
                .execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun init(context: Context?) {
        val config: FlowConfig = FlowConfig.Builder(context)
            .addDatabaseConfig(databaseConfig())
            .addDatabaseHolder(FlowDatabaseHolder::class.java)
            .openDatabasesOnInit(true)
            .build()
        FlowManager.init(config)
    }

    @NonNull
    private fun databaseConfig(): DatabaseConfig {
        val builder: DatabaseConfig.Builder = DatabaseConfig.Builder((mBuilder.databaseClass)!!)
        if (mBuilder.tableConfig != null) {
            builder.addTableConfig(mBuilder.tableConfig)
        }
        if (mBuilder.helperListener != null) {
            builder.helperListener(mBuilder.helperListener)
        }
        if (mBuilder.modelNotifier != null) {
            builder.modelNotifier(mBuilder.modelNotifier)
        }
        if (mBuilder.openHelperCreator != null) {
            builder.openHelper(mBuilder.openHelperCreator)
        }
        if (mBuilder.transactionManagerCreator != null) {
            builder.transactionManagerCreator(mBuilder.transactionManagerCreator)
        }
        return builder.build()
    }

    class Builder() {
        var databaseClass: Class<*>? = null
        var tableConfig: TableConfig<*>? = null
        var modelNotifier: ModelNotifier? = null
        var helperListener: DatabaseHelperListener? = null
        var openHelperCreator: OpenHelperCreator? = null
        var transactionManagerCreator: TransactionManagerCreator? = null
        fun databaseClass(databaseClass: Class<*>?): Builder {
            this.databaseClass = databaseClass
            return this
        }

        fun tableConfig(tableConfig: TableConfig<*>?): Builder {
            this.tableConfig = tableConfig
            return this
        }

        fun modelNotifier(modelNotifier: ModelNotifier?): Builder {
            this.modelNotifier = modelNotifier
            return this
        }

        fun helperListener(helperListener: DatabaseHelperListener?): Builder {
            this.helperListener = helperListener
            return this
        }

        fun openHelperCreator(openHelperCreator: OpenHelperCreator?): Builder {
            this.openHelperCreator = openHelperCreator
            return this
        }

        fun transactionManagerCreator(transactionManagerCreator: TransactionManagerCreator?): Builder {
            this.transactionManagerCreator = transactionManagerCreator
            return this
        }

        fun build(): Dbflows {
            return Dbflows(this)
        }
    }

    companion object {
        val TAG: String = "Dbflows"
        //    @Contract(pure = true)
        @get:NonNull
        val defaultHelperListener: DatabaseHelperListener
            get() = object : DatabaseHelperListener {
                override fun onOpen(@NonNull database: DatabaseWrapper) {
                    Log.d(TAG, "DatabaseWrapper onOpen")
                }

                override fun onCreate(@NonNull database: DatabaseWrapper) {
                    Log.d(TAG, "DatabaseWrapper onCreate")
                }

                override fun onUpgrade(
                    @NonNull database: DatabaseWrapper, oldVersion: Int,
                    newVersion: Int
                ) {
                    Log.d(
                        TAG,
                        "DatabaseWrapper onUpgrade:$oldVersion ==> $newVersion"
                    )
                }

                override fun onDowngrade(
                    @NonNull databaseWrapper: DatabaseWrapper, oldVersion: Int,
                    newVersion: Int
                ) {
                    Log.d(
                        TAG,
                        "DatabaseWrapper onDowngrade:$oldVersion ==> $newVersion"
                    )
                }
            }

        //    @Contract(pure = true)
        @get:NonNull
        val defaultModelNotifier: ModelNotifier
            get() {
                return object : ModelNotifier {
                    override fun <T> notifyModelChanged(@NonNull model: T, @NonNull adapter: ModelAdapter<T>, @NonNull action: BaseModel.Action) {
                        Log.d(
                            TAG,
                            "notifyModelChanged:" + model.toString()
                        )
                    }

                    override fun <T> notifyTableChanged(@NonNull table: Class<T>, @NonNull action: BaseModel.Action) {
                        Log.d(TAG, "notifyTableChanged")
                    }

                    override fun newRegister(): TableNotifierRegister? {
                        return null
                    }
                }
            }
    }

}
