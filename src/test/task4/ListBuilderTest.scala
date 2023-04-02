package task4


import org.junit.Test
import org.junit.Assert.*
import u04lab.code.List.append
import u04lab.code.{Item, List, ListBuilder, SameTag}

class ListBuilderTest:
  import ListBuilder.*
  import List.*
  @Test def testListBuilder(): Unit =
    val l = ListBuilder(1,4,5)
    assertEquals(Cons(1,Cons(4,Cons(5, Nil()))),l)

  @Test def testEmptyListBuilder(): Unit =
    val l = ListBuilder()
    assertEquals(Nil(), l)

class CommonTagTest:


  val dellXps: Item = Item(33, "Dell XPS 15", ListBuilder("Notebook"))
  val dellInspiron: Item = Item(34, "Dell Inspiron 13", ListBuilder("Notebook", "battery"))
  val xiaomiMoped: Item = Item(35, "Xiaomi S1", ListBuilder("Notebook", "Moped", "Mobility"))
  val list: List[Item] = ListBuilder(dellXps, dellInspiron, xiaomiMoped)
  @Test def testCommonTag(): Unit =
    assertEquals(Some("Notebook"), SameTag.unapply(list))

  @Test def testEmptyListCommonTag(): Unit =
    assertEquals(None, SameTag.unapply(ListBuilder()))


  @Test def testNoCommonTag(): Unit =
    val item = Item(36, "Xiaomi pro", ListBuilder("broken"))
    assertEquals(None, SameTag.unapply(append(list, ListBuilder(item))))

  @Test def matchCommonTag(): Unit =
    list match
      case SameTag(t) => assertEquals("Notebook", t)
      case _ => fail()




