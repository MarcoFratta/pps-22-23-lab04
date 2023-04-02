package u04lab.code

import u04lab.code.Option.Some

import java.util.Optional
import scala.annotation.tailrec


object ListBuilder:

  import List.*
  import u04lab.polyglot.OptionToOptional

  def apply[A](x: A*): List[A] =
    var l: List[A] = empty
    for i <- x do l = append(l, cons(i, empty))
    l

object OptionToOptional:
  def apply[A](option: Option[A]): scala.Option[A] = option match
    case Some(a) => scala.Option(a)
    case _ => scala.Option.empty

object SameTag:

  import List.*

  import scala.Option.*


  def unapply(l: List[Item]): scala.Option[String] =
    anyMatch(take(flatMap(l)(x => x.tags), 1))(a => allMatch(drop(l, 0))(item =>
      contains(item.tags, a)))

  @main def main(): Unit =
    import ListBuilder.*
    val l: List[Int] = ListBuilder(1, 2, 3, 4)
    println(l)

    println(allMatch(l)(_ > 5)) // false

    val dellXps = Item(33, "Dell XPS 15", "notebook")
    val dellInspiron = Item(34, "Dell Inspiron 13", "notebook")
    val xiaomiMoped = Item(35, "Xiaomi S1", "moped","notebook", "mobility")
    println(unapply(ListBuilder(dellXps, dellInspiron, xiaomiMoped)))

    println(unapply(ListBuilder(dellXps)))

  private def allMatch[A](l: List[A])(f: A => Boolean): Boolean =
    foldLeft(map(l)(x => f(x)))(true)(_ && _)

  @tailrec
  private def anyMatch[A](l: List[A])(f: A => Boolean): scala.Option[A] = l match
    case Cons(h, t) => if f(h) then scala.Some(h) else anyMatch(t)(f)
    case Nil() => scala.None;
