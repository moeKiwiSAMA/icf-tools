package cat.kiwi.game.icf.generator.ext

import cat.kiwi.game.icf.generator.model.ContentType

/**
 * @email me@kiwi.cat
 * @date 2023/6/26 11:06
 */
val String.versionByteList: List<Byte>
    get() {
        val version = this.split(".")
        if (version.size != 3) {
            throw IllegalArgumentException("Version format error")
        }
        val versionByteArray = arrayListOf<Byte>()
        val major = version[0].toInt()
        val minor = version[1].toInt()
        val build = version[2].toInt()
        versionByteArray.add(build.toByte())
        versionByteArray.add(minor.toByte())
        versionByteArray.addAll(major.reversedDualBytes)
        return versionByteArray
    }
val String.optVersionByteList: List<Byte>
    get() {
        if (this.length != 4) {
            throw IllegalArgumentException("OPTIOIN version format error")
        }
        return this.map { it.code.toByte() }
    }
val Int.reversedDualBytes: List<Byte>
    get() {
        val dualByte = arrayListOf<Byte>()
        dualByte.add(this.toByte())
        dualByte.add((this shr 8).toByte())
        return dualByte
    }

val String.buildDateByteList: ArrayList<Byte>
    get() {
        //20230210182916
        // E7 07 02 0A 12 1D 10
        if (this.length != 14) {
            throw IllegalArgumentException("Build date format error")
        }
        if (this.contains(Regex("[^0-9]"))) {
            throw IllegalArgumentException("Build date format error")
        }
        val buildDateByteArray = arrayListOf<Byte>()
        val year = this.substring(0, 4).toInt()
        val month = this.substring(4, 6).toInt()
        val day = this.substring(6, 8).toInt()
        val hour = this.substring(8, 10).toInt()
        val minute = this.substring(10, 12).toInt()
        val second = this.substring(12, 14).toInt()
        buildDateByteArray.addAll(year.reversedDualBytes)
        buildDateByteArray.add(month.toByte())
        buildDateByteArray.add(day.toByte())
        buildDateByteArray.add(hour.toByte())
        buildDateByteArray.add(minute.toByte())
        buildDateByteArray.add(second.toByte())
        return buildDateByteArray
    }
val String.platformVersionByteList: ArrayList<Byte>
    get() {
        val version = this.split(".")
        // 0071.55.01
        // 01 37 47 00
        if (version.size != 3) {
            throw IllegalArgumentException("Platform version format error")
        }

        val platformVersionByteArray = arrayListOf<Byte>()
        val major = version[0].toInt()
        val minor = version[1].toInt()
        val build = version[2].toInt()
        platformVersionByteArray.add(build.toByte())
        platformVersionByteArray.add(minor.toByte())
        platformVersionByteArray.addAll(major.reversedDualBytes)
        return platformVersionByteArray
    }

val Collection<Byte>.readableText: String
    get() {
        return this.joinToString(separator = "") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }
    }

@OptIn(ExperimentalUnsignedTypes::class)
val Collection<Byte>.crc32: UInt
    get() {
        var crc = 0xFFFFFFFFu
        this.forEach { byte ->
            var byteValue = byte.toInt() and 0xFF  // Convert signed byte to unsigned
            var tableIndex = (crc xor byteValue.toUInt()) and 0xFFu
            crc = (crc shr 8) xor CRC32_TABLE[tableIndex.toInt()]
        }
        return crc.inv()
    }

@OptIn(ExperimentalUnsignedTypes::class)
val CRC32_TABLE = UIntArray(256) { i ->
    var value = i.toUInt()
    repeat(8) {
        value = if (value and 1u != 0u) (value shr 1) xor 0xEDB88320u
        else value shr 1
    }
    value
}
val UInt.reversedQuadBytes: List<Byte>
    get() {
        return arrayListOf(
            ((this shr 24) and 0xFFu).toByte(),
            ((this shr 16) and 0xFFu).toByte(),
            ((this shr 8) and 0xFFu).toByte(),
            (this and 0xFFu).toByte()
        ).reversed()
    }

val Int.reversedQuadBytes: List<Byte>
    get() {
        return arrayListOf(
            ((this shr 24) and 0xFF).toByte(),
            ((this shr 16) and 0xFF).toByte(),
            ((this shr 8) and 0xFF).toByte(),
            (this and 0xFF).toByte()
        ).reversed()
    }

val Long.reversedOctaBytes: List<Byte>
    get() {
        return arrayListOf(
            ((this shr 56) and 0xFF).toByte(),
            ((this shr 48) and 0xFF).toByte(),
            ((this shr 40) and 0xFF).toByte(),
            ((this shr 32) and 0xFF).toByte(),
            ((this shr 24) and 0xFF).toByte(),
            ((this shr 16) and 0xFF).toByte(),
            ((this shr 8) and 0xFF).toByte(),
            (this and 0xFF).toByte()
        ).reversed()
    }

fun List<Byte>.toInt(): Int {
    var result = 0
    for (i in this.indices) {
        result = result shl 8
        result = result or (this[i].toInt() and 0xFF)
    }
    return result
}

val List<Byte>.contentType: ContentType
    get() {
        return if (this[0].toInt() == 0 && this[1].toInt() == 0) {
            ContentType.PLATFORM
        } else if (this[0].toInt() == 1 && this[1].toInt() == 0) {
            ContentType.APP
        } else if (this[0].toInt() == 1) {
            ContentType.PATCH
        } else if (this[0].toInt() == 2) {
            ContentType.OPTION
        } else {
            throw Exception("Unknown Content Type")
        }
    }

fun List<Byte>.convertToVersion(contentType: ContentType): String {
    return when (contentType) {
        ContentType.PLATFORM -> {
            val major = this[2].toInt().let { String.format("%02d", it) }
            val minor = this[1].toInt().let { String.format("%02d", it) }
            val build = this[0].toInt().let { String.format("%02d", it) }
            "$major.$minor.$build"
        }

        in listOf(ContentType.APP, ContentType.PATCH) -> {
            val major = this[2].toInt().toString()
            val minor = this[1].toInt().let { String.format("%02d", it) }
            val build = this[0].toInt().let { String.format("%02d", it) }
            "$major.$minor.$build"
        }

        ContentType.OPTION -> {
            this.joinToString("") { it.toInt().toChar().toString() }
        }

        else -> "UNKNOWN"
    }
}

val List<Byte>.buildDate: String
    get() {
        val year = this.slice(0..1).reversed().toInt()
        val month = this[2].toInt().let { String.format("%02d", it) }
        val day = this[3].toInt().let { String.format("%02d", it) }
        val hour = this[4].toInt().let { String.format("%02d", it) }
        val minute = this[5].toInt().let { String.format("%02d", it) }
        val second = this[6].toInt().let { String.format("%02d", it) }
        return "$year$month$day$hour$minute$second"
    }

val List<Byte>.requiredPlatformVersion:String
    get() {
        return if (this[3].toInt() == 0) {
            this.slice(0..2).reversed().joinToString(".") { String.format("%02d",it) }
        } else  {
            this.slice(0..3).reversed().joinToString(".") { String.format("%02d",it) }
        }
    }