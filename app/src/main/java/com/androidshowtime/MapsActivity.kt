package com.androidshowtime

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val lat = -1.298311703512306
        val lng = 36.76229638360123
        val homeLatLng = LatLng(lat, lng)

        val zoomLevel = 17f



        //move camera with zoom
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))







    map.addMarker(MarkerOptions().position(homeLatLng))

    //add long press marker
    addMarkerOnLongPress(map)

}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {

    menuInflater.inflate(R.menu.menu, menu)

    return super.onCreateOptionsMenu(menu)
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {

    //super.onOptionsItemSelected(item)
    return when (item.itemId) {

        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL

            true
        }
        R.id.hybrid -> {

            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }

        R.id.satellite_map -> {

            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }


        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }

        else               -> super.onOptionsItemSelected(item)
    }
}


private fun addMarkerOnLongPress(map: GoogleMap) {

    map.setOnMapLongClickListener {

        val snippet =
                String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        it.latitude, it.longitude)

        //use snippet() call on the
        map.addMarker(MarkerOptions().position(it)
                .title(getString(R.string.dropped_pin))// set the title
                .snippet(snippet)) //set info
    }
}


}