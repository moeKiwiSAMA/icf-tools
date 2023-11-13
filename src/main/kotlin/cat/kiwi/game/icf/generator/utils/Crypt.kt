package cat.kiwi.game.icf.generator.utils

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * @email me@kiwi.cat
 * @date 2023/6/26 12:40
 */

object Crypt {
    // Bring your own key please
    private val aesKey =
        arrayListOf(0).map { it.toByte() }
            .toByteArray()

    private val aesIv =
        arrayListOf(0).map { it.toByte() }
            .toByteArray()


    fun encrypt(from: ByteArray): ByteArray {
        var num = 4096
        val num2 = from.size
        val array = ByteArray(num2)

        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val keySpec = SecretKeySpec(aesKey, "AES")
        val ivSpec = IvParameterSpec(aesIv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        var i = 0
        while (i < num2) {
            val num3 = i
            val num4 = num2 - num3
            if (num4 < num) {
                num = num4
            }
            val array3 = ByteArray(num)
            System.arraycopy(from, num3, array3, 0, array3.size)

            val value = ByteBuffer.wrap(array3.sliceArray(0..7)).order(ByteOrder.LITTLE_ENDIAN).long xor num3.toLong()
            val value2 = ByteBuffer.wrap(array3.sliceArray(8..15)).order(ByteOrder.LITTLE_ENDIAN).long xor num3.toLong()

            val array4 = ByteArray(num4)
            System.arraycopy(
                ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array(),
                0,
                array4,
                0,
                8
            )
            System.arraycopy(
                ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value2).array(),
                0,
                array4,
                8,
                8
            )
            System.arraycopy(array3, 16, array4, 16, num4 - 16)
            val result = cipher.doFinal(array4)
            System.arraycopy(result, 0, array, i, result.size)
            i += num
        }
        return array
    }

    fun decrypt(from: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val keySpec = SecretKeySpec(aesKey, "AES")
        val ivSpec = IvParameterSpec(aesIv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(from)
    }
}