package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Classe Timer per gestire un conto alla rovescia basato su JavaFX Timeline.
 * Permette di impostare una durata in secondi, avviare, fermare e resettare il timer,
 * e notificare ad ogni "tick" (ogni secondo) il tempo rimanente tramite un Consumer.
 */
public class Timer {


    private Timeline timeline;

    private int timeInSeconds;

    private int remainingSeconds;

    /**
     * Consumer che viene chiamato ad ogni tick con il tempo rimanente.
     */
    private Consumer<Integer> onTick;

    /**
     * Costruisce un nuovo Timer con il tempo specificato in secondi.
     *
     * @param timeInSeconds durata del timer in secondi
     */
    public Timer(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        this.remainingSeconds = timeInSeconds;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            remainingSeconds--;

            if (onTick != null) {
                onTick.accept(remainingSeconds);
            }

            if (remainingSeconds <= 0) {
                stop();
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Avvia il timer.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Ferma il timer.
     */
    public void stop() {
        timeline.pause();
    }

    /**
     * Resetta il timer al tempo iniziale e ferma il conto alla rovescia.
     * Notifica anche il tempo aggiornato tramite il Consumer onTick, se presente.
     */
    public void reset() {
        stop();
        remainingSeconds = timeInSeconds;
        if (onTick != null) {
            onTick.accept(remainingSeconds);
        }
    }

    /**
     * Imposta il Consumer da chiamare ad ogni tick del timer,
     * ricevendo il tempo rimanente in secondi.
     *
     * @param onTick riceve il tempo rimanente
     */
    public void setOnTick(Consumer<Integer> onTick) {
        this.onTick = onTick;
    }

    /**
     * Restituisce il tempo rimanente in secondi.
     *
     * @return tempo rimanente in secondi
     */
    public int getRemainingSeconds() {
        return remainingSeconds;
    }
}
