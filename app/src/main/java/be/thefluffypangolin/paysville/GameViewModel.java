package be.thefluffypangolin.paysville;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.model.GameParameters;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class GameViewModel extends ViewModel {

    private PaysVilleGame game;

    /**
     * Initialise le jeu (si nécessaire), et lance une première manche.
     */
    public void init(GameParameters parameters, int number, String[] names) {
        if (game == null) {
            game = new PaysVilleGame(parameters, number, GameParameters.generatePlayersList(names));
            try {
                game.addNewRound();
            } catch (PaysVilleGame.NoLetterLeftException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return l'instance de jeu
     */
    public PaysVilleGame getGame() {
        return game;
    }
}