package android.project.auction.domain.use_case.sign_up

import android.project.auction.common.AuthResult
import android.project.auction.domain.repository.AuthRepository
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class SignUp @Inject constructor(
    private val repository: AuthRepository
) {


    suspend operator fun invoke(email: String, fullName: String, password: String): AuthResult<Unit> {
        return try {

            repository.signUp(
                    email = email,
                    fullName = fullName,
                    password = password
            )

            repository.signIn(
                email = email,
                password = password
            )

            AuthResult.Authorized()
        } catch (e: HttpException) {
            if(e.code() == 401){

                AuthResult.UnAuthorized()

            } else {
                Log.d("ERROR", e.toString())

                AuthResult.UnknownError()

            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())

            AuthResult.UnknownError()

        }
    }
}