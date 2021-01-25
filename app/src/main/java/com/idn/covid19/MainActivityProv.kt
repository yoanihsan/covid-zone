package com.idn.covid19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.idn.covid19.network.*
import kotlinx.android.synthetic.main.activity_main_prov.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivityProv : AppCompatActivity() {

    private lateinit var countryAdapter: ProvinceAdapter
    private var ascending = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_prov)

        supportActionBar?.hide()

        getCountry()

        btn_sequence.setOnClickListener{
            sequenceListener(ascending) // kondisi awal
            ascending = !ascending // mengubah nilai true menjadi false
        }

        search_view.setOnQueryTextListener(object :  androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryAdapter.filter.filter(newText)
                return false
            }
        })

        swipe_refresh.setOnRefreshListener {
            getCountry()
            swipe_refresh.isRefreshing = false
        }


    }

    private fun sequenceListener(ascending: Boolean) {
        rv_country.apply{
            layoutManager = LinearLayoutManager(this@MainActivityProv)
            setHasFixedSize(true)
            if (ascending){
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                Toast.makeText(this@MainActivityProv, "Z-A", Toast.LENGTH_LONG).show()
            } else {
                (layoutManager as LinearLayoutManager).reverseLayout = false
                (layoutManager as LinearLayoutManager).stackFromEnd = false
                Toast.makeText(this@MainActivityProv, "A-Z", Toast.LENGTH_LONG).show()
            }
            adapter = countryAdapter
        }
    }


    private fun getCountry() {
        val okHttp = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder().baseUrl("https://api.kawalcorona.com/")
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(InfoIndoService::class.java)
        api.getAllProvIndonesia().enqueue(object : Callback<List<AllProvIndo>> {
            override fun onResponse(
                call: Call<List<AllProvIndo>>,
                response: Response<List<AllProvIndo>>
            ) {
                if (response.isSuccessful) {
                    // Menampung data JSON dari objek
                    val dataCovid = response.body()
                    if (dataCovid != null) {
                        println("=============== "+dataCovid.get(0).attributes.Kasus_Posi)
                        println("=============== "+dataCovid.get(0).attributes.Kasus_Semb)
                        println("=============== "+dataCovid.get(0).attributes.Kasus_Meni)
                    }

                    val formatter: NumberFormat = DecimalFormat("#,###")
//                    txt_confirmed_globe.text = formatter.format(dataCovid?.get(0).Kasus_Posi?.toDouble())
//                    txt_deaths_globe.text = formatter.format(dataCovid?.get(0).Kasus_Meni?.toDouble())
//                    txt_recovered_globe.text = formatter.format(dataCovid?.get(0).Kasus_Semb?.toDouble())

                    txt_confirmed_globe.text = formatter.format(0)
                    txt_deaths_globe.text = formatter.format(0)
                    txt_recovered_globe.text = formatter.format(0)


                    rv_country.apply {
                        layoutManager = LinearLayoutManager(this@MainActivityProv)
                        setHasFixedSize(true)

                        countryAdapter = ProvinceAdapter(dataCovid as ArrayList<AllProvIndo>) {
                            itemClicked(it)
                        }

                        adapter = countryAdapter
                        progress_bar.visibility = View.GONE
                    }
                } else {
                    progress_bar.visibility = View.GONE
                    handleError(this@MainActivityProv)
                }
            }

            override fun onFailure(call: Call<List<AllProvIndo>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun handleError(mainActivityProv: MainActivityProv) {
        TODO("Not yet implemented")
    }

    private fun itemClicked(country: AllProvIndo) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val intent = Intent(this, ChartProvinceActivity::class.java)
        intent.putExtra(ChartCountryActivity.EXTRA_COUNTRY, country.attributes.FID.toString())
        intent.putExtra(ChartCountryActivity.EXTRA_DATE, currentDate)
        intent.putExtra(ChartCountryActivity.EXTRA_COUNTRY_CODE, country.attributes.FID.toString())

        intent.putExtra(ChartCountryActivity.EXTRA_NEW_DEATH, country.attributes.Kasus_Meni.toString())
        intent.putExtra(ChartCountryActivity.EXTRA_NEW_CONFIRMED, country.attributes.Kasus_Posi.toString())
        intent.putExtra(ChartCountryActivity.EXTRA_NEW_RECOVERED, country.attributes.Kasus_Semb.toString())

        intent.putExtra(ChartCountryActivity.EXTRA_TOTAL_CONFIRMED,  String.format("%d",country.attributes.Kasus_Posi))
        intent.putExtra(ChartCountryActivity.EXTRA_TOTAL_DEATH, String.format("%d",country.attributes.Kasus_Meni))
        intent.putExtra(ChartCountryActivity.EXTRA_TOTAL_RECOVERED, String.format("%d",country.attributes.Kasus_Semb))

        startActivity(intent)



    }
}
