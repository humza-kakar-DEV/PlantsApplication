package com.example.plantsservicefyp.fragment.buyer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.VideoListRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentVideoListBinding
import com.example.plantsservicefyp.model.VideoDetail
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoListFragment : Fragment() {

    private lateinit var binding: FragmentVideoListBinding

    private lateinit var videoListRecyclerViewAdapter: VideoListRecyclerViewAdapter

    private val videoList = mutableListOf<VideoDetail>(
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_1.jpg?alt=media&token=0eeffb62-dc5b-4a83-92e9-1978a05b0bd4",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_1.mp4?alt=media&token=c3839b52-e251-453c-8daa-7c175a268ba7",
            title = "2480 lb Giant Pumpkin Timelapse | aka Bear Swipe | Winner of Topsfield Fair",
            description = "A pumpkin, scientifically known as Cucurbita pepo, is a versatile winter squash characterized by its round or oblong shape, vibrant orange color, and thick, ribbed outer skin. The flesh, dense and bright orange, contains numerous seeds and stringy fibers. Pumpkins are widely used in various culinary dishes, including soups, pies, muffins, and bread. Pumpkin seeds, toasted and salted, are a popular snack. Nutritionally, pumpkins are low in calories and rich in vitamins A and C, fiber, potassium, and antioxidants. They hold cultural and symbolic significance, particularly during Halloween and Thanksgiving in the United States. Pumpkins grow on sprawling vines, requiring ample sunlight and well-drained soil. After use, they can be composted, contributing to environmental sustainability."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_2.jpg?alt=media&token=8dc12239-04dd-4b22-84ca-cd6c947e8a1b",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_2.mp4?alt=media&token=3454f0dc-3bf6-4216-8c33-5dac033ab22e",
            title = "Magical way to Grow Coriander In just 5 Days / How to grow Coriander at home",
            description = "Coriander (Coriandrum sativum) is a popular herb known for its distinct aroma and flavorful leaves. It is a slender, annual plant with delicate, feathery leaves and small, pale pink to white flowers. The leaves and seeds of the coriander plant are widely used in culinary applications around the world. The leaves, often referred to as cilantro, have a refreshing, citrusy taste, while the seeds are more aromatic and slightly spicy. Coriander is a staple in many cuisines, including Asian, Mediterranean, Indian, and Latin American, adding a unique taste and aroma to various dishes. It's easy to grow, requiring well-drained soil, adequate sunlight, and moderate watering. Coriander leaves are typically harvested before the plant blooms, while the seeds are collected once the flowers have dried."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_3.jpg?alt=media&token=1603eca8-6d73-4a37-a73a-ad421adf691e",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_3.mp4?alt=media&token=329d791f-199a-46db-ab61-ccb83757d808",
            title = "Easy and free fertilizer for any plants | Banana peel fertilizer",
            description = "Plant fertilizer is a nutrient-rich substance used to enhance plant growth and health. It provides essential elements such as nitrogen, phosphorus, and potassium that promote strong stems, lush leaves, vibrant blooms, and robust root systems. Fertilizers can come in various forms, including granular, liquid, or organic, tailored to specific plant needs. Proper application of fertilizer can optimize plant development, increase crop yields, and improve overall plant vitality. However, overuse or incorrect application can lead to environmental issues and plant damage. It's essential to follow recommended guidelines for each type of plant and adjust the fertilizer accordingly to achieve optimal growth."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_4.jpg?alt=media&token=2b40a3b1-30e4-498f-a8be-bd34440e422f",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_4.mp4?alt=media&token=cce55e69-0a9a-4b04-bb88-1794bd3e5f5c",
            title = "Best natural liquid fertilizer for plants , specially money plants",
            description = "Natural liquid fertilizer is an organic plant nutrient solution made from natural sources like compost, manure, or organic matter. It's typically in liquid form and contains essential elements like nitrogen, phosphorus, potassium, and various micronutrients. This fertilizer is easily absorbed by plants through their roots and leaves, providing a quick and efficient nutrient boost. It enriches the soil, promotes healthy growth, and enhances plant productivity. It's an eco-friendly alternative to synthetic fertilizers and supports sustainable gardening practices. Diluted with water, it can be applied directly to the soil or sprayed onto the plants, aiding in their overall development and health.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_5.jpg?alt=media&token=a7a41b88-bf29-4810-9edf-f72d780a5b6d",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_5.mp4?alt=media&token=72d0ab64-5e61-4fd5-b929-58b21fc97d9a",
            title = "Bean Time-Lapse - 25 days | Soil cross section",
            description = "A bean plant, scientifically known as Phaseolus vulgaris, is an annual plant with slender stems and green, compound leaves. The plant typically grows in an upright manner, producing clusters of delicate white or pink flowers. These flowers eventually give way to long, slender pods containing multiple seeds, which are the edible beans. The beans come in various colors, including green, yellow, purple, or brown, depending on the variety. Bean plants thrive in well-drained soil and require ample sunlight for optimal growth. They are a staple in many cuisines worldwide, valued for their nutritional value and versatility in a wide range of dishes.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_6.jpg?alt=media&token=b078448f-d379-454b-85f0-60fe54b0ef81",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_6.mp4?alt=media&token=7319a7dc-7df6-4499-b776-b9c986729206",
            title = "Growing Sunflower Time Lapse - Seed To Flower In 83 Days",
            description = "Sunflower (Helianthus annuus) is an annual flowering plant known for its iconic bright yellow, daisy-like flowers with large, dark centers. The plant can grow to impressive heights, featuring thick, sturdy stems and coarse, hairy leaves. Sunflowers are native to North America but are now cultivated globally for their ornamental beauty and various practical uses. The flowers, often facing the sun, give the plant its name. Sunflower seeds, found within the flower heads, are a popular snack and a rich source of oil used in cooking and food production. The plant is easy to grow, thriving in well-drained soil and ample sunlight."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_7.jpg?alt=media&token=686532f6-c289-49b5-bc5a-94fd94c4e634",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_7.mp4?alt=media&token=497960a3-4e74-40d1-8c97-095f3c996c7a",
            title = "Spinach Time-Lapse - 40 days | Soil cross section",
            description = "Spinach (Spinacia oleracea) is an annual leafy green plant. It features dark green, tender, and somewhat wrinkled leaves. The leaves are generally triangular, with a defined central vein. Spinach plants typically have a compact, rosette-like growth habit. This vegetable is known for its mild, slightly earthy taste and is often used in salads, sautés, soups, and various other dishes. It is a nutrient-dense plant, rich in vitamins, minerals, and antioxidants, making it a popular choice for a healthy diet. Spinach prefers well-drained, fertile soil and thrives in cooler weather. It's a fast-growing plant and can be harvested multiple times during its growth cycle.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_8.jpg?alt=media&token=9fc91524-d8b8-4e7c-b5a0-2e7d6b72b7ec",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_8.mp4?alt=media&token=0f53648e-abdd-4c1c-b03c-5fb7022627d1",
            title = "Growing Cranberry Bean Time Lapse - Seed To Pod in 42 Days",
            description = "Cranberry beans, scientifically known as Phaseolus vulgaris, are medium-sized beans with a distinctive appearance. They feature a cream-colored base with vibrant red streaks, resembling cranberries. These beans are plump, kidney-shaped, and have a mild, nutty flavor. They are commonly used in various cuisines, particularly in Italian and Mediterranean dishes. When cooked, cranberry beans develop a creamy texture and a slightly sweet taste. They are a good source of protein, fiber, vitamins, and minerals. In the garden, cranberry beans grow on bushy plants and are relatively easy to cultivate."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_9.jpg?alt=media&token=d39018c7-59a6-4fb9-b737-bd354e98abb8",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_9.mp4?alt=media&token=f1747930-23a6-4aee-9191-0fca38817362",
            title = "Growing Watermelon Plant Time Lapse - Seed to Fruit (110 Days)",
            description = "Watermelon plants (Citrullus lanatus) are sprawling vines with large, lobed leaves. They produce elongated fruits with thick green rinds and juicy, sweet, red or yellow flesh. The seeds are typically black or brown and are found throughout the flesh. Watermelon plants require warm temperatures, well-drained soil, and plenty of sunlight. They thrive in sandy or loamy soils. The fruit, a popular summertime treat, requires consistent watering for optimal growth. Proper care, including pruning and fertilizing, helps ensure a successful watermelon harvest."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_10.jpg?alt=media&token=16d21f87-d72f-4b3a-ad8d-d9559b546dcb",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_10.mp4?alt=media&token=876957b4-6231-4ea4-afe5-af28acbe964e",
            title = "Grow cardamom from seeds | Grow Properly at home | Grow plants from seeds",
            description = "Cardamom (Elettaria cardamomum) is a perennial plant known for its aromatic seeds and attractive foliage. It grows from rhizomes and has long, reed-like stems. The plant produces lance-shaped leaves and fragrant flowers with a pale green color. Cardamom plants require warm, tropical climates, well-drained soil, and partial shade for optimal growth. Regular watering and a consistent, humid environment are essential for its development. The seeds, found in the plant's pods, are a popular spice used in both sweet and savory dishes. Harvesting typically occurs when the pods are fully mature, and the seeds are ready for use."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_11.jpg?alt=media&token=226456a0-485e-49b6-afc2-97f5a6d37512",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_11.mp4?alt=media&token=fa7acc00-189c-4ce7-9cf4-bc0adcb2255c",
            title = "Growing Kiwi Plant From Seed (128 Days Time Lapse)",
            description = "Kiwi plants (Actinidia deliciosa) are vigorous climbers with large, heart-shaped leaves. They require well-drained, slightly acidic soil and a sunny location for optimal growth. Kiwi plants are dioecious, meaning they have separate male and female plants. Planting both types ensures successful fruit production. Regular watering, especially during dry periods, is crucial for healthy growth. Kiwi plants need a sturdy support structure for climbing, like a trellis or arbor. Pruning is essential to manage growth and encourage fruiting. Fruits are typically ready for harvest in late fall when they reach their desired size and firmness."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_12.jpg?alt=media&token=fd61aef7-5ba9-4fd1-b1e3-bc5f4ce16152",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_12.mp4?alt=media&token=23973e26-ccad-4bae-91b7-767c76708481",
            title = "Watermelon in soil - Time Lapse",
            description = "Watermelon plants thrive in well-drained, sandy or loamy soil with a slightly acidic to neutral pH. They require ample sunlight and warm temperatures for optimal growth. Adequate watering is crucial during the growing season, especially during dry periods, to ensure healthy fruit development. Proper fertilization and regular pest control measures are essential for a successful watermelon harvest. The fruit is usually ready for harvest when it reaches the desired size, has a sweet aroma, and produces a dull sound when tapped."
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fthumbnail_13.jpg?alt=media&token=ddd87e80-4bb4-40d8-842e-aabe5d30cde7",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/plants-market-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_13.mp4?alt=media&token=ba5e9256-5f63-4316-97dc-408a51316550",
            title = "How Does A Seed Become A Plant?",
            description = "A seed becomes a plant through germination. When planted in soil and watered, the seed absorbs water and swells, breaking its coat. Enzymes are activated, breaking down stored nutrients like starches and proteins, providing energy for growth. A root (radicle) and a shoot (embryonic stem) emerge. The root anchors the plant and absorbs water, while the shoot grows upward. Cotyledons or true leaves appear, starting photosynthesis. Photosynthesis, fueled by sunlight, begins, producing glucose for the plant's growth. With more leaves, stems, and roots, the plant can photosynthesize and absorb nutrients effectively. Continued growth leads to flower formation and, after pollination and fertilization, the development of seeds within the fruit. When mature, seeds are dispersed, fall to the ground, germinate, and grow into new plants, restarting the cycle."
        )
    )

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoListBinding.inflate(layoutInflater, container, false)

        videoListRecyclerViewAdapter = VideoListRecyclerViewAdapter(requireContext()) { position ->
            sharedViewModel.setVideoDetail(videoDetail = videoList[position])
            sharedViewModel.changeFragment(ChangeFragment.VIDEO_PLAYER_FRAGMENT)
        }

        videoListRecyclerViewAdapter.items = videoList

        binding.videoPlayerRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.videoPlayerRecyclerView.adapter = videoListRecyclerViewAdapter

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
}























