package com.revature.googlemapcomposev1

import android.Manifest

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.google.maps.android.ktx.model.cameraPosition
import com.revature.googlemapcomposev1.ui.theme.GoogleMapComposeV1Theme

val omaha=LatLng(41.2565369,-96.0045412)
val tampa=LatLng(27.9947147,-82.5943685)
val chicago=LatLng(41.8339042,-88.0121574)

var latitude=0.0
var longitude=0.0

class MainActivity : ComponentActivity(), LocationListener {

    private lateinit var locationManager:LocationManager

    private val locationPermissionCode=2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isMapLoaded by remember{mutableStateOf(false)}
            var cameraPositionState= rememberCameraPositionState{

                position= CameraPosition.fromLatLngZoom(tampa,11f)
            }
            GoogleMapComposeV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                     Box(modifier = Modifier.fillMaxSize())
                     {

                         GoogleMapView(
                             modifier=Modifier.matchParentSize(),
                             cameraPositionState =cameraPositionState,
                             onMapLoaded={isMapLoaded=true},

                         )
                        Button(onClick = { getLocation() }) {
                            
                            Text(text = "Get My Location")
                        }

                         Button(onClick = {



                         }) {

                             Text(text = "Redraw")
                         }



                     }

                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {

        latitude=location.latitude
        longitude=location.longitude

        Log.i("LatLong","Latitude ${location.latitude}, Longitude ${location.longitude}")

        Toast.makeText(this,"Latitude ${location.latitude}, Longitude ${location.longitude}",Toast.LENGTH_LONG).show()

    }



    private fun getLocation()
    {

        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationPermissionCode)
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f, this)
    }
}


@Composable
fun GoogleMapView(
    modifier:Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded:()->Unit,


)
{

   val context= LocalContext.current
    var uiSettings by remember{mutableStateOf(MapUiSettings(compassEnabled = true))}
    var mapProperties by remember{ mutableStateOf(MapProperties(mapType= MapType.NORMAL))}
    val circleCenter by remember{mutableStateOf(tampa)}

    GoogleMap(

        modifier=modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded =onMapLoaded,
        onPOIClick = {
            Toast.makeText(context,"Map Clicked:${it.name}",Toast.LENGTH_LONG).show()
        }
    )
    {

       // val omaha= Marker(position=omaha)
         /// val tampa= Marker(position=tampa)
//        val chicago= Marker(position=chicago)


        val currentLocation= com.google.maps.android.compose.Marker(position =LatLng(latitude,
            longitude) )

        Log.d("Lat Values","$latitude------$longitude")

        val current=Marker(position=LatLng(latitude,
            longitude))

        val markerClick:(Marker)->Boolean={

            Log.d("marker clicked","${it.title} was clicked")

            cameraPositionState.position.let{ projection->
                Log.d("marker clicked","The Current position is $projection")
            }

            false
        }

        Circle(
            center = circleCenter,
            fillColor=MaterialTheme.colors.secondary,
            strokeColor = MaterialTheme.colors.secondaryVariant,
            radius = 1000.0

            )





    }



}

@Composable
private fun DebugView(

    cameraPositionState: CameraPositionState,
    marketState:MarkerDragState
)
{

}

