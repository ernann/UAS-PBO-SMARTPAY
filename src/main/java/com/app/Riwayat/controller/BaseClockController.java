package com.app.Riwayat.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class BaseClockController {
    private Timeline clockTimeline;
    
    protected void initializeClock(Label timeLabel) {
        if (clockTimeline != null && clockTimeline.getStatus() == Animation.Status.RUNNING) {
            clockTimeline.stop();
        }
        
        clockTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                event -> {
                    LocalTime now = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    timeLabel.setText(now.format(formatter));
                }
            ),
            new KeyFrame(Duration.seconds(1))
        );
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
    }
    
    protected void stopClock() {
        if (clockTimeline != null) {
            clockTimeline.stop();
            clockTimeline = null;
        }
    }
}