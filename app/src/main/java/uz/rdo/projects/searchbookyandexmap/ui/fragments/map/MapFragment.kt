package uz.rdo.projects.searchbookyandexmap.ui.fragments.map

import android.R
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import uz.rdo.projects.searchbookyandexmap.data.model.PlaceM
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.FragmentMapBinding
import uz.rdo.projects.searchbookyandexmap.ui.adapters.recycler.ResultAdapter
import uz.rdo.projects.searchbookyandexmap.utils.metrToKM
import uz.rdo.projects.searchbookyandexmap.utils.showToast
import java.lang.NullPointerException

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding ?: throw NullPointerException("view is not available")

    private var currentPoint = Point(41.311081, 69.240562)
    private var tappedPoint: Point? = null

    private var adapter: ResultAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadObservers()
        loadViews()
        loadMapListeners()
    }

    private fun loadObservers() {
    }

    private fun loadViews() {
        binding.apply {
            btnNavigateMe.setOnClickListener {
                moveCameraPosition(currentPoint)
            }
        }
        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())
        adapter = ResultAdapter(arrayListOf())
        binding.rvResults.adapter = adapter
    }


    private fun loadMapListeners() {
        moveCameraPosition(currentPoint)
        binding.mapView.map.apply {
            addCameraListener(cameraPositionListener)
            addTapListener(geoObjectTapListener)
        }
        loadSuggestListener()
    }

    // map listeneres

    @SuppressLint("SetTextI18n")
    private val cameraPositionListener: CameraListener =
        CameraListener { p0, cameraPosition, cameraUpdateReason, isStopped ->
            if (cameraPosition.target.latitude !== 0.0) {
                if (isStopped) {
                    binding.mapView.map.deselectGeoObject()
                }
            }
        }


    private val geoObjectTapListener = GeoObjectTapListener { geoObjectTapEvent ->
        val name = geoObjectTapEvent.geoObject.name
        val selectionMetadata = geoObjectTapEvent.geoObject.metadataContainer.getItem(
            GeoObjectSelectionMetadata::class.java
        )

        var latitude = geoObjectTapEvent.geoObject.geometry[0].point?.latitude
        var longitude = geoObjectTapEvent.geoObject.geometry[0].point?.longitude


        showToast(" x = $latitude ,y = $longitude", requireContext())

        if (latitude != null && longitude != null) {
            tappedPoint = Point(latitude, longitude)

            moveCameraPosition(tappedPoint!!)
        }

        selectionMetadata != null
    }

    private fun moveCameraPosition(point: Point) {
        binding.mapView.map.move(
            CameraPosition(point, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    private fun loadSuggestListener() {
        val searchManager =
            SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    binding.rvResults.visibility =
                        if (s.toString().trim() == "") {
                            View.GONE
                        } else {
                            View.VISIBLE
                        }
                }

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
                    foundLocation.latitude = foundPoint.latitude
                    foundLocation.latitude = foundPoint.longitude
                    distanceOf = currentLocation.distanceTo(foundLocation)
                }

                val placeM = PlaceM(
                    title = suggest.obj?.name.toString(),
                    subtitle = suggest.obj?.descriptionText.toString(),
                    distance = distanceOf.metrToKM(),
                    point = foundPoint

                )

                resultTitleList.add(placeM.title)
                resultList.add(placeM)
            }
            adapter?.submitList(resultList)
        }
    }


    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}