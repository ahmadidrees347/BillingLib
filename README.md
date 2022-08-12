## Using Billing Library in your Android application

### Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### Add this in your build.gradle

```groovy
implementation 'com.github.ahmadidrees347:BillingLib:version'
```

Do not forget to add internet permission in manifest if already not present

```xml

<uses-permission android:name="android.permission.INTERNET" />
```

### Create an object IapConnector :

```kotlin
private var iapConnector: IapConnector? = null
 ```

To check status of billing connection:

```kotlin
private var isBillingClientConnected = false
```

### Initialize IapConnector obj:

```kotlin
iapConnector = IapConnector(
    context = this,
    nonConsumableKeys = listOf("android.test.purchased"),
    consumableKeys = listOf("android.test.purchased"),
    subscriptionKeys = listOf("sku_monthly"),
    enableLogging = true
)
```

Now Add listeners:

```kotlin
iapConnector?.addBillingClientConnectionListener(object : BillingClientConnectionListener {
    override fun onConnected(status: Boolean, billingResponseCode: Int) {
        isBillingClientConnected = status
    }
})
```

For One Time Purchase:

```kotlin
iapConnector?.addPurchaseListener(object : PurchaseServiceListener {
    override fun onEmptyPurchasedList() {
        //No purchase
    }

    override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.SkuDetails>) {
        val currency = iapKeyPrices["your_sub_id"]?.priceCurrencyCode ?: ""
        val price = iapKeyPrices["your_sub_id"]?.price ?: ""
        //"$currency $price"

    }

    override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
        // will be triggered whenever subscription succeeded
    }

    override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
        // will be triggered when user purchase that        
    }
})
```

For Subscription:

```kotlin
iapConnector?.addSubscriptionListener(object : SubscriptionServiceListener {
    override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {

    }

    override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {

    }

    override fun onEmptySubscriptionList() {
    }

    override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.SkuDetails>) {

    }

})
```

For Subscription Call this method:

```kotlin
if (isBillingClientConnected) {
    iapConnector?.purchase(this, "android.test.purchased")
}
```

For Subscription Call this method:

```kotlin
if (isBillingClientConnected) {
    iapConnector?.subscribe(this, "sub_id")
}
```

### License

```
   Copyright (C) 2022 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```