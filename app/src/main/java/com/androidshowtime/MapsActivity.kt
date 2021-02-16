package com.androidshowtime

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import timber.log.Timber
import java.util.*

class MapsActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var map: GoogleMap


    //suppress missing permission
    @SuppressLint("MissingPermission")
    private val locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) {

        isGranted ->

        if (isGranted) {

            //add my Location Button on top-right side corner
            map.isMyLocationEnabled = true
        }
        else {

            // request for permission is not granted
            ActivityCompat.requestPermissions(this ,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION) ,
                    0)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        Timber.plant(Timber.DebugTree())
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

        //call launch() passing in the permission required
        locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    val lat = -1.298311703512306
    val lng = 36.76229638360123
    val homeLatLng = LatLng(lat , lng)

    val zoomLevel = 17f


    //define size of the overlay
    val androidSize = 100f

    //move camera with zoom
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng , zoomLevel))






    map.addMarker(MarkerOptions().position(homeLatLng))

    //add long press marker
    addMarkerOnLongPress(map)

    //set point of InstrumentationRegistry
    setPOIClick(map)


    //set style
    setMapStyle(map)


    //inside on map ready
    val androidOverlay =
            GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.android))

                    //Set the position property for the GroundOverlayOptions object
                    .position(homeLatLng , androidSize)

    map.addGroundOverlay(androidOverlay)

    locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)


}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {

    menuInflater.inflate(R.menu.menu , menu)

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
                String.format(Locale.getDefault() , "Lat: %1$.5f, Long: %2$.5f" , it.latitude , it.longitude)

        //use snippet() call on the
        map.addMarker(MarkerOptions().position(it)
                .title(getString(R.string.dropped_pin)) // set the title
                .snippet(snippet) //set info
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
    }


}
/*  */
private fun setPOIClick(map: GoogleMap) {


    map.setOnPoiClickListener {

        val poiMarker =
                map.addMarker(MarkerOptions().position(it.latLng)
                        .title(it.name))

        //call show info
        poiMarker.showInfoWindow()
    }

}


private fun setMapStyle(map: GoogleMap) {

    try {

        //customize map which returns a boolean value
        val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this , R.raw.map_style))

        if (!success) {

            Timber.i("File Parsing Failed")
        }

    }
    catch (e: Resources.NotFoundException) {

        Timber.i("File Parsing error $e")
    }

}


}