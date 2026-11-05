package com.nutriflow.mobile.data.cache

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.nutriflow.mobile.data.model.NutritionistDashboardResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OfflineCacheManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "nutriflow_offline_cache",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()

    fun saveDashboardCache(data: NutritionistDashboardResponse) {
        val json = gson.toJson(data)
        val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        
        sharedPreferences.edit()
            .putString("cached_dashboard_json", json)
            .putString("cached_dashboard_timestamp", timestamp)
            .apply()
    }

    fun getDashboardCache(): NutritionistDashboardResponse? {
        val json = sharedPreferences.getString("cached_dashboard_json", null) ?: return null
        return try {
            gson.fromJson(json, NutritionistDashboardResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getLastCacheTimestamp(): String {
        return sharedPreferences.getString("cached_dashboard_timestamp", "Desconhecido") ?: "Desconhecido"
    }

    fun clearCache() {
        sharedPreferences.edit().clear().apply()
    }
}
