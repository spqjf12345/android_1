package com.example.android1

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.googlemaplayer.*
import noman.googleplaces.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashSet


class MapActivity: AppCompatActivity(), OnMapReadyCallback, PlacesListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null

    private val TAG = "googlemap"
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val UPDATE_INTERVAL_MS = 1000
    private val FASTEST_UPDATE_INTERVAL_MS = 500
    private val PERMISSIONS_REQUEST_CODE = 100
    var needRequest = false

    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    var mCurrentLocatiion: Location? = null
    var currentPosition: LatLng? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var location: Location? = null

    private var mLayout: View? = null
    var previous_marker: ArrayList<Marker>? = null

    lateinit var MyLocation: LatLng


    var locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList:List<Location> = locationResult.locations
            if (locationList.size > 0) {
                location = locationList[locationList.size - 1]
                //location = locationList.get(0);
                currentPosition = LatLng(location!!.getLatitude(), location!!.getLongitude())

                Log.d("currentPosition", currentPosition.toString())

                val markerTitle: String = getCurrentAddress(currentPosition!!)
                val markerSnippet = "위도:" + location!!.getLatitude()
                    .toString() + " 경도:" + location!!.getLongitude().toString()
                Log.d(TAG, "onLocationResult : $markerSnippet")
                setCurrentLocation(location!!, markerTitle, markerSnippet)
                mCurrentLocatiion = location
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //previous_marker = List<Marker>()

        setContentView(R.layout.googlemaplayer)

        mLayout = layout_main
        locationRequest = LocationRequest()

            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS.toLong())

        /* Place API */
        fd_restaurant?.setOnClickListener {
            showPlaceInformation(MyLocation);
        }


        val builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest(LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS.toLong())
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS.toLong()))



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        val mapFragment: SupportMapFragment? = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        Log.d(TAG, "getMapAsync")


    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        Log.d(TAG, "onMapReady")

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
       // setDefaultLocation()

       //val SEOUL: LatLng = LatLng(37.56, 126.97)
        //val markerOptions = MarkerOptions()
        //markerOptions.position(SEOUL)
        //markerOptions.title("서울")
        //markerOptions.snippet("한국의 수도")
        //mMap!!.addMarker(markerOptions)

        //mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10F))


        initLocation()

        var hasFineLocationPermission: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        var hasCoarseLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
            ) {
                Snackbar.make(
                    mLayout!!.rootView, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("확인", View.OnClickListener {
                    ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE
                    );
                }).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                );
            }
        }

        mMap!!.getUiSettings().setMyLocationButtonEnabled(true)
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
        mMap!!.setOnMapClickListener {
            Log.d("onMapClick : ", "onMapClick")
        }




    }

    @SuppressLint("MissingPermission")
    fun initLocation(){
        if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
            if(location != null){
                val MyLocationMarker = MarkerOptions()
                MyLocation = LatLng(location.latitude, location.longitude)
                Log.d("MyLocation", MyLocation.toString())
                MyLocationMarker.position(MyLocation)
                MyLocationMarker.title("내 위치") // marker name
                MyLocationMarker.snippet("되면 좋겠다") // marker specification
                mMap?.addMarker(MyLocationMarker)
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 10F))

            }
        }

       /*for (item:RestaurantMarker in restaurantMarker){ // in item
            val restaurantmarker = MarkerOptions()
            restaurantmarker.position(LatLng(item.latitude, item.longitude))
            restaurantmarker.title(item.name)
            restaurantmarker.snippet(item.menu)
            mMap?.addMarker(restaurantmarker)
        }*/

       // val SEOUL: LatLng = LatLng(37.56, 126.97)
       // val markerOptions = MarkerOptions()
       // markerOptions.position(SEOUL)
       // markerOptions.title("서울")
       // markerOptions.snippet("한국의 수도")
       // mMap!!.addMarker(markerOptions)
    }


    private fun startLocationUpdates() {//2
        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else{
            val hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }
            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates")

            mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap!!.setMyLocationEnabled(true);
        }
    }

    override fun onStart(){//1
        super.onStart()
        Log.d(TAG, "onStart")
        if (checkPermission()) {
            mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap!!.setMyLocationEnabled(true);

        }
    }

    override fun onStop() {
        super.onStop()
        if(mFusedLocationClient != null){
            Log.d(TAG, "onStop : call stopLocationUpdates")
            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
        }
    }

   fun getCurrentAddress(latlng: LatLng): String {
       val geocoder = Geocoder(this, Locale.getDefault())
       var addresses: List<Address?>
       try { addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
       } catch (ioException: IOException){
           Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
           return "지오코더 서비스 사용불가"
       } catch (illegalArgumentException: IllegalArgumentException) {
           Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
           return "잘못된 GPS 좌표"
       }
       if (addresses == null || addresses.size == 0) {
           Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
           return "주소 미발견"
       } else{
           val address = addresses[0]!!
           return address.getAddressLine(0).toString()
       }

   }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var lmManager = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return lmManager
    }

    fun setCurrentLocation(location: Location, markerTitle: String, markerSnippet:String) {
        if (currentMarker != null) currentMarker!!.remove();
        val currentLatLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        markerOptions.title(markerTitle)
        markerOptions.snippet(markerSnippet)
        markerOptions.draggable(true)
        currentMarker = mMap!!.addMarker(markerOptions)
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mMap!!.moveCamera(cameraUpdate)
    }

    fun setDefaultLocation(){
        val DEFAULT_LOCATION = LatLng(37.56, 126.97)
        val markerTitle = "위치정보 가져올 수 없음"
        val markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요"
        if (currentMarker != null) currentMarker!!.remove()
        val markerOptions = MarkerOptions()
        markerOptions.position(DEFAULT_LOCATION)
        markerOptions.title(markerTitle)
        markerOptions.snippet(markerSnippet)
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        currentMarker = mMap!!.addMarker(markerOptions)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15F)
        mMap!!.moveCamera(cameraUpdate)

    }
    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private fun checkPermission(): Boolean {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else false
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */

    fun onRequestPermissionsResult(//override and String[], Int[]
            permsRequestCode:Int, @NonNull permissions:Array<String>, @NonNull grandResults:Array<Int> ) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {//grandResults.length
            var check_result = true
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                startLocationUpdates();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )
                ) {
                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(
                        mLayout!!.rootView, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인", View.OnClickListener() {
                        //override void onClick(view:View) {
                        finish()
                        //}
                    }).show()
                } else {
                    Snackbar.make(
                        mLayout!!.rootView, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인", View.OnClickListener() {
                        finish()
                    }).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String?>, grandResults: IntArray) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates()
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )
                ) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(
                        mLayout!!, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인") { finish() }.show()
                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(
                        mLayout!!, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인") { finish() }.show()
                }
            }
        }
    }

    private fun showDialogForLocationServiceSetting(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MapActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
                앱을 사용하기 위해서는 위치 서비스가 필요합니다.
                위치 설정을 수정하실래요?
                """.trimIndent()
        )
        builder.setCancelable(true);
        builder.setPositiveButton("설정", DialogInterface.OnClickListener() {dialogInterface, i->
            //fun onClick(dialog: DialogInterface, id: Int) {
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
            //}
        })

        builder.setNegativeButton("취소", DialogInterface.OnClickListener() {dialogInterface, i ->
            //fun onClick(dialog: DialogInterface, id: Int) {
            dialogInterface.cancel()
            //}
        })
        builder.create().show()
        }

   override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GPS_ENABLE_REQUEST_CODE) {
            //사용자가 GPS 활성 시켰는지 검사
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {

                    Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
                    needRequest = true;
                    return;
                }
            }

        }

    }




    override fun onPlacesFailure(e: PlacesException?) {

    }

    fun showPlaceInformation(location:LatLng){
        mMap?.clear()
        if (previous_marker != null)
            previous_marker!!.clear() //지역정보 마커 클리어
        Log.d(TAG, NRPlaces.Builder().toString())

        var key = "AIzaSyDzepISLlztmiLEUZOPEaD8qb5AJFZFLXc"
        /*NRPlaces.Builder()
                .key("AIzaSyDzepISLlztmiLEUZOPEaD8qb5AJFZFLXc")
                .latlng(location.latitude, location.longitude) //현재 위치
                .radius(500) //500 미터 내에서 검색
                .type(PlaceType.RESTAURANT) //음식점
                .build()
        //var url: String = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location=" + location.latitude+"," + location.longitude +"&radius=500" + "&types=" + type + "&sensor=true" + sensor + "&key=" +
                .execute()
                Log.d(TAG, "get_api_key")
                */
         //이 url을 공공 url 이라고 생각 하고 파싱을 통해 제목과 소제목을 가져와 셋팅하기
        var makeUrlString: String = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=${key}&location=${location.latitude},${location.longitude}&radius=500&type=restaurant"
        //var urlString: String = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDzepISLlztmiLEUZOPEaD8qb5AJFZFLXc&location=36.3740203,127.3656701&radius=500&type=restaurant"
    }

    override fun onPlacesSuccess(places: List<Place>?) {
        runOnUiThread(Runnable() {
           fun run() {
               for (place in places!!) {
                   var latLng = LatLng(place.getLatitude(), place.getLongitude())
                  var markerSnippet: String = getCurrentAddress(latLng)
                   val markerOptions = MarkerOptions()
                   markerOptions.position(latLng) // <- 초기화 작업 필요
                   markerOptions.title(place.getName())
                   markerOptions.snippet(markerSnippet)
                   var item:Marker = mMap!!.addMarker(markerOptions)
                   previous_marker?.add(item)

               }

               //중복 마커 제거
               val hashSet = HashSet<Marker>()
               //HashSet<Marker> hashSet = new HashSet<Marker>();
               previous_marker?.let {
                   hashSet.addAll(it)
               }
               //hashSet.addAll(previous_marker);
               previous_marker?.clear();
               previous_marker?.addAll(hashSet);

           }

           })
        
    }

    override fun onPlacesFinished() {
    }

    override fun onPlacesStart() {
    }
}


