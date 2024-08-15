package com.example.shopsmart.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopsmart.modelClass.BannerModel
import com.example.shopsmart.modelClass.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {

    private val firebaseStorage = FirebaseStorage.getInstance().getReference("banners")
    private val firestore = FirebaseFirestore.getInstance().collection("banners")
    private val firestoreProduct = FirebaseFirestore.getInstance()

    val bannerList = MutableLiveData<List<BannerModel>>()

     val productList = MutableLiveData<List<ProductModel>>()


    fun fetchBanners() {
        firestore.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                bannerList.postValue(emptyList())
                return@addSnapshotListener
            }
            val banners = snapshot.toObjects(BannerModel::class.java)
            bannerList.postValue(banners)
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val allProductsSnapshot = firestoreProduct.collection("allproducts").get().await()
                val products = allProductsSnapshot.toObjects(ProductModel::class.java)

                productList.postValue(products)
                Log.d("ProductViewModel", "Fetched all products: $products")
            } catch (e: Exception) {
                productList.postValue(emptyList())
                Log.e("ProductViewModel", "Error fetching all products", e)
            }
        }
    }

}