package ph.stacktrek.novare.snakeandladder.geronimo.jance

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment

class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_game, container, false)

        val button = view.findViewById<Button>(R.id.start_button)

        button.setOnClickListener {
            // Navigate to the activity
            val intent = Intent(activity, PlayerActivity::class.java)
            startActivity(intent)

        }

        // Use the values as needed
        return view
    }


}