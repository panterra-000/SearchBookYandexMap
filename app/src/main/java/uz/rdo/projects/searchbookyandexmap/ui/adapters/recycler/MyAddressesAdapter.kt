package uz.rdo.projects.searchbookyandexmap.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.ItemPlaceDescBinding
import uz.rdo.projects.searchbookyandexmap.databinding.ItemResultBinding
import uz.rdo.projects.searchbookyandexmap.utils.SingleBlock
import uz.rdo.projects.searchbookyandexmap.utils.show

class MyAddressesAdapter(private val placeList: ArrayList<PlaceModel>) :
    RecyclerView.Adapter<MyAddressesAdapter.ResultHolder>() {

    private var listenClick: SingleBlock<PlaceModel>? = null

    private var listenClickLocation: SingleBlock<PlaceModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder = ResultHolder(
        ItemPlaceDescBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: MyAddressesAdapter.ResultHolder, position: Int) =
        holder.bind()

    inner class ResultHolder(private val binding: ItemPlaceDescBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                txtTitle.text = placeList[adapterPosition].title ?: ""
                txtSubtitle.text = placeList[adapterPosition].subtitle ?: ""
                txtCloseTime.text = "19:18"

                if (placeList[adapterPosition].score != null) {
                    ratingV.show()
                    txtScore.show()
                    txtCloseTime.show()
                    ratingV.rating = placeList[adapterPosition].score!!
                    txtScore.text = "${placeList[adapterPosition].score}"
                }
                root.setOnClickListener { listenClick?.invoke(placeList[adapterPosition]) }
                imageView.setOnClickListener { listenClickLocation?.invoke(placeList[adapterPosition]) }
            }
        }
    }

    fun submitList(places: ArrayList<PlaceModel>) {
        placeList.clear()
        placeList.addAll(places)
        notifyDataSetChanged()
    }

    fun setOnclickItemListener(f: SingleBlock<PlaceModel>) {
        listenClick = f
    }

    fun setOnclickCallbackLocation(f: SingleBlock<PlaceModel>) {
        listenClickLocation = f
    }

}
