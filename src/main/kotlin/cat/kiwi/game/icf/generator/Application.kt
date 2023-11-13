package cat.kiwi.game.icf.generator

import cat.kiwi.game.icf.generator.model.ICFInfo
import cat.kiwi.game.icf.generator.parser.ICFDeserializer
import cat.kiwi.game.icf.generator.parser.ICFSerializer
import cat.kiwi.game.icf.generator.utils.Crypt
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import kotlin.system.exitProcess

/**
 * @email me@kiwi.cat
 * @date 2023/6/26 10:17
 */
class Application {
    companion object {
        val helpMsg = """
                    usage:
                    
                        j2i <fileName>  - convert json to icf
                        i2j <fileName>  - convert icf to json
                """.trimIndent()
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 2) {
                println(helpMsg)
                exitProcess(1)
            }
            when (args[0]) {
                "j2i" -> {
                    jsonToIcf(args[1])
                }

                "i2j" -> {
                    icfToJson(args[1])
                }
            }

        }

        fun jsonToIcf(jsonFileName: String) {
            println("converting Json to ICF file: $jsonFileName")
            val text = File(jsonFileName).readText()
            val icfInfo = Gson().fromJson<ICFInfo>(text, ICFInfo::class.java)
            val icfData = ICFSerializer.parse(icfInfo)
            println("writing decrypted ICF file: $jsonFileName.icf.dec")
            File("$jsonFileName.icf.dec").writeBytes(icfData.toByteArray())
            val crypt = Crypt.encrypt(icfData.toByteArray())
            println("writing ICF file: $jsonFileName.icf")
            File("$jsonFileName.icf").writeBytes(crypt)
            println("done")
        }

        fun icfToJson(icfFileName: String) {
            println("converting ICF to Json file: $icfFileName")
            val content = File(icfFileName).readBytes()
            println("writing decrypted ICF file: $icfFileName.icf.dec")
            val decrypted = Crypt.decrypt(content)
            File("$icfFileName.dec").writeBytes(decrypted)
            println("writing ICF file: $icfFileName.json")
            File("$icfFileName.json").writeText(
                ICFDeserializer().parse(decrypted).let {
                    GsonBuilder().setPrettyPrinting().create().toJson(it)
                }
            )
            println("done")
        }
    }
}