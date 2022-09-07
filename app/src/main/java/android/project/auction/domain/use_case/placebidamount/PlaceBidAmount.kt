package android.project.auction.domain.use_case.placebidamount

import android.content.SharedPreferences
import android.project.auction.domain.repository.AuctionRepository
import android.util.Log
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlaceBidAmount @Inject constructor(
    private val repository: AuctionRepository,
    private val preferences: SharedPreferences
) {

    suspend operator fun invoke(amount: Int, itemId: String) {
        try {

            val userID = preferences.getString("USERID", null) ?: "NOT REGISTERED USER"

            Log.d("testBID", amount.toString())
            Log.d("testBID", itemId)


            repository.placeBidAmount(
                amount = amount,
                itemId = itemId,
                userId = userID
            )

        } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.d("ERROR", e.toString())
                } else {
                    Log.d("ERROR", e.toString())
                }
            } catch (e: IOException) {
                Log.d("ERROR", e.toString())
            }
        }
}