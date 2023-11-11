package cat.kiwi.game.icf.generator

import cat.kiwi.game.icf.generator.model.ICFInfo
import cat.kiwi.game.icf.generator.parser.ICFSerializer
import cat.kiwi.game.icf.generator.utils.Crypt
import com.google.gson.Gson
import java.io.File

/**
 * @email me@kiwi.cat
 * @date 2023/6/26 10:17
 */
class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var jsonFileName: String? = null

            for (i in args.indices) {
                when (args[i]) {
                    "-f" -> {
                        if (i + 1 < args.size) {
                            jsonFileName = args[i + 1]
                        } else {
                            println("Error: -f option requires a file name")
                            return
                        }
                    }
                }
            }
            if (jsonFileName != null) {
                println("converting json to ICF file: $jsonFileName")
                val text = File(jsonFileName).readText()
                val icfInfo = Gson().fromJson<ICFInfo>(text, ICFInfo::class.java)
                val icfData = ICFSerializer.parse(icfInfo)
                println("writing decrypted ICF file: $jsonFileName.icf.dec")
                File("$jsonFileName.icf.dec").writeBytes(icfData.toByteArray())
                val crypt = Crypt.encrypt(icfData.toByteArray())
                println("writing ICF file: $jsonFileName.icf")
                File("$jsonFileName.icf").writeBytes(crypt.toByteArray())
                println("done")
            } else {
                println("Error: No JSON file specified")
                println("usage: -f <json file>")
            }
        }
    }
}