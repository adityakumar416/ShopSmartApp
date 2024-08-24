package com.example.shopsmart.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopsmart.MainActivity
import com.example.shopsmart.addDeliveryAddress.AddressModel
import com.example.shopsmart.modelClass.BannerModel
import com.example.shopsmart.modelClass.OrderModel
import com.example.shopsmart.modelClass.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel(application: Application) : AndroidViewModel(application) {

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

    // LiveData to observe the filtered order list in the fragment
    val orderList = MutableLiveData<List<OrderModel>>()

    // A variable to store the original list of all fetched orders
    private var originalOrderList: List<OrderModel> = emptyList()

    private val sharedPreferences = application.getSharedPreferences("ShopSmartPrefs", Context.MODE_PRIVATE)
    val selectedPaymentMethod = MutableLiveData<String>()

    init {
        // Load saved payment method from SharedPreferences
        selectedPaymentMethod.value = loadPaymentMethod()
    }

    fun savePaymentMethod(method: String) {
        selectedPaymentMethod.value = method
        sharedPreferences.edit().putString("payment_method", method).apply()
    }

    private fun loadPaymentMethod(): String {
        return sharedPreferences.getString("payment_method", "Cash on Delivery") ?: "Cash on Delivery"
    }

    // user address
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val addresses = MutableLiveData<List<AddressModel>>()

    val saveStatus = MutableLiveData<Boolean>()

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
                    Toast.makeText(
                        activity,
                        "Product is already added to the cart",
                        Toast.LENGTH_SHORT
                    ).show()
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
                Toast.makeText(activity, "Failed to place order: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }

    fun fetchOrders() {
        viewModelScope.launch {
            try {
                val ordersCollection = FirebaseFirestore.getInstance().collection("orders")
                val orderSnapshot = ordersCollection.get().await()
                val orders = orderSnapshot.toObjects(OrderModel::class.java)

                // Save the fetched orders to originalOrderList
                originalOrderList = orders

                // Post the fetched orders to the LiveData
                orderList.postValue(originalOrderList)
                Log.d("MainViewModel", "Fetched orders: $orders")
            } catch (e: Exception) {
                orderList.postValue(emptyList())
                Log.e("MainViewModel", "Error fetching orders", e)
            }
        }
    }


    // Save the address under the "addresses" node only
    fun saveAddress(addressModel: AddressModel) {
        val documentReference = firestore.collection("addresses").document()

        val addressWithId = addressModel.copy(id = documentReference.id)
        documentReference.set(addressWithId)
            .addOnSuccessListener {
                saveStatus.value = true
            }
            .addOnFailureListener {
                saveStatus.value = false
            }
    }

    fun updateAddress(address: AddressModel) {
        val document = firestore.collection("addresses").document(address.id)
        document.set(address)
            .addOnSuccessListener {
                saveStatus.value = true
            }
            .addOnFailureListener {
                saveStatus.value = false
            }
    }

    // ViewModel code for fetching and managing addresses
    fun fetchAddresses() {
        firestore.collection("addresses")
            .get()
            .addOnSuccessListener { documents ->
                val addressList = documents.mapNotNull { document ->
                    document.toObject(AddressModel::class.java)?.copy(id = document.id)
                }
                addresses.value = addressList
            }
            .addOnFailureListener {
                addresses.value = emptyList()
            }
    }


    private val userId = "user-id" // Replace with actual user ID

    val selectedAddress = MutableLiveData<AddressModel?>()


    fun selectAddress(address: AddressModel) {
        selectedAddress.value = address
        saveSelectedAddressToFirestore(address)
    }

    private fun saveSelectedAddressToFirestore(address: AddressModel) {
        firestore.collection("users")
            .document(userId)
            .collection("addresses")
            .document("selectedAddress")
            .set(address) // Firestore handles serialization
    }

    fun loadSelectedAddressFromFirestore() {
        firestore.collection("users")
            .document(userId)
            .collection("addresses")
            .document("selectedAddress")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val address = document.toObject(AddressModel::class.java)
                    selectedAddress.value = address

                    // Ensure the correct address is marked as selected in the list
                    addresses.value?.forEach {
                        it.selectedAddress = it.id == address?.id
                    }
                }
            }
    }


    fun filterOrders(status: String) {
        orderList.value = if (status == "All") {
            originalOrderList
        } else {
            originalOrderList.filter { it.orderStatus == status }
        }
    }



}