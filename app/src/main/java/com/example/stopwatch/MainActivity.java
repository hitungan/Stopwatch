package com.example.stopwatch;

import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private int a;
    MaterialButton start;
    MaterialButton stop;
    MaterialButton reset;
    TextView angka;
    StopWatch stopWatch;
    private boolean ok;
    private Handler handler;
    CoordinatorLayout layout;

    public class StopWatch implements Runnable {
        private int time;
        private boolean done;

        public StopWatch(int time) {
            this.time = time;
            done = false;
        }

        private String getTime() {
            int hour = time / 3600;
            int menit = (time - 3600 * hour) / 60;
            int detik = time % 60;

            DecimalFormat format = new DecimalFormat("00");
            String waktu = format.format(hour) + ":" + format.format(menit) + ":" + format.format(detik);

            return waktu;
        }

        public void stop() {
            done = true;
        }

        public int getWaktu() {
            return time;
        }

        @Override
        public void run() {
            try {
                do {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            angka.setText(getTime());
                        }
                    });
                    Thread.sleep(1000);
                    ++time;
                } while (!done);
            } catch (Exception e) {
                Log.d("okokokokok", e.getMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        angka = findViewById(R.id.angka);
        reset = findViewById(R.id.reset);
        layout = findViewById(R.id.layout);

        handler = new Handler();

        a = 0;
        ok = true;

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ok) {
                    stopWatch = new StopWatch(a);
                    new Thread(stopWatch).start();
                    ok = false;
                    Snackbar.make(layout, "Stopwatch Started", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopWatch != null) {
                    stopWatch.stop();
                    a = stopWatch.getWaktu();
                    Snackbar.make(layout, "Stopwatch Stopped", Snackbar.LENGTH_SHORT).show();
                }
                stopWatch = null;
                ok = true;
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = 0;
                angka.setText(getResources().getString(R.string.init));
                if (stopWatch != null) {
                    stopWatch.stop();
                    stopWatch = null;
                    Snackbar.make(layout, "Stopwatch Reset", Snackbar.LENGTH_SHORT).show();
                }
                ok = true;
            }
        });
    }
}
