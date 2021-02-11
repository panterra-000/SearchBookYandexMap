package uz.rdo.projects.searchbookyandexmap.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.ItemResultBinding
import uz.rdo.projects.searchbookyandexmap.utils.SingleBlock

class ResultAdapter(private val placeList: ArrayList<PlaceModel>) :
    RecyclerView.Adapter<ResultAdapter.ResultHolder>() {

    private var listenClick: SingleBlock<PlaceModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder = ResultHolder(
        ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ResultAdapter.ResultHolder, position: Int) = holder.bind()

    inner class ResultHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.txtTitle.text = placeList?.get(adapterPosition)?.title ?: ""
            binding.txtSubtitle.text = placeList?.get(adapterPosition)?.subtitle ?: ""
            binding.txtDistance.text = placeList?.get(adapterPosition)?.distance ?: ""
            binding.root.setOnClickListener { listenClick?.invoke(placeList[adapterPosition]) }
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


}
