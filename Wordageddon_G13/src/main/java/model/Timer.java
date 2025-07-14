package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

public class Timer {

    private Timeline timeline;
    private int timeInSeconds;
    private int remainingSeconds;
    private Consumer<Integer> onTick;

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

    public void start() { timeline.play(); }

    public void stop() { timeline.pause(); }

    public void reset() {

        stop();

        remainingSeconds = timeInSeconds;

        if (onTick != null) {
            onTick.accept(remainingSeconds);
        }
    }

    public void setOnTick(Consumer<Integer> onTick) {
        this.onTick = onTick;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }
}
