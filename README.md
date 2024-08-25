ShopSmart - Your Smart Shopping Companion
Welcome to ShopSmart, an intuitive and user-friendly e-commerce app designed to provide users with a seamless shopping experience. Whether you're browsing through our curated collections or managing your wishlist, ShopSmart offers all the essential features for a modern shopping app.

Key Features
User Authentication: Secure login and registration process with email verification to ensure a safe shopping environment.

Product Listings: Browse through a wide range of products across various categories like electronics, clothing, beauty products, and more.

Product Details: View detailed product descriptions, images, ratings, and reviews to make informed purchase decisions.

Wishlist Management: Add products to your wishlist for future reference. The wishlist is stored and synced with the server, ensuring access across multiple devices.

Cart Functionality: Easily add products to your cart and manage quantities with a user-friendly interface. The cart also supports removing items and calculating total costs, including taxes and delivery charges.

Order Placement: Place orders with a simple checkout process. Choose your preferred delivery address and payment method. An 8-digit unique order ID is generated for tracking, and the order date and status are stored for user reference.

Delivery Address Management: Add, edit, and delete delivery addresses. Select a preferred address for quick checkouts.

Favorites & Wishlist: Toggle the favorite status of products. Products marked as favorites are displayed in the Wishlist Fragment for easy access.

Seamless Navigation: Enjoy smooth transitions between different sections of the app using the Bottom Navigation View, customized to display icons and text dynamically based on the selected tab.

Order Tracking: Keep track of your orders in the "My Orders" section, complete with status updates and detailed order information.

Technologies Used
Kotlin: The primary programming language for the app, leveraging its concise syntax and safety features.

Jetpack Navigation Component: For managing app navigation, ensuring a smooth and user-friendly flow throughout the app.

ViewModel and LiveData: For managing UI-related data lifecycle consciously and reacting to data changes asynchronously.

Firebase Firestore: Used as the primary database for storing user and product data. Firestore’s real-time syncing ensures that all user actions (like adding to the cart or wishlist) are updated instantly.

Firebase Authentication: Secure and reliable authentication method for managing user sign-in and sign-up processes.

Glide: For loading images efficiently into the app, providing smooth scrolling and optimized memory usage.

RecyclerView and Adapters: Utilized for creating dynamic lists such as product listings, wishlists, and order history. Custom adapters have been implemented to handle complex data sets and interactions.

Retrofit: For network operations, particularly for fetching weather data and other required APIs, ensuring robust and efficient communication with external services.

Shared Preferences: For managing small, simple key-value pairs, particularly useful for storing user login status and preferences locally on the device.

App Structure
Authentication: Secure login and registration screens implemented using Firebase Authentication.

HomeFragment: The main landing page displaying promotional banners (using BannerViewPager) and popular products.

ProductDetailsFragment: Detailed view of selected products, allowing users to add items to their cart or wishlist.

CartFragment: Displays all items added to the cart, with functionalities to update quantities, remove items, and proceed to checkout.

WishlistFragment: Displays all products marked as favorites by the user.

MyOrderFragment: Allows users to track their placed orders and view detailed information about each order.

AddProductFragment: Admin-specific functionality for adding new products, complete with product details and image uploads to Firestore.

ViewAllProductFragment: Admin-specific functionality for viewing all products, with options to edit or delete products directly.

Address Management: Separate fragments for viewing all addresses (ViewAllAddressFragment) and adding new addresses (AddDeliveryAddressFragment).

![WhatsApp Image 2024-08-25 at 7 00 46 AM](https://github.com/user-attachments/assets/c6b56045-c736-4189-ad97-6e2a4ed6e58c)

Getting Started
To run this project locally, follow these steps:

Clone the repository:

bash
Copy code
git clone https://github.com/YourUsername/ShopSmart.git
cd ShopSmart
Set up Firebase:

Create a Firebase project in the Firebase Console.
Add your Android app to the Firebase project and follow the setup instructions to obtain the google-services.json file.
Place the google-services.json file in the app/ directory of your project.
Configure dependencies:

Ensure that all dependencies in your build.gradle files are up-to-date.
Run the app:

Build and run the app on an Android device or emulator.
Screenshots
Include screenshots of different parts of the app here to give a visual overview of the user experience.


Contributions
Contributions are welcome! Please fork the repository and submit a pull request for any changes you would like to make. Be sure to follow the coding standards and provide detailed information about your changes in the pull request description.

License
This project is licensed under the MIT License - see the LICENSE file for details.
