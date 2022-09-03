package com.example.lunchwallet.common.authentication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lunchwallet.util.USER_DATASTORE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class UserDatastore @Inject constructor(@ApplicationContext val context: Context): UserStore {

    private val datastore = context.datastore

    companion object{
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(USER_DATASTORE)
        val USER_ID = stringPreferencesKey("USER ID")
        val USER_EMAIL= stringPreferencesKey("USER_EMAIL")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val BRUNCH_CODE = stringPreferencesKey("BRUNCH_CODE")
        val DINNER_CODE = stringPreferencesKey("DINNER_CODE")
        val SCAN_RESULT = stringPreferencesKey("SCAN_RESULT")
    }

    val userId: Flow<String?>
        get() = datastore.data.map {
            it[USER_ID]
        }

    val userEmail: Flow<String?>
        get() = datastore.data.map {
            it[USER_EMAIL]
        }

    val authToken: Flow<String?>
    get() = datastore.data.map {
        it[ACCESS_TOKEN]
    }

    val refreshToken: Flow<String?>
        get() = datastore.data.map {
            it[REFRESH_TOKEN]
        }
    override suspend fun saveToDataStore(userAuth: UserAuth) {
        datastore.edit {
            it[USER_ID] = userAuth.userId
            it[USER_EMAIL] = userAuth.email
            it[ACCESS_TOKEN] = userAuth.accessToken
            it[REFRESH_TOKEN] = userAuth.refreshToken
        }
    }

    suspend fun saveCode(code: Code) {
        datastore.edit {
            it[BRUNCH_CODE] = code.brunchCode ?:""
            it[DINNER_CODE] = code.dinnerCode ?:""
        }
    }

    suspend fun saveScanResult(result: String) {
        datastore.edit {
            it[SCAN_RESULT] = result ?:""
        }
    }

    val scanResult: Flow<String?>
        get() = datastore.data.map {
            it[SCAN_RESULT]
        }

    val brunchCode: Flow<String?>
        get() = datastore.data.map {
            it[BRUNCH_CODE]
        }

    val dinnerCode: Flow<String?>
        get() = datastore.data.map {
            it[DINNER_CODE]
        }


    override suspend fun getFromDataStore() = context.datastore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
             }
        }.map {
            UserAuth(
            userId = it[USER_ID] ?:"",
            email= it[USER_EMAIL] ?:"",
            accessToken = it[ACCESS_TOKEN] ?:"",
            refreshToken = it[REFRESH_TOKEN] ?:""
        )
    }
//    override suspend fun saveAccessToken(token: String) {
//        context.datastore.edit { preferences ->
//            preferences[ACCESS_TOKEN] = token
//        }
//    }
//    override suspend fun saveRefreshToken(token: String) {
//        context.datastore.edit { preferences ->
//            preferences[REFRESH_TOKEN] = token
//        }
//    }

    override suspend fun getAccessToken(): Flow<String?> {
       return datastore.data.map {
           it[ACCESS_TOKEN] ?:""
       }
    }

    override suspend fun getRefreshToken(): Flow<String?> {
        return datastore.data.map {
            it[REFRESH_TOKEN] ?:""
        }
    }

    override suspend fun getUserEmail(): Flow<String> {
        return datastore.data.map {
            it[USER_EMAIL] ?:""
        }
    }

    override suspend fun getUserId(): Flow<String> {
        return datastore.data.map {
            it[USER_EMAIL] ?:""
        }
    }

    override suspend fun clearFromDataStore() =
        datastore. edit{ userPreferences ->
           userPreferences.clear()
    }

    override suspend fun clearToken(key: Preferences.Key<String>) {
        datastore.edit{
            it.remove(key)
        }
    }
}