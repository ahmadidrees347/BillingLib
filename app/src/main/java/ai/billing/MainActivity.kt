package ai.billing

import ai.lib.billing.BillingClientConnectionListener
import ai.lib.billing.DataWrappers
import ai.lib.billing.IapConnector
import ai.lib.billing.PurchaseServiceListener
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var iapConnector: IapConnector? = null
    private var isBillingClientConnected = false
    val inAppList = listOf("android.test.purchased")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBilling()

        findViewById<Button>(R.id.btn).setOnClickListener {
            if (isBillingClientConnected) {
                iapConnector?.purchase(this@MainActivity, inAppList[0])
            }
        }
    }

    private fun initBilling() {
        iapConnector = IapConnector(
            context = this,
            consumableKeys = inAppList,
            enableLogging = true
        )
        iapConnector?.addBillingClientConnectionListener(object : BillingClientConnectionListener {
            override fun onConnected(status: Boolean, billingResponseCode: Int) {
                Log.e(
                    "tagV4*",
                    "status: $status & responseCode: $billingResponseCode"
                )
                isBillingClientConnected = status
            }
        })

        iapConnector?.addPurchaseListener(object : PurchaseServiceListener {
            override fun onEmptyPurchasedList() {
                Log.e("tagV4*", "onEmptyPurchasedList")
            }

            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.SkuDetails>) {
                // list of available products will be received here, so you can update UI with prices if needed
                Log.e("tagV4*", "iapKeyPrices $iapKeyPrices")
                val prices = HashMap<String, String>()
                inAppList.forEach {
                    val currency = iapKeyPrices[it]?.priceCurrencyCode ?: ""
                    val price = iapKeyPrices[it]?.price ?: ""
                    prices[it] = "$currency $price"
                }
            }

            override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered whenever subscription succeeded
                when (purchaseInfo.sku) {
                    inAppList[0] -> {
                        Log.e("tagV4*", "onSubscriptionRestored $purchaseInfo")
                    }
                }
            }

            override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered upon fetching owned subscription upon initialization
                Log.e("tagV4*", "onSubscriptionRestored $purchaseInfo")
                when (purchaseInfo.sku) {
                    inAppList[0] -> {
                        Log.e("tagV4*", "purchaseState ${purchaseInfo.purchaseState}")

                    }
                }
            }
        })
    }
}