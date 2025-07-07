package com.omnivoiceai.neuromirror.utils
import android.content.Context
import android.util.Base64
import org.bouncycastle.asn1.ASN1InputStream
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.DERBitString
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.encodings.OAEPEncoding
import org.bouncycastle.crypto.engines.RSAEngine
import org.bouncycastle.crypto.params.RSAKeyParameters
import org.json.JSONObject
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/// Load the RSA public key from a file
fun loadPublicKey(context: Context, fileName: String): RSAKeyParameters {
    val pem = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val cleaned = pem
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\n", "")
    val decoded = Base64.decode(cleaned, Base64.DEFAULT)

    val asn1 = ASN1InputStream(decoded).use { it.readObject() as ASN1Sequence }
    val bitString = asn1.getObjectAt(1) as DERBitString
    val keySeq = ASN1InputStream(bitString.bytes).use { it.readObject() as ASN1Sequence }

    val modulus = (keySeq.getObjectAt(0) as ASN1Integer).positiveValue
    val exponent = (keySeq.getObjectAt(1) as ASN1Integer).positiveValue

    return RSAKeyParameters(false, modulus, exponent)
}

fun generateAesKeyAndIv(): Pair<SecretKey, IvParameterSpec> {
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(256)
    val secretKey = keyGen.generateKey()

    val ivBytes = ByteArray(16)
    SecureRandom().nextBytes(ivBytes)
    val iv = IvParameterSpec(ivBytes)

    return Pair(secretKey, iv)
}

fun aesEncrypt(message: String, key: SecretKey, iv: IvParameterSpec): ByteArray {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    return cipher.doFinal(message.toByteArray(Charsets.UTF_8))
}

fun rsaEncryptAesKey(aesKey: SecretKey, publicKey: RSAKeyParameters): ByteArray {
    val oaep = OAEPEncoding(
        RSAEngine(),
        SHA256Digest(),      // Digest
        SHA256Digest(),      // MGF1 Digest
        ByteArray(0)         // Default label
    )
    oaep.init(true, publicKey)
    return oaep.processBlock(aesKey.encoded, 0, aesKey.encoded.size)
}

fun encryptMessage(context: Context, message: String, publicKeyPath: String): String {
    val rsaKey = loadPublicKey(context, publicKeyPath)
    val (aesKey, iv) = generateAesKeyAndIv()
    val cipherText = aesEncrypt(message, aesKey, iv)
    val encryptedAesKey = rsaEncryptAesKey(aesKey, rsaKey)

    val payload = JSONObject()
    payload.put("encryptedAesKey", Base64.encodeToString(encryptedAesKey, Base64.NO_WRAP))
    payload.put("iv", Base64.encodeToString(iv.iv, Base64.NO_WRAP))
    payload.put("ciphertext", Base64.encodeToString(cipherText, Base64.NO_WRAP))

    return payload.toString()
}
