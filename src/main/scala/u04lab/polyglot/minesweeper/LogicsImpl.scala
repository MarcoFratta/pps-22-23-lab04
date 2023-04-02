package u04lab.polyglot.minesweeper

import u04lab.code.List.length
import u04lab.code.{Option, *}
import u04lab.polyglot.minesweeper.Logics

import java.util.Optional
import scala.annotation.tailrec

case class Cell(x: Int, y: Int):

  import Stream.*

  def adjacent: List[Cell] =
    List.append(List.append(takeRow(x - 1, y - 1)(3)(_ + 1),
      List.remove(takeRow(x, y - 1)(3)(_ + 1))(_.y == y)),
      takeRow(x + 1, y - 1)(3)(_ + 1))

  private def takeRow(fixedX: Int, startY: Int)(limit: Int)(f: Int => Int): List[Cell] =
    toList(map(take(iterate(startY)(f))(limit))(y => Cell(fixedX, y)))

class Grid(val w: Int, val h: Int,
           var cells: List[Cell] = ListBuilder()):

  override def toString: String = this.cells.toString

  def apply(x: Int, y: Int): Option[Cell] =
    List.find(cells)(c => c == Cell(x, y))

  def apply(cell: Cell): Option[Cell] =
    List.find(cells)(c => c == cell)

  def +(cell: Cell): Grid = if (isInside(cell) && !List.contains(this.cells, cell))
    Grid(w, h, List.append(ListBuilder(cell), this.cells)) else this

  def -(cell: Cell): Grid = if (isInside(cell))
    Grid(w, h, List.remove(this.cells)(c => c == cell)) else this

  def isInside(cell: Cell): Boolean = cell match
    case Cell(x, y) if x < 0 || y < 0 => false
    case Cell(x, y) => x < w && y < h

  def -(x: Grid): Grid = Grid(w, h,
    List.filter(this.cells)(c => !List.contains(x.cells, c)))

object Options:

  def toBoolean[A](option: Option[A]): Boolean = option match
    case Option.Some(_) => true
    case _ => false

object Length:
  def unapply(grid: Grid): scala.Option[Int] =
    scala.Option(length(grid.cells))

object Grid:
  import scala.util.Random
  private val r = Random(4)

  @tailrec
  def randomGrid(grid: Grid)(boundX: Int, boundY: Int)(n: Int): Grid = n match
    case 0 => grid
    case n => (r.nextInt(boundX), r.nextInt(boundY)) match
      case (x, y) => grid(x, y) match
        case Option.Some(_) => randomGrid(grid)(boundX, boundY)(n)
        case _ => randomGrid(grid + Cell(x, y))(boundX, boundY)(n - 1)

class LogicsImpl(val size: Int, val mines: Int) extends Logics:

  private val minedGrid: Grid = Grid.randomGrid(Grid(size, size))(size, size)(mines)
  private var clickedGrid: Grid = Grid(size, size)
  private var flagGrid: Grid = Grid(size, size)

  override def isWin: Boolean = this.clickedGrid - this.minedGrid match
      case Length(n) if n == (size * size - mines) => true
      case _ => false

  override def hasMine(row: Int, column: Int): Boolean = Options.toBoolean(this.minedGrid(row, column))

  override def hit(row: Int, column: Int): Boolean =
    this.clickedGrid = this.clickedGrid + Cell(row, column)
    recursiveHit(row, column)
    Options.toBoolean(this.minedGrid(row, column))

  private def recursiveHit(row: Int, column: Int): Unit =
    this.clickedGrid(row, column) match
      case Option.Some(a) => this.getOptionAdjacentMines(row, column) match
        case Option.Some(c) if c == 0 => forEach(List.filter(a.adjacent)(x =>
          !Options.toBoolean(this.clickedGrid(x))))(c => hit(c.x, c.y))
        case _ =>
      case _ =>

  override def flag(row: Int, column: Int): Unit = this.flagGrid(row, column) match
    case Option.Some(_) => this.flagGrid = this.flagGrid - Cell(row, column)
    case _ => this.flagGrid = this.flagGrid + Cell(row, column)

  override def hasFlag(row: Int, column: Int): Boolean = Options.toBoolean(this.flagGrid(row, column))

  override def getAdjacentMines(row: Int, column: Int): Optional[Integer] =
    getOptionAdjacentMines(row, column) match
      case Option.Some(a) => Optional.of(a)
      case _ => Optional.empty()

  private def getOptionAdjacentMines(row: Int, column: Int): Option[Integer] = {
    this.clickedGrid(row, column) match
      case Option.Some(a) => Option.Some(List.foldLeft(List.map(a.adjacent)(x => this.minedGrid(x) match
          case Option.Some(_) => 1
          case Option.None() => 0))(0)(_ + _))
      case _ => Option.None()
  }

  @tailrec
  private def forEach[A](l: List[A])(f: A => Unit): Unit = l match
    case List.Cons(head, tail) => f(head)
      forEach(tail)(f)
    case _ =>



