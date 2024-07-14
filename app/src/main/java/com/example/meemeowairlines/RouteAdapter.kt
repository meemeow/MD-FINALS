package com.example.meemeowairlines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RouteAdapter(
    private val context: Context,
    private val routes: List<Pair<String, Int>>,  // Pair of route string and price in PHP thousand
    private val departureDate: String?,
    private val arrivalDate: String?,
    private val tripType: String?,
    private val flightClass: String?,
    private val passengerCount: Int // Added passengerCount parameter
) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val (route, price) = routes[position]
        holder.bind(route, price, departureDate, arrivalDate, tripType, flightClass, passengerCount)
    }

    override fun getItemCount(): Int = routes.size

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textRoute: TextView = itemView.findViewById(R.id.textRoute)
        private val textPrice: TextView = itemView.findViewById(R.id.textPrice) // TextView for price
        private val textDepartureDate: TextView = itemView.findViewById(R.id.textDepartureDate)
        private val textArrivalDate: TextView = itemView.findViewById(R.id.textArrivalDate)
        private val textFlightClass: TextView = itemView.findViewById(R.id.textFlightClass)
        private val buttonSelect: Button = itemView.findViewById(R.id.buttonSelect)

        fun bind(route: String, price: Int, departureDate: String?, arrivalDate: String?, tripType: String?, flightClass: String?, passengerCount: Int) {
            textRoute.text = route
            textPrice.text = "PHP $price"
            textDepartureDate.text = "Departure: ${departureDate ?: "N/A"}"
            if (arrivalDate != null) {
                textArrivalDate.text = "Arrival: ${arrivalDate ?: "N/A"}"
                textArrivalDate.visibility = View.VISIBLE
            } else {
                textArrivalDate.visibility = View.GONE
            }
            textFlightClass.text = "Class: ${flightClass ?: "N/A"}"

            buttonSelect.setOnClickListener {
                val dialog = FlightDetailsDialog(context, route, departureDate, arrivalDate, tripType, flightClass, price.toDouble(), passengerCount)
                dialog.show()
            }
        }
    }
}
