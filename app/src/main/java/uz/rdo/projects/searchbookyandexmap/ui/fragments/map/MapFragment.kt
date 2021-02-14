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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import uz.rdo.projects.searchbookyandexmap.data.room.db.MyDataBase
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.FragmentMapBinding
import uz.rdo.projects.searchbookyandexmap.ui.adapters.recycler.ResultAdapter
import uz.rdo.projects.searchbookyandexmap.ui.baseFactories.MapViewModelFactory
import uz.rdo.projects.searchbookyandexmap.ui.dialog.AddressSaveDialog
import uz.rdo.projects.searchbookyandexmap.utils.*

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding ?: throw NullPointerException("view is not available")

    private lateinit var viewModel: MapViewModel

    private var clickedPlaceModel: PlaceModel? = null
    private var currentPoint = Point(41.311081, 69.240562)
    private var tappedPoint: Point? = null

    private lateinit var adapter: ResultAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModel()
        loadObservers()
        loadViews()
        loadMapListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setBottomSheet(selectedPlaceM: PlaceModel) {
        showBottomSheet()
        binding.bottom.apply {
            txtTitleBottom.text = selectedPlaceM.title
            txtSubTitleBottom.text = selectedPlaceM.subtitle

            val score = selectedPlaceM.score
            val review = selectedPlaceM.allReview

            if (score != null && review != null) {
                txtScoreBottom.text = score.toString()
                ratingView.rating = score
                txtReviewBottom.text =
                    review.toString() + " ${resources.getString(R.string.review_txt)}"
                txtScoreBottom.show()
                ratingView.show()
                txtReviewBottom.show()
            } else {
                txtScoreBottom.hide()
                ratingView.hide()
                txtReviewBottom.hide()
            }

            btnAddAddressBottom.setOnClickListener {

                val dialog = AddressSaveDialog(requireActivity(), selectedPlaceM)
                dialog.show()

                dialog.setOnclickSaveCallback { placeModel ->
                    viewModel.addPlaceModelToDB(placeModel)
                }
            }
            imgCloseBottom.setOnClickListener {
                hideBottomSheet()
            }
        }
    }

    private fun hideBottomSheet() {
        binding.bottom.root.hide()
        (requireActivity() as MainActivity).setVisibilityBottomMenu(true)
    }

    private fun showBottomSheet() {
        binding.bottom.root.show()
        (requireActivity() as MainActivity).setVisibilityBottomMenu(false)
    }

    private fun setupViewModel() {
        val dao = MyDataBase.getDatabase().placeModelDao()
        var repositoryImpl = MapRepositoryImpl(dao)
        viewModel = ViewModelProviders.of(
            this,
            MapViewModelFactory(repositoryImpl)
        ).get(MapViewModel::class.java)
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.resultLiveData.observe(this, addToDB)
    }

    private val addToDB = Observer<Boolean> {
        showToast("Yes, added !!!", requireContext())
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

            clickedPlaceModel = placeModel
            val point = Point(placeModel.latitude, placeModel.longitude)
            moveCameraPosition(point)
            hideKeyboard(requireActivity())
            binding.etSearch.setText("")
            clickedPlaceModel?.let { clickedPlaceModel ->
                setBottomSheet(clickedPlaceModel)
            }
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

    @SuppressLint("SetTextI18n")
    private val cameraPositionListener =
        CameraListener { p0, cameraPosition, cameraUpdateReason, isStopped ->

            binding.tRV.text = "Yurvotiiii ........"
            binding.btnPin.changeFormer()
            var lat = cameraPosition.target.latitude
            var long = cameraPosition.target.longitude

            if (cameraPosition.target.latitude !== 0.0) {
                if (isStopped) {
                    binding.tRV.text = "qoyib yuborildi !!!"
                    binding.mapView.map.deselectGeoObject()
                    binding.btnPin.resumeFormer()
                }
            }
        }

    private val geoObjectTapListener = GeoObjectTapListener { geoObjectTapEvent ->

        val selectionMetadata = geoObjectTapEvent.geoObject.metadataContainer.getItem(
            GeoObjectSelectionMetadata::class.java
        )

        binding.tRV.text = if (geoObjectTapEvent != null) {
            "bosildi"
        } else {
            "qoyib yuborildi"
        }

        val businessData =
            geoObjectTapEvent.geoObject.metadataContainer.getItem(BusinessRating1xObjectMetadata::class.java)


        val currentLocation = Location("current")
        val foundLocation = Location("found")
        var distance = 0F

        val title = geoObjectTapEvent.geoObject.name
        val subTitle = geoObjectTapEvent.geoObject.descriptionText
        var latitude = geoObjectTapEvent.geoObject.geometry[0].point?.latitude
        var longitude = geoObjectTapEvent.geoObject.geometry[0].point?.longitude
        if (latitude != 0.0) {
            foundLocation.latitude = latitude!!
            foundLocation.longitude = longitude!!
            distance = currentLocation.distanceTo(foundLocation)
        }

        if (latitude != null && longitude != null) {
            tappedPoint = Point(latitude, longitude)
            hideKeyboard(requireActivity())
            moveCameraPosition(tappedPoint!!)

            val placeModel = PlaceModel(
                id = 0,
                title = title ?: " Title Not found",
                subtitle = subTitle ?: "Description Not found",
                distance = if (distance == 0.0F) "Nan" else distance.metrToKM(),
                allReview = null,
                score = null,
                longitude = longitude,
                latitude = latitude
            )

            setBottomSheet(placeModel)
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
                        hideBottomSheet()
                    }
                }
            })
    }

    private val searchListener = object : Session.SearchListener {
        override fun onSearchError(p0: Error) {
        }

        override fun onSearchResponse(response: Response) {
            val resultList = ArrayList<PlaceModel>()

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

