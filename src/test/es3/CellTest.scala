package task4


object CellTest {
  private val X = 3
  private val Y = 3
}
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.junit.Test
import task4.CellTest.{X, Y}
import u04lab.polyglot.minesweeper.{Cell, Grid}
import u04lab.code.{List, ListBuilder, Option}
class CellTest:
  import Cell.*
  private var cell: Cell = Cell(X,Y)


  @Test def testGetXPosition(): Unit = {
    assertEquals(CellTest.X, this.cell.x)
  }

  @Test def testGetYPosition(): Unit = {
    assertEquals(CellTest.Y, this.cell.y)
  }

  @Test def testAdjacentCells(): Unit =
    assertTrue(List.contains(this.cell.adjacent,Cell(X-1,Y-1)))


  @Test def testAllAdjacentCells(): Unit =
    assertEquals(ListBuilder(Cell(X-1,Y-1),
      Cell(X-1,Y),Cell(X-1,Y + 1),Cell(X,Y-1), Cell(X,Y + 1), Cell(X + 1, Y - 1),
      Cell(X + 1, Y), Cell(X + 1,Y + 1)), this.cell.adjacent)

class testGrid:

  @Test def testOutsideGrid(): Unit =
    val grid = Grid(4,4)
    assertFalse(grid.isInside(Cell(-1,-1)))
    assertFalse(grid.isInside(Cell(4,4)))
    assertFalse(grid.isInside(Cell(5,8)))
  @Test def testInsideGrid(): Unit =
    val grid = Grid(4, 4)
    assertTrue(grid.isInside(Cell(3,3)))

  @Test def testAdd(): Unit =
    var grid = Grid(4, 4)
    val cell = Cell(2,3)
    grid = grid + Cell(3,3) + Cell(2,3)
    assertEquals(Option.Some(cell), grid(2, 3))

  @Test def testRemove(): Unit =
    val grid = Grid(4, 4)
    grid + Cell(3, 3)
    grid - Cell(3, 3)
    assertEquals(Option.None(), grid(3, 3))

  @Test def testDifference(): Unit =
    var grid = Grid(4, 4)
    grid = grid + Cell(3, 3) + Cell(1,2) + Cell(0,1)
    val difference:Grid =  grid - Grid(4,4,ListBuilder(Cell(3,3),Cell(0,1)))
    assertEquals(ListBuilder(Cell(1,2)), difference.cells)




