package pl.droidsonroids.testing.mockwebserver

import org.apache.commons.text.translate.AggregateTranslator
import org.apache.commons.text.translate.EntityArrays
import org.apache.commons.text.translate.JavaUnicodeEscaper
import org.apache.commons.text.translate.LookupTranslator
import org.yaml.snakeyaml.Yaml
  
internal class YamlResourcesParser : ResourcesParser {
    private val escaper = AggregateTranslator(
        LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE),
        JavaUnicodeEscaper.outsideOf(32, 0x7f)
    )
      private val parser = Yaml()
  
      override fun parseFrom(fileName: String): Fixture {
          val path = "fixtures/$fileName.yaml"
          val content = path.getResourceAsString()
        val escapedContent = escaper.translate(content)
        val result = parser.loadAs(escapedContent, Fixture::class.java)

        if (!result.hasJsonBody()) {
            val bodyPath = "fixtures/${result.body}"
            result.body = bodyPath.getResourceAsString()
        }
        return result
    }
}
