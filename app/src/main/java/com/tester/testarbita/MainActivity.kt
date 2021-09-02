package com.tester.testarbita


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tester.testarbita.models.BoardSize
import com.tester.testarbita.models.MemoryGame
import com.tester.testarbita.utils.EXTRA_BOARD_SIZE
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var rvBoard : RecyclerView
    private lateinit var clroot :ConstraintLayout
    private  lateinit var numMoves : TextView
    private lateinit var numScore : TextView
    private lateinit var memoryGame: MemoryGame

    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        rvBoard = findViewById(R.id.rvBoard)
        numMoves = findViewById(R.id.numMoves)
        numScore = findViewById(R.id.numScore)
        clroot = findViewById(R.id.clroot)

        setupboard()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.refreshbtn -> {
                if (memoryGame.totalMoves > 0 && !memoryGame.isWin()) {
                    setAlert( "Quit the Game?",view = null,positiveButtonClickListener = View.OnClickListener {  setupboard()})
                }
                else{
                    setupboard()
                }
                return true
            }
            R.id.newSize->{
                setSizeAlert()
                return true
            }
            R.id.change_theme -> {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                return true
            }

            R.id.service ->{
                startActivity(Intent(this, ServiceActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setSizeAlert()
    {
        val BoardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val radioGroupView = BoardSizeView.findViewById<RadioGroup>(R.id.radio_group)
        when(boardSize)
        {
            BoardSize.EASY ->radioGroupView.check(R.id.btn_easy)
            BoardSize.MEDIUM -> radioGroupView.check(R.id.btn_medium)
            BoardSize.HARD -> radioGroupView.check(R.id.btn_hard)
        }
        setAlert("Choose new Board Size", BoardSizeView, positiveButtonClickListener =View.OnClickListener {
            boardSize = when(radioGroupView.checkedRadioButtonId)
            {
                R.id.btn_easy -> BoardSize.EASY
                R.id.btn_medium -> BoardSize.MEDIUM
                R.id.btn_hard -> BoardSize.HARD
                else -> boardSize
            }
            setupboard()
        })

    }

    private fun setAlert( title:String, view: View?, positiveButtonClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("cancel", null)
            .setPositiveButton("OK"){_,_->
                positiveButtonClickListener.onClick(null)
            }.show()

    }

    private fun setupboard() {
        memoryGame = MemoryGame(boardSize)

        rvBoard.adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object : MemoryBoardAdapter.CardClickListener {
            override fun onCardClick(position: Int) {
                updateGamewithFlip(position)
            }

        })
        rvBoard.layoutManager =GridLayoutManager(this,boardSize.getWidth())

        numScore.text ="Score: ${memoryGame.numPairesFound}/${boardSize.getNumPairs()}" //update the score
        numMoves.text ="Moves:${memoryGame.totalMoves/2} "

    }

    private fun updateGamewithFlip(position: Int) {

        if (!memoryGame.cards[position].isFaceUp) {

            if (memoryGame.flipcard(position)){ //if a match is made
                numScore.text ="Score: ${memoryGame.numPairesFound}/${boardSize.getNumPairs()}" //update the score

                //if the user wins the game
                if (memoryGame.isWin()) {
                    Snackbar.make(clroot, "You Won!!",Snackbar.LENGTH_LONG).show()
                }
            }
            numMoves.text ="Moves:${memoryGame.totalMoves/2} "   //update the moves
        }

        rvBoard.adapter?.notifyDataSetChanged()
    }

}