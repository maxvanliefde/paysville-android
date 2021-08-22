package be.thefluffypangolin.paysville;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import be.thefluffypangolin.paysville.model.GameParameters;
import be.thefluffypangolin.paysville.model.PaysVilleGame;
import be.thefluffypangolin.paysville.model.Player;

public class GameViewModel extends ViewModel {

    private PaysVilleGame game;

    /**
     * Initialise le jeu (si nécessaire), et lance une première manche.
     */
    public void init(GameParameters parameters, int number, String[] names) {
        if (game == null) {
            game = new PaysVilleGame(parameters, number, generatePlayersList(names));
            game.addNewRound();
        }
    }

    /**
     * Crée une liste de joueurs de noms donnés
     * @param names les noms des joueurs
     * @return une liste contenant de nouvelles instances de Player,
     * dans le même ordre que la liste names
     */
    public static Player[] generatePlayersList(String[] names) {
        return Arrays.stream(names).map(Player::new).toArray(Player[]::new);
    }

    /**
     * @return l'instance de jeu
     */
    public PaysVilleGame getGame() {
        return game;
    }
}