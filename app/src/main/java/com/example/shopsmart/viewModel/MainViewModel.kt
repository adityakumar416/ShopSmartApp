package com.example.shopsmart.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopsmart.MainActivity
import com.example.shopsmart.modelClass.BannerModel
import com.example.shopsmart.modelClass.OrderModel
import com.example.shopsmart.modelClass.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {

    private val firebaseStorage = FirebaseStorage.getInstance().getReference("banners")
    private val firestoreBanners = FirebaseFirestore.getInstance().collection("banners")
    private val firestoreProduct = FirebaseFirestore.getInstance()
    private val cartCollection = firestoreProduct.collection("cartItems")

    // LiveData for banners and products
    val bannerList = MutableLiveData<List<BannerModel>>()
    val productList = MutableLiveData<List<ProductModel>>()

    // LiveData for cart items
     val cartItems = MutableLiveData<List<ProductModel>>()

     val removeCartItems = MutableLiveData<List<ProductModel>?>()

    val orderList = MutableLiveData<List<OrderModel>>()


    // Fetch banners from Firestore
    fun fetchBanners() {
        firestoreBanners.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                bannerList.postValue(emptyList())
                return@addSnapshotListener
            }
            val banners = snapshot.toObjects(BannerModel::class.java)
            bannerList.postValue(banners)
        }
    }

    // Fetch all products from Firestore
    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val allProductsSnapshot = firestoreProduct.collection("allproducts").get().await()
                val products = allProductsSnapshot.toObjects(ProductModel::class.java)
                productList.postValue(products)
                Log.d("MainViewModel", "Fetched all products: $products")
            } catch (e: Exception) {
                productList.postValue(emptyList())
                Log.e("MainViewModel", "Error fetching all products", e)
            }
        }
    }

    // Add a product to the cart if it doesn't already exist
    fun addToCart(product: ProductModel, activity: MainActivity) {
        viewModelScope.launch {
            try {
                val existingProduct = cartCollection.document(product.id).get().await()
                if (existingProduct.exists()) {
                    Toast.makeText(activity, "Product is already added to the cart", Toast.LENGTH_SHORT).show()
                    Log.d("MainViewModel", "Product already in cart: ${product.id}")
                } else {
                    cartCollection.document(product.id).set(product).await()
                    Toast.makeText(activity, "Product added to cart", Toast.LENGTH_SHORT).show()
                    Log.d("MainViewModel", "Product added to cart: ${product.id}")
                }
            } catch (e: Exception) {
                Toast.makeText(activity, "Failed to add product to cart", Toast.LENGTH_SHORT).show()
                Log.e("MainViewModel", "Error adding product to cart", e)
            }
        }
    }


    // Fetch cart items from Firestore
    fun fetchCartItems() {
        viewModelScope.launch {
            try {
                val cartSnapshot = cartCollection.get().await()
                val items = cartSnapshot.toObjects(ProductModel::class.java)
                cartItems.postValue(items)
            } catch (e: Exception) {
                cartItems.postValue(emptyList())
                Log.e("MainViewModel", "Error fetching cart items", e)
            }
        }
    }

    fun updateCartItem(product: ProductModel) {
        viewModelScope.launch {
            try {
                cartCollection.document(product.id).set(product).await()
                Log.d("MainViewModel", "Cart item updated: ${product.id}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error updating cart item", e)
            }
        }
    }

    fun removeCartItem(product: ProductModel) {
        viewModelScope.launch {
            try {
                cartCollection.document(product.id).delete().await()

                val updatedList = removeCartItems.value?.toMutableList()
                updatedList?.remove(product)
                removeCartItems.postValue(updatedList)

                Log.d("MainViewModel", "Cart item removed: ${product.id}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error removing item from cart", e)
            }
        }
    }

    fun updateProductFavoriteStatus(product: ProductModel) {
        viewModelScope.launch {
            try {
                // Update the favorite status in Firestore
                firestoreProduct.collection("allproducts").document(product.id)
                    .update("favoriteProduct", product.favoriteProduct)
                    .await() // Await to ensure the operation completes

                Log.d("MainViewModel", "Favorite status updated for product: ${product.id}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error updating favorite status", e)
            }
        }
    }


    fun placeOrder(order: OrderModel, activity: MainActivity) {
        val db = FirebaseFirestore.getInstance()
        val ordersCollection = db.collection("orders")

        ordersCollection.document(order.orderId)
            .set(order)
            .addOnSuccessListener {
                Toast.makeText(activity, "Order placed successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Failed to place order: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    fun fetchOrders() {
        viewModelScope.launch {
            try {
                val ordersCollection = FirebaseFirestore.getInstance().collection("orders")
                val orderSnapshot = ordersCollection.get().await()
                val orders = orderSnapshot.toObjects(OrderModel::class.java)
                orderList.postValue(orders)
                Log.d("MainViewModel", "Fetched orders: $orders")
            } catch (e: Exception) {
                orderList.postValue(emptyList())
                Log.e("MainViewModel", "Error fetching orders", e)
            }
        }
    }



}
