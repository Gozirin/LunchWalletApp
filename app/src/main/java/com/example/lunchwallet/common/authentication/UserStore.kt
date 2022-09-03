package com.example.lunchwallet.common.authentication


import kotlinx.coroutines.flow.Flow



interface UserStore {
    suspend fun saveToDataStore(userAuth: UserAuth)
    suspend fun getFromDataStore(): Flow<UserAuth>
//    suspend fun saveAccessToken(token:String)
//    suspend fun saveRefreshToken(token:String)
    suspend fun getAccessToken(): Flow<String?>
    suspend fun getRefreshToken(): Flow<String?>
    suspend fun getUserEmail(): Flow<String>
    suspend fun getUserId(): Flow<String>
    suspend fun clearFromDataStore(): androidx.datastore.preferences.core.Preferences
    suspend fun clearToken(key: androidx.datastore.preferences.core.Preferences.Key<String>)
}