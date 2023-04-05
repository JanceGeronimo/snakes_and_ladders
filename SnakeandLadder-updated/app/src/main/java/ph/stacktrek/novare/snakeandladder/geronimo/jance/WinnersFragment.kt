package ph.stacktrek.novare.snakeandladder.geronimo.jance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ph.stacktrek.novare.snakeandladder.geronimo.jance.ARG_PARAM1
import ph.stacktrek.novare.snakeandladder.geronimo.jance.ARG_PARAM2
import ph.stacktrek.novare.snakeandladder.geronimo.jance.adapters.PlayerAdapter
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerDAO
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerDAOSQLLiteImplementation
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerWinnerDAO
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerWinnerDAOSQLLiteImplementation
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.FragmentWinnersBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.model.PlayerWinner

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WinnersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WinnersFragment : Fragment() {
    private lateinit var binding: FragmentWinnersBinding


    private lateinit var playerWinnerDAO: PlayerWinnerDAO

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWinnersBinding.inflate(inflater, container, false)

        playerWinnerDAO = PlayerWinnerDAOSQLLiteImplementation(requireContext().applicationContext)
        val latestPlayers: ArrayList<PlayerWinner> = playerWinnerDAO.get5LatestPlayersWinner()

        //More optimized and cleaner
        val textViews = listOf(
            binding.winner1Text,
            binding.winner2Text,
            binding.winner3Text,
            binding.winner4Text,
            binding.winner5Text
        )

        for (i in latestPlayers.indices) {
            if (i >= textViews.size) break
            textViews[i].text = latestPlayers[i].name
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WinnersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WinnersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}