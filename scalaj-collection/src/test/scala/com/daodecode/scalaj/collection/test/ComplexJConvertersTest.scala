package com.daodecode.scalaj.collection.test

import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Seq => MSeq, Set => MSet}

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

class ComplexJConvertersTest extends WordSpec with Matchers {

  "AsJava collection converters" should {

    "convert nested collections and primitives" in {

      List(List(List('a'))).deepAsJava: JList[JList[JList[JChar]]]

      List(MSeq(MBuffer('a'))).deepAsJava: JList[JList[JList[JChar]]]

      List(Map('a' -> 1)).deepAsJava: JList[JMap[JChar, JInt]]

      List(MMap('a' -> 1)).deepAsJava: JList[JMap[JChar, JInt]]

      List(Set(Array(12D))).deepAsJava: JList[JSet[Array[JDouble]]]

      List(Array(Set(12D))).deepAsJava: JList[Array[JSet[JDouble]]]

      Array(List(Set(12L))).deepAsJava: Array[JList[JSet[JLong]]]

      Array(Set(List(false))).deepAsJava: Array[JSet[JList[JBoolean]]]

      Set(List(Array('a'))).deepAsJava: JSet[JList[Array[JChar]]]

      Set(Map(1 -> 'a')).deepAsJava: JSet[JMap[JInt, JChar]]

      Map("set1" -> Set(23, 24)).deepAsJava: JMap[String, JSet[JInt]]

      Map("set1" -> MSet(23, 24)).deepAsJava: JMap[String, JSet[JInt]]

      Map("set1" -> Map(23 -> 'a', 24 -> 'b')).deepAsJava: JMap[String, JMap[JInt, JChar]]

      MMap("set1" -> MMap(23 -> 'a', 24 -> 'b')).deepAsJava: JMap[String, JMap[JInt, JChar]]
    }

    "keep nested mutable buffers as mutable" in {
      val sm: MSet[MBuffer[Int]] = MSet(MBuffer(1, 2))
      val jm = sm.deepAsJava

      val jList: JList[JInt] = jm.iterator().next()

      jList.add(3)
      sm should be(MSet(MBuffer(1, 2, 3)))
    }

    "keep nested mutable sets as mutable" in {
      val sm: Set[MSet[Int]] = Set(MSet(1, 2))
      val jm = sm.deepAsJava

      val jSet: JSet[JInt] = jm.iterator().next()

      jSet.add(3)
      sm should be(Set(MSet(1, 2, 3)))
    }

    "keep nested mutable maps as mutable" in {
      val sm: MMap[Int, MMap[Long, String]] = MMap(1 -> MMap(2L -> "2"))
      val jm = sm.deepAsJava

      val jMap: JMap[JLong, String] = jm.get(1)

      jMap.put(3L, "3")
      sm should be(MMap(1 -> MMap(2L -> "2", 3L -> "3")))
    }
  }
}

