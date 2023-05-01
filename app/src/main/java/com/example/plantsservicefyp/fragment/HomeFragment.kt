package com.example.plantsservicefyp.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.SpecialOfferRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentHomeBinding
import com.example.plantsservicefyp.model.Plant

class HomeFragment(private val homeFragmentCallBack: () -> Unit) : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var specialOfferPlantData = listOf<Plant>(
        Plant(
            name = "Butter Fruit",
            price = "3,350",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2021/04/images-2021-04-26T120957.224-510x340.jpeg",
            rating = "1.2",
            sold = "14"
        ),
        Plant(
            name = "Cassia Javanica",
            price = "480",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2018/03/ETL-1047-510x510.jpg",
            rating = "1.6",
            sold = "10"
        ),
        Plant(
            name = "Cassia fistula",
            price = "7,280",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2018/03/ETL-1045-510x510.jpg",
            rating = "1.1",
            sold = "9"
        ),
        Plant(
            name = "Ficus Microcarpa",
            price = "2,377",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2018/03/ETL-1052-510x510.jpg",
            rating = "3.1",
            sold = "33"
        ),
        Plant(
            name = "Senghor",
            price = "4,560",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2019/05/Buy-Seedless-hybrid-Lemon-Plant-with-high-yield-online-in-Pakistan-Lahore-Karachi-and-Islamabad-510x720.png",
            rating = "3.7",
            sold = "54"
        ),
        Plant(
            name = "Marginata",
            price = "2,240",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2020/02/Buy-Dracaena-Online-in-Pakistan-Lahore-Karachi-Islamabad-from-etree-1-510x510.jpg",
            rating = "2.3",
            sold = "18"
        ),
        Plant(
            name = "Meetha Neem",
            price = "1,875",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2018/03/ETL-1069-510x510.jpg",
            rating = "4.7",
            sold = "76"
        )
    )

    private var mostPopularPlantData = listOf<Plant>(
        Plant(
            name = "Lucky Bamboo",
            price = "7,256",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2019/09/Buy-Lucky-Bamboo-Plant-online-in-Karachi-Lahore-Islamabad-and-Pakistan-510x510.jpg",
            rating = "4.9",
            sold = "89"
        ),
        Plant(
            name = "Aeonium decorum",
            price = "3,558",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2021/12/Profile-6-510x509.png",
            rating = "3.1",
            sold = "17"
        ),
        Plant(
            name = "Aglaonema Red",
            price = "5,070",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2021/03/aglaonema-red-valentine-5in-in-anna-ceramic.jpg",
            rating = "1.6",
            sold = "7"
        ),
        Plant(
            name = "Aglaonema siam",
            price = "5,099",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2019/10/aglaonema-siam-aurora-aglaonema-lipstick-chinese-evergreen-red-plant-510x510.jpg",
            rating = "4.2",
            sold = "44"
        ),
        Plant(
            name = "Aglaonema Snow",
            price = "5,100",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2019/10/images-2019-10-12T154307.249-510x510.jpeg",
            rating = "3.9",
            sold = "34"
        ),
        Plant(
            name = "Aglaonema Pink",
            price = "4,099",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2019/10/PicsArt_10-12-04.15.34-510x346.jpg",
            rating = "2.5",
            sold = "21"
        ),
        Plant(
            name = "Aglaonema Green",
            price = "5,088",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2019/10/20191012_155816-510x510.jpg",
            rating = "2.1",
            sold = "8"
        ),
        Plant(
            name = "Elephant Plant",
            price = "14,677",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2019/12/alocasia-plant.jpg",
            rating = "3.5",
            sold = "27"
        ),
        Plant(
            name = "Aloe Striatula",
            price = "2,691",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2021/12/Profile-8-510x510.png",
            rating = "3.7",
            sold = "17"
        ),
        Plant(
            name = "Ajwain Plant",
            price = "2,345",
            imageDownloadUrl = "https://etree.pk/wp-content/uploads/2021/09/ajwain_top_1.jpg",
            rating = "2.3",
            sold = "19"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        var specialOfferRecyclerViewAdapter =
            SpecialOfferRecyclerViewAdapter(requireContext(), specialOfferPlantData)
        var mostPopularRecyclerViewAdapter =
            SpecialOfferRecyclerViewAdapter(requireContext(), mostPopularPlantData)

        binding.specialOfferRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.specialOfferRecyclerView.adapter = specialOfferRecyclerViewAdapter

        binding.mostPopularRecyclerView.layoutManager = GridLayoutManager(context, 2).apply {
            orientation = GridLayoutManager.VERTICAL
        }

        binding.mostPopularRecyclerView.adapter = mostPopularRecyclerViewAdapter

        binding.seeAllTextView.setOnClickListener {
            homeFragmentCallBack()
        }

        return binding.root
    }
}