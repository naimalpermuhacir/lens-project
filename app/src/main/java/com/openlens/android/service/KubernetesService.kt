package com.openlens.android.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import javax.inject.Inject

class KubernetesService : Service() {
    
    @Inject
    lateinit var kubernetesRepository: com.openlens.android.data.repository.KubernetesRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}