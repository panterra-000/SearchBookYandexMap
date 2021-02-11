package uz.rdo.projects.searchbookyandexmap.ui.fragments.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import uz.rdo.projects.searchbookyandexmap.MainActivity
import uz.rdo.projects.searchbookyandexmap.R
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.FragmentMapBinding
import uz.rdo.projects.searchbookyandexmap.ui.adapters.recycler.ResultAdapter
import uz.rdo.projects.searchbookyandexmap.utils.*

class MapFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding ?: throw NullPointerException("view is not available")

    private var selectedPlace: PlaceModel? = null


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

    private fun setBottomView(placeModel: PlaceModel?) {
        (requireActivity() as MainActivity).setVisibilityBottomMenu(false)

        binding.bottom.apply {
            root.show()
            btnAddAddressBottom.setOnClickListener {
                showToast("ai", requireContext())
            }
            imgCloseBottom.setOnClickListener {
                root.hide()
                (requireActivity() as MainActivity).setVisibilityBottomMenu(true)
            }
            txtTitleBottom.text = "qKLDKA AWD"
        }
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

        adapter!!.setOnclickItemListener { placeModel ->
            Toast.makeText(
                requireContext(),
                "${placeModel.title} , ${placeModel.subtitle}",
                Toast.LENGTH_SHORT
            ).show()

            selectedPlace = placeModel
            val point = Point(placeModel.latitude, placeModel.longitude)
            moveCameraPosition(point)
            hideKeyboard(requireActivity())
            binding.etSearch.setText("")
            setBottomView(null)
        }

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
            hideKeyboard(requireActivity())
            moveCameraPosition(tappedPoint!!)
        }

        selectionMetadata != null
    }

    private fun moveCameraPosition(point: Point) {
        binding.mapView.map.move(
            CameraPosition(point, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0.7f),
            null
        )
    }

    private fun loadSuggestListener() {
        val searchManager =
            SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {

                    Log.d("tto", "dddddd tto ttto ttto ttto ttto ttto tttto ttttto ")
                }

                override fun beforeTextChanged(
                    text: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onTextChanged(
                    text: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val searchOption = SearchOptions()
                    searchOption.snippets = Snippet.BUSINESS_RATING1X.value

                    searchManager.submit(
                        text.toString(),
                        VisibleRegionUtils.toPolygon(binding.mapView.map.visibleRegion),
                        searchOption,
                        searchListener
                    )

                    var drawable = requireActivity().resources.getDrawable(R.drawable.ic_close)

                    if (text.toString().trim() == "") {
                        binding.rvResults.hide()
                    } else {
                        binding.rvResults.show()
                    }
                }
            })
    }

    private val searchListener = object : Session.SearchListener {
        override fun onSearchError(p0: Error) {
        }

        override fun onSearchResponse(response: Response) {
            val resultList = ArrayList<PlaceModel>()
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

                val placeMoreData =
                    suggest.obj?.metadataContainer?.getItem(BusinessRating1xObjectMetadata::class.java)

                Log.d("TUU", "$placeMoreData")

                val placeM = PlaceModel(
                    id = 0,
                    title = suggest.obj?.name.toString(),
                    subtitle = suggest.obj?.descriptionText.toString(),
                    distance = distanceOf.metrToKM(),
                    allReview = placeMoreData?.ratings,
                    score = placeMoreData?.score,
                    latitude = foundPoint!!.latitude,
                    longitude = foundPoint!!.longitude
                )
                Log.d("1997O", "placeM : ")
                resultTitleList.add(placeM.title)
                resultList.add(placeM)
            }
//            showToast(resultList.toString(), requireContext())
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