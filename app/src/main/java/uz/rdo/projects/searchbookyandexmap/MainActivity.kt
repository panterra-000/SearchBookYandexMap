package uz.rdo.projects.searchbookyandexmap

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import uz.rdo.projects.searchbookyandexmap.databinding.ActivityMainBinding
import uz.rdo.projects.searchbookyandexmap.data.model.PlaceM
import uz.rdo.projects.searchbookyandexmap.utils.metrToKM

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private var currentPoint = Point(41.311081, 69.240562)

    private var tappedPoint: Point? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MapKitFactory.initialize(this);


        loadMap()
        loadMapListeners()
        loadButtonViews()
        loadSuggestListener()
    }

    private fun loadSuggestListener() {
        val searchManager =
            SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    text: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    searchManager.submit(
                        text.toString(),
                        VisibleRegionUtils.toPolygon(binding.mapView.map.visibleRegion),
                        SearchOptions(),
                        searchListener
                    )
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
    }

    private val searchListener = object : Session.SearchListener {
        override fun onSearchError(p0: Error) {
        }

        override fun onSearchResponse(response: Response) {
            val resultList = ArrayList<PlaceM>()
            val resultTitleList = ArrayList<String>()

            for (suggest in response.collection.children) {

                val currentLocation = Location("currentLocation")
                currentLocation.latitude = currentPoint.latitude
                currentLocation.longitude = currentPoint.longitude

                val foundPoint = suggest.obj?.geometry?.get(0)?.point
                val foundLocation = Location("foundLocation")
                var distanceOf = 0F

                if (foundPoint != null) {
                    foundLocation.latitude = foundPoint?.latitude
                    foundLocation.latitude = foundPoint?.longitude
                    distanceOf = currentLocation.distanceTo(foundLocation)
                }

                val place: PlaceM = PlaceM(
                    title = suggest.obj?.name.toString(),
                    subtitle = suggest.obj?.descriptionText ?: "No subtitle",
                    distance = if (distanceOf == 0F) "NaN" else distanceOf.metrToKM(),
                    point = foundPoint
                )
                resultTitleList.add(place.title)
                resultList.add(place)
            }
            binding.txtResult.text = resultTitleList.toString()
        }
    }


    private fun loadMapListeners() {
        binding.apply {
            mapView.map.addCameraListener(cameraPositionListener)
            mapView.map.addTapListener(geoObjectTapListener)
        }
    }

    private val geoObjectTapListener = GeoObjectTapListener { geoObjectTapEvent ->
        val name = geoObjectTapEvent.geoObject.name
        val selectionMetadata = geoObjectTapEvent.geoObject.metadataContainer.getItem(
            GeoObjectSelectionMetadata::class.java
        )

        val latitude = geoObjectTapEvent.geoObject.geometry[0].point?.latitude
        val longitude = geoObjectTapEvent.geoObject.geometry[0].point?.longitude

        binding.txtResult.text = name?.toString() ?: "no name"

        if (latitude != null && longitude != null) {
            tappedPoint = Point(latitude, longitude)
        }
        selectionMetadata != null
    }


    @SuppressLint("SetTextI18n")
    private val cameraPositionListener: CameraListener =
        CameraListener { p0, cameraPosition, cameraUpdateReason, isStopped ->
            if (cameraPosition.target.latitude !== 0.0) {
                if (isStopped) {
                    binding.mapView.map.deselectGeoObject()
                }
            }
        }


    @SuppressLint("SetTextI18n")
    private fun loadButtonViews() {

        binding.apply {
            btnShowMe.setOnClickListener {
                moveCameraPosition(currentPoint)
            }

            btnShowNowAddressPoint.setOnClickListener {
                val long = mapView.map.cameraPosition.target.longitude
                val lat = mapView.map.cameraPosition.target.latitude
                txtResult.text = "$long, $lat"
            }
        }
    }

    private fun loadMap() {
        binding.mapView.map.move(
            CameraPosition(Point(55.751574, 37.573856), 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )

    }

    private fun moveCameraPosition(point: Point) {
        binding.mapView.map.move(
            CameraPosition(point, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}