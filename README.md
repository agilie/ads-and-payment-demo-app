# AdsDemoApp

The demo application demonstrates the integration of several advertising
platforms in a single application and the use of advertising banners in
non-standard situations. The application emulates the process of
purchasing movie tickets with the ability to choose between available
cinemas.

The following advertising platforms are used:
- Leadbolt (https://www.leadbolt.com/)
- Adform (https://site.adform.com/)
- Inmobi (https://www.inmobi.com/)

1. Using with Google Maps

   ![Google maps](https://lh6.googleusercontent.com/lSK-CQON4JgMbdCAYoPSJ1BBucnNZbGK82GOeIFcJnxnZ4t9oaOkaRf0UBd313FXwbUs7Ib8jQcUS5BoxOfw3pMjMK0Y9tXKN074fKWI1X3ltis2p9BdhTpsZBDInaJTpSy9aJ4Z)
1. Using as an element of GridView
   -  (Leadbolt)
      
      ![GridView element - Leadbolt](https://lh4.googleusercontent.com/AbFNu95YDJfZ1XFBkQUi4hbQMk9mgSrL93SHqD6uJcBTlK6GWhKi8LC32Np7hPH97EXUQ8-LWpldfVg5YCHkdWQEBp21Zzd_SJOKPDJwYVNn24J5K8Zw_ln31ZgBtahS-WpTVBtf)
   -  (Adform)
      
      ![GridView element - Adform](https://lh6.googleusercontent.com/cQ5RQpPz_RWvtNZJSYDwUePwUr1KgKGq-cP-sK2aX51-5Yy08_1sxpH8C9UkuKswl2-wPpq-0wR59G0MeINT7HNLXyT2PpQZAYt0_c_QLdetVNkEz53XcUAfQYgCncCvATl4Nl-t)
1. Using inside of CoordinatorLayout

   ![Inside CoordinatorLayout](https://lh5.googleusercontent.com/P1wUadzazSQNUwF03Nm-A5EnxmOvP8U_r4RzUtxrPfeHuk1Z3NGSE7OixtQSMQdGQQpuDYljMof_KECtKMijcZ22sXN4g4R5D9lu_bhXcfPcUtnZa3VkKyyEHT5Cq63dv4qgy9cQ)
4. Handling the back button as a separate event (implemented only for
   Leadbolt)

## Add dependencies

#### 1. Leadbolt
- To add an SDK to the project, you need to download the `.jar` file at
  this
  [link](http://d8fj70cz9kmyh.cloudfront.net/dwn/Android/leadbolt_native_android_sdk_v9.0.zip).
- Put the downloaded file in the `libs` folder of your application or
  module.
- Add a dependency to the `build.gradle` file of the application /
  module level:
  ```groovy
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "com.google.android.gms:play-services-base:[actual_version]"
  ```
- More detailed instructions can be found on the
  [link](https://help.leadbolt.com/android-native-integration-guide/).

#### 2. Adform
- Add to the `build.gradle` file of **the project** level repository:
  ```groovy
    allprojects {
        repositories {
            ...
            maven { url "https://github.com/adform/adform-android-sdk/raw/master/releases/" }
        }
    }
  ```
- Add a dependency to the `build.gradle` file of **the application /
  module** level:
  ```groovy
    implementation "com.adform.advertising.sdk:advertising-sdk:[actual_version]@aar"
    implementation "com.google.android.gms:play-services-ads:[actual_version]"
  ```
- More detailed instructions can be found on the
  [link](https://github.com/adform/adform-android-sdk).

#### 3. InMobi

- Integration is described in detail in our article
  [How to Integrate InMobi SDK to Start Monetizing Your Android App](https://agilie.com/en/blog/how-to-integrate-inmobi-sdk-to-start-monetizing-your-android-app)
  and also you can look at the official website
  [Android Guidelines | Getting Started with Android SDK Integration](https://support.inmobi.com/monetize/android-guidelines/)

## Interaction with advertising SDK

An interface `AdProvider` has been created to interact with various SDK's:
```kotlin
interface AdProvider : LifecycleObserver {

   fun init(context: Context)
   
   fun inflateDefaultBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner)
   
   fun loadAdForDefaultBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>)
   
   fun inflateBigBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner)
   
   fun loadAdForBigBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>)

   fun displayFullScreenAd(
       context: Context,
       lifecycleOwner: LifecycleOwner,
       observer: Observer<FullScreenAdEvent>
   )

   fun createGridViewHolder(parent: ViewGroup): AdViewHolder
   
   fun getAdAdapterManager(): AdRecyclerView.AdAdapterManager
   
   fun getAdLayoutManager(): AdRecyclerView.AdLayoutManager
}
```

- SDK initialization
  ```kotlin
  fun init(context: Context)
  ```
- Inflate View, that will contain and display a standard banner ad.  
  It adds the created View to the passed `parent`
  ```kotlin
  fun inflateDefaultBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner)
  ```
  * `LifecycleOwner` is used to process the lifecycle methods of the
    object that caused this method (for example, canceling the loading
    of advertising data if the user returned to the previous screen).  
    Adform also requires calling lifecycle methods for its View.
- Initializes the loading of ad data.  
  Received data is returned to `Observer<AdLoadingEvent>`
  ```kotlin
  fun loadAdForDefaultBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>)
  ```
- Inflate View for a **"Big Banner"**, that will contain and display a
  standard banner ad.  
  It adds the created View to the passed `parent`
  ```kotlin
  fun inflateBigBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner)
  ```
  * `LifecycleOwner` is used to process the lifecycle methods of the
    object that caused this method (for example, canceling the loading
    of advertising data if the user returned to the previous screen).  
    Adform also requires calling lifecycle methods for its View.  
  *  **"Big Banner" is:**
     *    **Adform** is an interactive ad with a given height.
     *    **Leadbolt** is a native banner with a height of 300dp.
     *    **Inmobi** there is the implementation of native advertising
          with given dimensions.
- Initializes the loading of ad data for a **"Big Banner"**.  
  Received data is returned to `Observer<AdLoadingEvent>`
  ```kotlin
  fun loadAdForBigBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>)
  ```
- Initializes the display of full-screen advertising.  
  Full-screen advertising events are returned to
  `Observer<FullScreenAdEvent>` (for example, closing ads, ending
  downloads, etc.)
  ```kotlin
  fun displayFullScreenAd(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<FullScreenAdEvent>
  )
  ```
- Returns an object of the `AdViewHolder` class that extends
  `RecyclerView.ViewHolder` for further use in `RecyclerView.Adapter`.
  ```kotlin
  fun createGridViewHolder(parent: ViewGroup): AdViewHolder
  ```
- Returns an object of the `AdRecyclerView.AdAdapterManager` class that
  manages the list of data displayed in `RecyclerView` with the addition
  of advertisements to this list
  ```kotlin
  fun getAdAdapterManager(): AdRecyclerView.AdAdapterManager
  ```
- Returns an object of the `AdRecyclerView.AdAdapterManager` class,
  which in turn creates a `RecyclerView.LayoutManager`, which will
  manage a specific `AdProvider` implementation for displaying
  advertisements in `RecyclerView`.  
  *For example, for __Adform__, you need to use built-in mechanisms for
  displaying advertisements in `RecyclerView`, but they do not support
  displaying advertisements in custom View like an item in
  `GridLayoutManager`*
  ```kotlin
  fun getAdLayoutManager(): AdRecyclerView.AdLayoutManager
  ```
  
## Payment system

For the payment system was used [Stripe](https://stripe.com/). 
