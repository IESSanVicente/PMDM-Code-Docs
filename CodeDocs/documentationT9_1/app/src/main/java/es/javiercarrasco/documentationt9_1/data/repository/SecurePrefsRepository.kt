package es.javiercarrasco.documentationt9_1.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.util.Base64
import es.javiercarrasco.documentationt9_1.data.security.CryptoManager

private val Context.dataStore by preferencesDataStore(name = "secure_prefs")

class SecurePrefsRepository(private val context: Context) {

    // Claves para el DataStore.
    private val KEY_IV = stringPreferencesKey("token_iv_b64")
    private val KEY_CT = stringPreferencesKey("token_ct_b64")

    // Guarda un token cifrado en DataStore (iv + ciphertext, ambos en Base64).
    suspend fun saveToken(token: String) {
        val ct = CryptoManager.encrypt(token.encodeToByteArray())
        val ivB64 = Base64.encodeToString(ct.iv, Base64.NO_WRAP)
        val dataB64 = Base64.encodeToString(ct.bytes, Base64.NO_WRAP)

        // Almacena ambos en DataStore.
        context.dataStore.edit { prefs ->
            prefs[KEY_IV] = ivB64
            prefs[KEY_CT] = dataB64
        }
    }

    // Flujo que expone el token descifrado o null si no existe.
    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs: Preferences ->
        val ivB64 = prefs[KEY_IV]
        val ctB64 = prefs[KEY_CT]
        if (ivB64 == null || ctB64 == null) return@map null

        val iv = Base64.decode(ivB64, Base64.NO_WRAP)
        val data = Base64.decode(ctB64, Base64.NO_WRAP)
        CryptoManager.decrypt(CryptoManager.Ciphertext(iv, data)).decodeToString()
    }

    // Flujo que expone el token cifrado en formato "iv|ciphertext" o null si no existe.
    val encryptedFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        val iv = prefs[KEY_IV]
        val ct = prefs[KEY_CT]
        if (iv != null && ct != null) "$iv|$ct" else null
    }

    // Elimina el token almacenado.
    suspend fun clearToken() {
        context.dataStore.edit { it.remove(KEY_IV); it.remove(KEY_CT) }
    }
}
