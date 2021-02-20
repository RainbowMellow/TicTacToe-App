package com.example.tictactoe

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.children
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.view.marginTop as marginTop

var playerX = User()
var playerO = User()
var currentUser = User()
var is3x3: Boolean = true
var totalButtons: Int = 0
var count: Int = 0
var columns: Int = 0
var rows: Int = 0
var isInGame: Boolean = false
var xList = ArrayList<Int>()
var oList = ArrayList<Int>()

class MainActivity : AppCompatActivity() {
    override fun onSaveInstanceState(outState: Bundle) {

        if(isInGame)
        {
            outState.putBoolean("gameBool", isInGame)
            outState.putIntegerArrayList("xList", xList)
            outState.putIntegerArrayList("oList", oList)
            outState.putBoolean("is3x3", is3x3)
            outState.putBoolean("isX", currentUser.isX)

            outState.putString("nameX", playerX.name)
            outState.putString("nameO", playerO.name)
            outState.putBoolean("isGameOver", checkIfWonOrTie())
        }
        super.onSaveInstanceState(outState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null)
        {
            val gameBool = savedInstanceState.getBoolean("gameBool")
            val boolIs3x3 = savedInstanceState.getBoolean("is3x3")
            val boolIsX = savedInstanceState.getBoolean("isX")
            val listX = savedInstanceState.getIntegerArrayList("xList")
            val listO = savedInstanceState.getIntegerArrayList("oList")
            val xName = savedInstanceState.getString("nameX")
            val oName = savedInstanceState.getString("nameO")
            if(gameBool)
            {
                val isGameOver = savedInstanceState.getBoolean("isGameOver")

                is3x3 = boolIs3x3

                if(boolIs3x3)
                {
                    rbFirst.isChecked = true
                }
                else {
                    rbSecond.isChecked = true
                }


                if(xName == "X")
                {
                    playerX.name = "X"
                }
                else {
                    playerX.name = xName.toString()
                }
                playerX.isX = true

                if(oName == "O")
                {
                    playerO.name = "O"
                }
                else {
                    playerO.name = oName.toString()
                }
                playerO.isX = false


                currentUser = if(boolIsX) {
                    playerX
                } else {
                    playerO
                }

                startGame(gameBool, listX as ArrayList<Int>, listO as ArrayList<Int>)

                if(isGameOver)
                {
                    checkIfWonOrTie()
                }
            }
        }

        rbFirst.isChecked = true
        rbX.isChecked = true
        val rulesText = "To win you must get 3 in a row!"
        tvRulesText.text = rulesText
    }

    fun onClickBoardSize(view: View) {
        if(rbFirst.isChecked)
        {
            val rulesText = "To win you must get 3 in a row!"
            tvRulesText.text = rulesText
        }
        else if(rbSecond.isChecked)
        {
            val rulesText = "To win you must get 4 in a row!"
            tvRulesText.text = rulesText
        }
    }

    fun onClickPlay(view: View) {
        startGame(false, xList, oList)
    }

    fun startGame(isGameStarted: Boolean, listForX: ArrayList<Int>, listForO: ArrayList<Int>)
    {
        isInGame = true

        if(!isGameStarted)
        {
            //Set up players
            if(playerXName.text.isNotEmpty())
            {
                playerX.name = playerXName.text.toString()
            }
            else {
                playerX.name = "X"
            }
            playerX.isX = true

            if(playerOName.text.isNotEmpty())
            {
                playerO.name = playerOName.text.toString()
            }
            else {
                playerO.name = "O"
            }
            playerO.isX = false
        }

        if(rbX.isChecked)
        {
            currentUser = playerX
        }
        else if(rbO.isChecked)
        {
            currentUser = playerO
        }

        val turnText = currentUser.name + " it is your turn!"
        tvTurns.text = turnText

        if(rbFirst.isChecked)
        {
            removeAllForStart()
            columns = 3
            rows = 3
            createBoard(300, 300, 50F, isGameStarted, listForX, listForO)
            is3x3 = true
        }
        else if(rbSecond.isChecked)
        {
            columns = 4
            rows = 4
            removeAllForStart()
            createBoard(230, 100, 40F, isGameStarted, listForX, listForO)
            is3x3 = false
        }
    }

    fun removeAllForStart()
    {
        rlFirst.visibility = View.INVISIBLE
    }


    fun createBoard( heightBtn: Int, widthBtn: Int, btnTxtSize: Float, inMiddleOfGame: Boolean, listX: ArrayList<Int>, listO: ArrayList<Int>) {
        rlGame.visibility = View.VISIBLE

        glBoard.columnCount = columns
        glBoard.rowCount = rows

        totalButtons = columns * rows
        val buttonsToBeAdded = totalButtons - 1

        for (i in 0..buttonsToBeAdded) {
            val buttonDynamic = Button(this)
            buttonDynamic.height = heightBtn
            buttonDynamic.width = widthBtn
            buttonDynamic.textSize = btnTxtSize
            buttonDynamic.setTextColor(Color.BLACK)
            buttonDynamic.setOnClickListener { v ->
                onClickButtonBoard(v)
            }
            buttonDynamic.setBackgroundResource(R.drawable.buttons)
            glBoard.addView(buttonDynamic)
        }

        if(inMiddleOfGame)
        {
            listX.forEach{ i ->
                val button = glBoard[i]
                with (button as Button) {
                    button.text = "X"
                    button.isEnabled = false
                } }

            listO.forEach{ i ->
                val button = glBoard[i]
                with (button as Button) {
                    button.text = "O"
                    button.isEnabled = false
                } }
        }
    }

    private fun onClickButtonBoard(view: View) {

        with (view as Button) {
            if(currentUser.isX)
            {
                view.text = "X"
                val index = glBoard.indexOfChild(view)
                xList.add(index)
            }
            else if(!currentUser.isX)
            {
                view.text = "O"
                val index = glBoard.indexOfChild(view)
                oList.add(index)
            }

            view.setEnabled(false)
            count++

            println(glBoard.indexOfChild(view))
        }

        if(!checkIfWonOrTie())
        {
            if(currentUser == playerO)
            {
                currentUser = playerX
            }
            else if(currentUser == playerX)
            {
                currentUser = playerO
            }
            val turnText = currentUser.name + " it is your turn!"
            tvTurns.text = turnText
        }
    }

    //region Check If Someone Has Won
    private fun checkIfWonOrTie(): Boolean {
        val listOfButtons = ArrayList<Button>()

        glBoard.children.forEach { b ->
            listOfButtons.add(b as Button)
        }

        var xOrO = ""
        if(currentUser.isX)
        {
            xOrO = "X"
        }
        else if(!currentUser.isX)
        {
            xOrO = "O"
        }

        if(is3x3)
        {
            //WinCheck for 3x3 gameboard

            //Rows
            val rowList = arrayOf(0, 3, 6)
            rowList.forEach { firstNumber ->

                val secondNumber = firstNumber+1
                val thirdNumber = secondNumber+1

                if(checkButtonText3x3(listOfButtons, xOrO, firstNumber, secondNumber, thirdNumber)){
                    return true
                }
            }

            //Columns
            val columnList = arrayOf(0, 1, 2)
            columnList.forEach { firstNumber ->

                val secondNumber = firstNumber + columns
                val thirdNumber = secondNumber + columns

                if(checkButtonText3x3(listOfButtons, xOrO, firstNumber, secondNumber, thirdNumber)) {
                    return true
                }
            }

            //Diagonals
            //Left -> Right
            val firstNumberDL = 0
            val secondNumberDL = firstNumberDL + columns + 1
            val thirdNumberDL = secondNumberDL + columns + 1

            if(checkButtonText3x3(listOfButtons, xOrO, firstNumberDL, secondNumberDL, thirdNumberDL)) {
                return true
            }

            //Right -> Left
            val firstNumberDR = 2
            val secondNumberDR = firstNumberDR + columns - 1
            val thirdNumberDR = secondNumberDR + columns - 1

            if(checkButtonText3x3(listOfButtons, xOrO, firstNumberDR, secondNumberDR, thirdNumberDR)) {
                return true
            }
        }

        if(!is3x3)
        {
            //WinCheck for 4x4 gameboard

            //Rows
            val rowList = arrayOf(0, 4, 8, 12)
            rowList.forEach { firstNumber ->

                val secondNumber = firstNumber+1
                val thirdNumber = secondNumber+1
                val fourthNumber = thirdNumber+1

                if(checkButtonText4x4(listOfButtons, xOrO, firstNumber, secondNumber, thirdNumber, fourthNumber)) {
                    return true
                }
            }

            //Columns
            val columnList = arrayOf(0, 1, 2, 3)
            columnList.forEach { firstNumber ->

                val secondNumber = firstNumber + columns
                val thirdNumber = secondNumber + columns
                val fourthNumber = thirdNumber + columns

                if(checkButtonText4x4(listOfButtons, xOrO, firstNumber, secondNumber, thirdNumber, fourthNumber)) {
                    return true
                }
            }

            //Diagonals
            //Left -> Right
            val firstNumberDL = 0
            val secondNumberDL = firstNumberDL + columns + 1
            val thirdNumberDL = secondNumberDL + columns + 1
            val fourthNumberDL = thirdNumberDL + columns + 1

            if(checkButtonText4x4(listOfButtons, xOrO, firstNumberDL, secondNumberDL, thirdNumberDL, fourthNumberDL)) {
                return true
            }

            //Right -> Left
            val firstNumberDR = 3
            val secondNumberDR = firstNumberDR + columns - 1
            val thirdNumberDR = secondNumberDR + columns - 1
            val fourthNumberDR = thirdNumberDR + columns - 1

            if(checkButtonText4x4(listOfButtons, xOrO, firstNumberDR, secondNumberDR, thirdNumberDR, fourthNumberDR)) {
                return true
            }
        }

        if(count == totalButtons)
        {
            showWinOrTieMessage(listOfButtons, false)
            return true
        }
        return false
    }


    private fun checkButtonText3x3(listOfButtons: ArrayList<Button>, xOrO: String, firstIndex: Int, secondIndex: Int, thirdIndex: Int): Boolean{

        if(listOfButtons[firstIndex].text == xOrO && listOfButtons[secondIndex].text == xOrO && listOfButtons[thirdIndex].text == xOrO)
        {
            showWinOrTieMessage(listOfButtons, true)
            return true
        }
        return false
    }

    private fun checkButtonText4x4(listOfButtons: ArrayList<Button>, xOrO: String, firstIndex: Int, secondIndex: Int, thirdIndex: Int, fourthIndex: Int): Boolean{

        if(listOfButtons[firstIndex].text == xOrO && listOfButtons[secondIndex].text == xOrO && listOfButtons[thirdIndex].text == xOrO && listOfButtons[fourthIndex].text == xOrO)
        {
            showWinOrTieMessage(listOfButtons, true)
            return true
        }
        return false
    }

    //endregion

    private fun showWinOrTieMessage(buttonList: ArrayList<Button>, hasWon: Boolean)
    {
        buttonList.forEach{ b ->
            b.isEnabled = false
        }

        val gameEndedText = "The game has ended!"
        tvTurns.text = gameEndedText

        if(hasWon)
        {
            val winText = currentUser.name + " has won!"
            tvWin.text = winText
        }
        if(!hasWon)
        {
            val tieText = "It was a tie!"
            tvWin.text = tieText
        }
    }

    fun onClickReset(view: View) {
        isInGame = false

        glBoard.removeAllViewsInLayout()
        tvWin.text = ""
        count = 0
        xList.clear()
        oList.clear()
        rlGame.visibility = View.INVISIBLE
        rlFirst.visibility = View.VISIBLE
    }

}