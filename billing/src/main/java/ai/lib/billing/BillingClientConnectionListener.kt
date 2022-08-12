package ai.lib.billing

interface BillingClientConnectionListener {
    fun onConnected(status: Boolean, billingResponseCode: Int)
}