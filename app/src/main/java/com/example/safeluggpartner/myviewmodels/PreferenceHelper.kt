import android.content.Context

object PreferenceHelper {
    private const val PREF_NAME = "safe_lugg_prefs"
    private const val KEY_DETAILS_SUBMITTED = "details_submitted"

    fun setDetailsSubmitted(context: Context, isSubmitted: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DETAILS_SUBMITTED, isSubmitted).apply()
    }

    fun isDetailsSubmitted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DETAILS_SUBMITTED, false)
    }


    private const val KEY_EMAIL = "vendor_email"

    fun saveVendorEmail(context: Context, email: String){
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_EMAIL, email).apply()
    }
    fun getVendorEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, null)
    }
}
