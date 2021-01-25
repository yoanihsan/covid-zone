package com.idn.covid19

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idn.covid19.network.AllProvIndo
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ProvinceAdapter(val country: ArrayList<AllProvIndo>, val clickListener: (AllProvIndo) -> Unit) :
    RecyclerView.Adapter<ProvinceAdapter.CountryViewHolder>(), Filterable{

    var countryFilterList = ArrayList<AllProvIndo>()
    init {
        countryFilterList = country
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_prov, parent, false))


    override fun getItemCount() = countryFilterList.size

    override fun onBindViewHolder(holder: ProvinceAdapter.CountryViewHolder, position: Int) {
       holder.bindItem(countryFilterList[position], clickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()){
                    country
                } else {
                    var resultList = ArrayList<AllProvIndo>()
                    for (row in country){
                        if (row.attributes.Provinsi.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))
                        ){
                            resultList.add(row)
                        }
                    }
                    resultList
                }

                val filterResult = FilterResults()
                filterResult.values = countryFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<AllProvIndo>
                notifyDataSetChanged()
            }
        }
    }

    class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val tvCountry = itemView.findViewById<TextView>(R.id.txt_country_name)
        val tvTotalCase = itemView.findViewById<TextView>(R.id.txt_total_case)
        val tvTotalRecovered = itemView.findViewById<TextView>(R.id.txt_total_recovered)
        val tvTotalDeaths = itemView.findViewById<TextView>(R.id.txt_total_deaths)
        val tvStatus = itemView.findViewById<TextView>(R.id.status)
        val imgFlag = itemView.findViewById<ImageView>(R.id.img_flag_country)

        fun bindItem(countries: AllProvIndo, clickListener: (AllProvIndo) -> Unit){
            tvCountry.text = countries.attributes.Provinsi
            val formatter: NumberFormat = DecimalFormat("#,###")
            tvTotalCase.text = formatter.format(countries.attributes.Kasus_Posi)
            tvTotalRecovered.text = formatter.format(countries.attributes.Kasus_Semb)
            tvTotalDeaths.text = formatter.format(countries.attributes.Kasus_Meni)
            tvStatus.text =  "Merah"
            tvStatus.setTextColor(Color.parseColor("#f54242"))
            itemView.setOnClickListener{ clickListener(countries) }

            Glide.with(itemView.context)
                .load("http://www.countryflags.io/AL/flat/64.png")
                .into(imgFlag)
            itemView.setOnClickListener { clickListener(countries) }
        }

    }
}