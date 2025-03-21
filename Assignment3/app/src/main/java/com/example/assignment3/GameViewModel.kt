import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment3.Game

class GameViewModel : ViewModel() {
    private var _gamelist = MutableLiveData(ArrayList<Game>())
    val gamelist: LiveData<ArrayList<Game>> get() = _gamelist

    fun set(games : ArrayList<Game>) {
        _gamelist.value = games;
    }

    fun add(game: Game) {
        var newlist = _gamelist.value!!
        newlist.add(game);
        _gamelist.value = newlist
    }

    fun update(game: Game, oldname : String) {
        var newlist = _gamelist.value!!
        for(i in 0..newlist.size - 1){
            if(newlist[i].name.equals(oldname)){
                newlist[i] = game
            }
        }
        _gamelist.value = newlist
    }

    fun remove(name: String) {
        val updatedList = _gamelist.value?.filter { !it.name.equals(name) } // Filter out the game by name
        _gamelist.value = ArrayList(updatedList) // Create a new ArrayList from the filtered results
    }
}