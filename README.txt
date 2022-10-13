=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: sbroder
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
     I used a 2D array to represent the Minesweeper game board, which has 16 rows and 16 columns.
     This 2D array contains objects of type Cell, which is an abstract class implemented by objects
     of type EmptyCell and MineCell. A 2D array is an appropriate data structure to represent the board
     because the board is a grid of cells whose dimensions are immutable once it has been initialized,
     and cells often have to be accessed based on their position on the game board, which corresponds to
     their index in the array. The board is initialized as an empty array in the constructor or the reset
     method, and it is populated with mines and empty cells in one of two instances: upon the first left click
     on the board or if the user clicks the replay button.
     I also utilized a 2D array of chars to store and retrieve previous instances of the game board
     for when the user wants to replay their previous game.

  2. Recursion
     When a non-mine cell with 0 mines surrounding it is clicked, there is a cascade effect in which
     all the neighboring boxes are revealed with the number of mines surrounding them. If the number
     of mines surrounding one of these neighboring boxes is also 0, then all of its non-mine neighbors
     are also revealed. I used a recursive loop with the methods flood() in GameBoard and leftClick() in
     EmptyCell to account for this functionality. These methods repeatedly call each other until there are
     no more neighbors with 0 mines to be revealed.

  3. File I/O
     I used file I/O to allow the user to replay their previous game. The method storeBoard() writes
     the current board into a text file called previousBoard.txt. In this file, an 'X' is written to
     represent a mine cell and an 'O' is written to represent an empty cell. The method getPreviousBoard(),
     which reads this file and represents the board as a 2D char array, is called by replayPrevGame().
     When the replay button is clicked, replayPrevGame() populates the board with mines where there is an
     'X' and empty cells where there is an 'O'.
     I used image I/O to read in images, such as an empty cell, a mine, a flag, and number cells and
     display them on the game board.
     I used a reader to read in the game instructions from a file and display them in the JOptionPanel
     that is displayed when the user clicks the instructions JButton.

  4. Inheritance/Subtyping
     The game board will be filled with Cell objects, but there will be two types of cell objects:
     EmptyCell and MineCell. Both EmptyCell and MineCell will extend the abstract class Cell.
     Although both types of cells need to be stored in the same array, they must behave
     differently in certain situations. For example, when a mine cell is clicked, the game is over
     and the whole board is revealed. Conversely, when an empty cell is clicked, the number of neighboring
     mines is displayed and the recursive loop with flood() must occur. They also need to have different
     drawCell methods to display the correct image when they are revealed.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

      GameBoard:
      This class instantiates the board on which the game is played and keeps track of all
      aspects of the current game state. It creates and populates the 2D array that represents
      the game board. It handles clicks on the board by calling the appropriate click method on
      the correct cell. It provides the implementation for methods that are called when JButtons (such as
      reset and replay) are clicked. It also keeps track of and updates the current game status, the timer,
      and the number of mines left and updates all the JLabels that display this information accordingly.
      Finally, it handles when the user wins and loses.

      Cell:
      This is an abstract class, representing a cell that is on the game board. This abstract class
      is extended by EmptyCell and MineCell. It keeps track of the current state of the cell (hidden,
      revealed, or flagged), the row and column it occupies on the board, and the board instance it
      resides on. It also repaints cell objects by calling the respective drawCell methods in its
      subclasses.

      EmptyCell:
      Empty cells are cells on the game board that do not contain a mine. When one of these cells
      is clicked, it draws the number of mines that surround it. If 0 mines surround it, the recursive
      loop with flood() is called. EmptyCell objects keep track of the number of mines surrounding them.

      MineCell:
      Mine cells are cells on the game board that contain a  mine. When one of them is clicked, it draws
      a bomb, and the loseGame() method in GameBoard is called so that the game ends and the entire board
      is revealed.

      RunMinesweeper:
      Creates the top level frame for the game which contains a status panel (with JLabels that display
      the current game status, the timer, and the number of mines remaining), the game board, and a control
      panel (with a reset button, an instructions button, and a replay button). When the instructions
      button is pressed, a JOptionPane with the game instructions is displayed. These instructions
      are read in from a file called instructions.txt.

      Game:
      Creates an instance of Runnable to run the game

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

    At first, I had trouble conceptualizing which methods should go in what class.
    I was overwhelmed since several classes have to interact with each other, but
    I was able to combat this by breaking down the game into smaller pieces and handling my implementation
    one step at a time. Something else that was difficult part for me was the front end implementation, such as
    drawing the game board and updating it as the user interacted with it. While I felt much more confident updating
    the game state on the back end, reflecting these changes visually to the user is something I have not had as
    much practice with. I think I become more comfortable with this as I get more practice with Java's Swing library.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

    I feel that I did a good job of designing my game. The functionality is separated nicely
    because GameBoard handles all the back-end state of the game and updates the front-end
    when necessary based on these states, while RunMinesweeper only handles front-end functionality
    by setting up the frames, panels, and buttons for the game. I think my private state is sufficiently
    encapsulated because I wrote many getter and setter methods for private fields. If given the chance to
    refactor, I might try to write more helper methods to make my code more efficient and less redundant.
    In addition, drawing the entire board causes some lag time when the program is first opened and when the
    reset/replay buttons are clicked. If I had more time, I would look into more efficient ways to repaint
    the board so that the time it takes is minimized.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

    http://www.freeminesweeper.org/help/minehelpinstructions.html
    https://minesweeperonline.com/
    https://docs.oracle.com/javase/8/docs/api/javax/imageio/ImageIO.html
    https://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
