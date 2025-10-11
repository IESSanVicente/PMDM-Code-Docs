package es.javiercarrasco.documentationt9_1

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Guardar un archivo de texto en memoria interna
fun saveToFile(context: Context, fileName: String, contenido: String) {
    context.openFileOutput(fileName, Context.MODE_APPEND).use { output ->
        output.write(contenido.toByteArray())
    }
}

// Leer un archivo de texto desde memoria interna
fun readFromFile(context: Context, fileName: String): String {
    return try {
        context.openFileInput(fileName).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        "Error al leer el archivo: ${e.message}"
    }
}
// ----------------------------------------------------------------

// Guardar preferencia
fun savePreference(context: Context, key: String, value: String) {
    val prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
    prefs.edit { putString(key, value) }
}

// Leer preferencia
fun readPreference(context: Context, key: String): String? {
    val prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
    // Leer preferencia
    return prefs.getString(key, "Invitado")
}
// ----------------------------------------------------------------

// DataStore - Guardar y leer preferencia
val Context.dataStore by preferencesDataStore("ajustes")
val TEMA_OSCURO = booleanPreferencesKey("tema_oscuro")

suspend fun saveThemeMode(context: Context, oscuro: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[TEMA_OSCURO] = oscuro
    }
}

fun readThemeMode(context: Context): Flow<Boolean> {
    val temaFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[TEMA_OSCURO] ?: false
    }
    return temaFlow
}
// ----------------------------------------------------------------

/*

// Almacenamiento seguro de preferencias con Android Keystore + AES-GCM
// 1. Generar/obtener clave AES en Keystore (una vez)
private const val KEY_ALIAS = "app_prefs_aes"

fun getOrCreateAesKey(): SecretKey {
    val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    (ks.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey?.let { return it }

    val spec = KeyGenParameterSpec.Builder(
        KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setUserAuthenticationRequired(false) // Ajusta si quieres exigir biometría
        .build()

    return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        .apply { init(spec) }
        .generateKey()
}

// 2. Cifrar y descifrar
// AES-GCM requiere un IV (vector de inicialización) único por cada cifrado
// Se suele guardar junto al texto cifrado (no es secreto)
// Se sobrecarga equals y hashCode para comparar contenido de ByteArray.
data class Ciphertext(val iv: ByteArray, val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ciphertext

        if (!iv.contentEquals(other.iv)) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = iv.contentHashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}

fun encrypt(plain: ByteArray, key: SecretKey): Ciphertext {
    val cipher = getInstance("AES/GCM/NoPadding")
    cipher.init(ENCRYPT_MODE, key)
    val iv = cipher.iv
    val enc = cipher.doFinal(plain)
    return Ciphertext(iv, enc)
}

fun decrypt(ct: Ciphertext, key: SecretKey): ByteArray {
    val cipher = getInstance("AES/GCM/NoPadding")
    val spec = GCMParameterSpec(128, ct.iv)
    cipher.init(DECRYPT_MODE, key, spec)
    return cipher.doFinal(ct.bytes)
}

// 3. Guardar el valor cifrado (ej.: en DataStore Preferences)
suspend fun saveSecureToken(context: Context, token: String) {
    val key = getOrCreateAesKey()
    val ct = encrypt(token.encodeToByteArray(), key)
    val ivB64 = Base64.encodeToString(ct.iv, Base64.NO_WRAP)
    val dataB64 = Base64.encodeToString(ct.bytes, Base64.NO_WRAP)

    val TOKEN_IV = stringPreferencesKey("token_iv")
    val TOKEN_CT = stringPreferencesKey("token_ct")

    context.dataStore.edit {
        it[TOKEN_IV] = ivB64
        it[TOKEN_CT] = dataB64
    }
}

suspend fun readSecureToken(context: Context): String? {
    val prefs = context.dataStore.data.first()
    val iv = prefs[stringPreferencesKey("token_iv")] ?: return null
    val ct = prefs[stringPreferencesKey("token_ct")] ?: return null

    val key = getOrCreateAesKey()
    val bytes = decrypt(
        Ciphertext(Base64.decode(iv, Base64.NO_WRAP), Base64.decode(ct, Base64.NO_WRAP)),
        key
    )
    return bytes.decodeToString()
}

*/

