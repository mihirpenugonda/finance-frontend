package com.developer.finance.featureExpenseManagement.services

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.util.Log
import com.developer.finance.featureExpenseManagement.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TestService @Inject constructor(
    private val transactionRepository: TransactionRepository
) : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        doJob()
        doService()
        return true
    }

    private fun doService() {
        var jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        var builder = JobInfo.Builder(1, ComponentName(this, TestService::class.java))
        jobScheduler.schedule(builder.build())
    }


    private fun doJob() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("RT Job: ", "Service is currently Running")
            transactionRepository.testFunctionForService()
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

}