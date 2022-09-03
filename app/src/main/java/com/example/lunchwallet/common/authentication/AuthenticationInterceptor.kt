package com.example.lunchwallet.common.authentication

import android.util.Log
import com.example.lunchwallet.util.AUTHORIZATION
import com.example.lunchwallet.util.TOKEN_TYPE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject


class AuthenticationInterceptor @Inject constructor(
    private val userStore: UserStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        var accessToken: String? = ""
        runBlocking {
            accessToken =  userStore.getAccessToken().first()
            Log.d("AccessToken", "intercept: $accessToken")
        }

        val response = chain.proceedWithToken(request, accessToken)

        if (response.code != HttpURLConnection.HTTP_UNAUTHORIZED) {
            return response
        }

        var newToken: String? = ""
        runBlocking {
                newToken = userStore.getRefreshToken().first()
            Log.d("AccessToken", "newToken is intercept: $newToken")
            }
        return if (newToken !== null) {
            response.close()
            chain.proceedWithToken(request, newToken)
        } else {
            response
        }
    }

    private fun Interceptor.Chain.proceedWithToken(req: Request, token: String?): Response {
        return req.newBuilder()
            .apply {
                if (token !== null) {
                    addHeader(AUTHORIZATION, "$TOKEN_TYPE $token")
                }
            }
            .build()
            .let(::proceed)
    }
}

//class AuthenticationInterceptor @Inject constructor(
//    private val userStore: UserStore,
//) : Interceptor {
//
//        override fun intercept(chain: Interceptor.Chain): Response  {
//            var token = ""
//                runBlocking {
//                userStore.getAccessToken().collect {
//                    token = it!!
//                }
//            }
//            return chain.proceed(
//                chain.request()
//                    .newBuilder()
//                    .addHeader(AUTHORIZATION, "$TOKEN_TYPE $token")
//                    .build()
//            )
//
//        }
//}
