package task4

import org.junit.Assert.{assertEquals, assertFalse, assertThrows, assertTrue}
import org.junit.Test
import u04lab.polyglot.minesweeper.{Logics, LogicsImpl}

import java.util.Objects
import java.util.Optional
import java.util.function.{BiConsumer, BiPredicate}

class LogicsTest:
  val SIZE = 5
  private val MINES_NUMBERS = 5
  private var logics = new LogicsImpl(SIZE, MINES_NUMBERS)



  @Test def testNotWinAtStart(): Unit =
    assertFalse(this.logics.isWin)

  @Test def testCanCreateMines(): Unit = {
    assertEquals(MINES_NUMBERS, countOnAllGrid(this.logics.hasMine, SIZE))
  }

  @Test def testCanHit(): Unit = {
    this.logics = LogicsImpl(SIZE, 0)
    val x = 3
    val y = 3
    assertFalse(this.logics.hit(x, y))
  }

  @Test def testCanHitAMine(): Unit = {
    val size = 3
    this.logics = LogicsImpl(size, size * size)
    assertTrue(this.logics.hit(size - 1, size - 1))
  }

  @Test def testCanFlagAndRemoveFlag(): Unit = {
    val row = 3
    val column = 2
    assertFalse(this.logics.hasFlag(row, column))
    this.logics.flag(row, column)
    assertTrue(this.logics.hasFlag(row, column))
    this.logics.flag(row, column)
    assertFalse(this.logics.hasFlag(row, column))
  }

  @Test def testSelected(): Unit = {
    this.logics = new LogicsImpl(3, 3 * 3)
    val row = 1
    val column = 1
    assertEquals(9, countOnAllGrid((x,y) => logics.hasMine(x,y),3))
    assertEquals(Optional.empty, this.logics.getAdjacentMines(row, column))
    this.logics.hit(row, column)
    assertEquals(Optional.of(8), this.logics.getAdjacentMines(row, column))
  }

  @Test def testRecursiveSelection(): Unit = {
    this.logics = new LogicsImpl(SIZE, 0)
    this.logics.hit(0, 0)
    assertEquals(0, countOnAllGrid((x: Int, y: Int) =>
      this.logics.getAdjacentMines(x, y) == Optional.empty, SIZE))
  }

  @Test def testCanWinAfterClickingAll(): Unit = {
    val size = 5
    this.logics = new LogicsImpl(size, 1)
    val (m_X,m_Y) = find((x, y) => this.logics.hasMine(x, y), size)
    foreachCellDo((i, j) => (i,j) match
      case (x,y) if x == m_X && y == m_Y =>
      case (x,y) => logics.hit(x,y)
      , size)
    assertTrue(this.logics.isWin)
  }

  @Test def testNotWinAfterAMine(): Unit = {
    val (x,y) = find((x, y) => this.logics.hasMine(x, y), SIZE)
    this.logics.hit(x, y)
    assertFalse(this.logics.isWin)
  }

def countOnAllGrid(f:(Int,Int) => Boolean, size: Int) =
  var count = 0
  for (i <- 0 until size) {
    for (j <- 0 until size) {
      if (f(i, j)) count += 1
    }
  }
  count

def find(f:(Int, Int) => Boolean, size: Int): (Int,Int) = {
  for (i <- 0 until size) {
    for (j <- 0 until size) {
      if (f(i, j))  return (i, j)
    }
  }
  null
}

def foreachCellDo(action: (Int,Int) => Unit, size: Int): Unit = {
  for (i <- 0 until size) {
    for (j <- 0 until size) {
      action(i, j)
    }
  }
}

