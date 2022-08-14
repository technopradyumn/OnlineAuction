package android.project.auction.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.project.auction.common.Constants
import android.project.auction.data.local.AppDatabase
import android.project.auction.data.remote.AuctionAPI
import android.project.auction.data.remote.AuthAPI
import android.project.auction.data.repository.AuctionRepositoryImpl
import android.project.auction.data.repository.AuthRepositoryImpl
import android.project.auction.domain.repository.AuctionRepository
import android.project.auction.domain.repository.AuthRepository
import android.project.auction.domain.use_case.AuctionProjectUseCase
import android.project.auction.domain.use_case.authentication.AuctionAuthUseCase
import android.project.auction.domain.use_case.authentication.auth.Authentication
import android.project.auction.domain.use_case.authentication.logout.Logout
import android.project.auction.domain.use_case.authentication.sign_in.SignIn
import android.project.auction.domain.use_case.authentication.sign_up.SignUp
import android.project.auction.domain.use_case.createitem.CreateItem
import android.project.auction.domain.use_case.getbidhistory.GetBidHistory
import android.project.auction.domain.use_case.getcategories.GetCategories
import android.project.auction.domain.use_case.getcategories.GetSubCategories
import android.project.auction.domain.use_case.getitemdetail.GetItemDetail
import android.project.auction.domain.use_case.getitems.GetItems
import android.project.auction.domain.use_case.placebidamount.PlaceBidAmount
import android.project.auction.domain.use_case.validateform.*
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthAPI(): AuthAPI {

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5,TimeUnit.MINUTES)
            .build()



        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAuctionAPI(): AuctionAPI {

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()



        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuctionAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthAPI): AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAuctionRepository(api: AuctionAPI): AuctionRepository {
        return AuctionRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAuctionAPIUseCases(
        repository: AuctionRepository,
        db: AppDatabase,
        preferences: SharedPreferences
    ): AuctionProjectUseCase {
        return AuctionProjectUseCase(
            getCategories = GetCategories(repository = repository),
            getItems = GetItems(repository = repository, db),
            getItemDetail = GetItemDetail(repository = repository),
            getBidHistory = GetBidHistory(repository = repository),
            placeBidAmount = PlaceBidAmount(repository = repository, preferences = preferences),
            getSubCategories = GetSubCategories(repository = repository),
            createItem = CreateItem(repository = repository, preferences = preferences)
        )
    }

    @Provides
    @Singleton
    fun provideValidationUseCases(): ValidationUseCase {
        return ValidationUseCase(
            validateEmail = ValidateEmail(),
            validatePassword = ValidatePassword(),
            validateRepeatedPassword = ValidateRepeatedPassword(),
            validateTerms = ValidateTerms(),
        )
    }

    @Provides
    @Singleton
    fun provideAuctionAuthUseCases(
        repository: AuthRepository,
        preferences: SharedPreferences
    ): AuctionAuthUseCase {
        return AuctionAuthUseCase(
            signIn = SignIn(repository = repository, preferences = preferences),
            signUp = SignUp(repository = repository),
            authentication = Authentication(repository = repository, preferences = preferences),
            logout = Logout(repository = repository, preferences = preferences)
        )
    }

    @Singleton
    @Provides
    fun provideAuctionDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "auctiondb.db"
        ).build()
    }

}