package uz.rdo.projects.searchbookyandexmap.ui.fragments.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yandex.mapkit.geometry.Point
import uz.rdo.projects.searchbookyandexmap.databinding.FragmentMapBinding
import java.lang.NullPointerException

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding ?: throw NullPointerException("view is not available")

    // user use points
    private var currentPoint = Point(41.311081, 69.240562)
    private var tappedPoint: Point? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
}