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
    private val routes: List<String>,
    private val departureDate: String?,
    private val arrivalDate: String?,
    private val tripType: String?,
    private val flightClass: String? // Added flightClass parameter
) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val route = routes[position]
        holder.bind(route, departureDate, arrivalDate, tripType, flightClass, context) // Pass flightClass
    }

    override fun getItemCount(): Int = routes.size

    class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textRoute: TextView = itemView.findViewById(R.id.textRoute)
        private val textDepartureDate: TextView = itemView.findViewById(R.id.textDepartureDate)
        private val textArrivalDate: TextView = itemView.findViewById(R.id.textArrivalDate)
        private val textFlightClass: TextView = itemView.findViewById(R.id.textFlightClass) // Add TextView for flight class
        private val buttonSelect: Button = itemView.findViewById(R.id.buttonSelect)

        fun bind(route: String, departureDate: String?, arrivalDate: String?, tripType: String?, flightClass: String?, context: Context) {
            textRoute.text = route
            textDepartureDate.text = "Departure: ${departureDate ?: "N/A"}"
            if (arrivalDate != null) {
                textArrivalDate.text = "Arrival: ${arrivalDate ?: "N/A"}"
                textArrivalDate.visibility = View.VISIBLE
            } else {
                textArrivalDate.visibility = View.GONE
            }
            textFlightClass.text = "Class: ${flightClass ?: "N/A"}" // Bind flightClass

            buttonSelect.setOnClickListener {
                val dialog = FlightDetailsDialog(context, route, departureDate, arrivalDate, tripType, flightClass)
                dialog.show()
            }
        }
    }
}

