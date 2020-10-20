package cqrsos

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

object JsonObjectMapper {

  val objectMapper: ObjectMapper = createObjectMapper()

  private def createObjectMapper(): ObjectMapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
    mapper.setSerializationInclusion(Include.NON_NULL)
  }
}
