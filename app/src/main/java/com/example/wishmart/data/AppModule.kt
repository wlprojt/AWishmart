package com.example.wishmart.data

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.wishmart.auth.*
import com.example.wishmart.orders.OrderApi
import com.example.wishmart.orders.PaymentApi
import com.example.wishmart.product.ProductApi
import com.example.wishmart.product.SaleApi
import com.example.wishmart.product.SaleRepository
import com.example.wishmart.products.sale.BackendApi
import com.example.wishmart.products.sale.CartApi
import com.example.wishmart.products.sale.ProductsApi
import com.example.wishmart.search.SearchApi
import com.example.wishmart.search.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://192.168.29.136:3000/"

    @Provides
    @Singleton
    fun provideSharedPrefs(app: Application): SharedPreferences =
        app.getSharedPreferences("prefs", MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideOkHttpClient(prefs: SharedPreferences): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(prefs))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // ✅ ALL apis from same retrofit
    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideAuthRepository(api: AuthApi, prefs: SharedPreferences): AuthRepository =
        AuthRepositoryImpl(api, prefs)

    @Provides @Singleton
    fun provideBackendApi(retrofit: Retrofit): BackendApi =
        retrofit.create(BackendApi::class.java)

    @Provides @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi =
        retrofit.create(CartApi::class.java)

    @Provides @Singleton
    fun provideProductsApi(retrofit: Retrofit): ProductsApi =
        retrofit.create(ProductsApi::class.java)

    @Provides @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideSaleApi(retrofit: Retrofit): SaleApi =
        retrofit.create(SaleApi::class.java)

    @Provides
    @Singleton
    fun provideSaleRepository(api: SaleApi): SaleRepository =
        SaleRepository(api)

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi =
        retrofit.create(SearchApi::class.java)

    @Provides
    @Singleton
    fun provideSearchRepository(api: SearchApi): SearchRepository =
        SearchRepository(api)

    @Provides @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi =
        retrofit.create(OrderApi::class.java)

    @Provides @Singleton
    fun providePaymentApi(retrofit: Retrofit): PaymentApi =
        retrofit.create(PaymentApi::class.java)

}