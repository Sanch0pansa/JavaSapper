package com.example.grid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView mines;
    Button[][] cells;
    int[][] fields;
    boolean[][] opened_fields;
    boolean[][] flags;
    final int WIDTH = 8;
    final int HEIGHT = 10;
    final int MINESCONST = 10;
    int MinesCurrent = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mines = findViewById(R.id.TV);
        mines.setText(""+MINESCONST+" / "+MinesCurrent);
        generate();

    }

    public void generate(){
        GridLayout layout = findViewById(R.id.GRID);
        layout.removeAllViews();
        layout.setColumnCount(WIDTH);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        cells = new Button[HEIGHT][WIDTH];
        fields = new int[HEIGHT][WIDTH];
        opened_fields = new boolean[HEIGHT][WIDTH];
        flags = new boolean[HEIGHT][WIDTH];

        // Declaration of buttons
        for(int i=0;i<HEIGHT;i++){
            for(int j=0;j<WIDTH;j++){
                cells[i][j] = (Button) inflater.inflate(R.layout.cell,layout,false);
                fields[i][j] = 0;
                opened_fields[i][j] = false;
                flags[i][j] = false;
            }
        }

        for(int i=0;i<HEIGHT;i++){
            for(int j=0;j<WIDTH;j++){
                int finalJ = j;
                int finalI = i;
                cells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)  {
                        try {
                            MainActivity.this.open(finalI, finalJ);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                cells[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        try {
                            MainActivity.this.setFlag(finalI, finalJ);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });

                cells[i][j].setText("?");
                cells[i][j].setTag(""+(j+WIDTH*i));
                layout.addView(cells[i][j]);
            }
        }


        Random rand = new Random();
        // Generating mines
        for (int i = 0; i < MINESCONST; i++) {
            int x = rand.nextInt(WIDTH - 1);
            int y = rand.nextInt(HEIGHT - 1);

            while (fields[y][x] == -1) {
                x = rand.nextInt(WIDTH - 1);
                y = rand.nextInt(HEIGHT - 1);
            }
            fields[y][x] = -1;
            cells[y][x].setText("M");
        }

        // Counting nearest mines
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH;j++) {
                if (fields[i][j] > -1) {
                    int mn_y = Math.max(i - 1, 0);
                    int mn_x = Math.max(j - 1, 0);
                    int mx_y = Math.min(i + 1, HEIGHT - 1);
                    int mx_x = Math.min(j + 1, WIDTH - 1);

                    for (int cy = mn_y; cy <= mx_y; cy++) {
                        for (int cx = mn_x; cx <= mx_x; cx++) {
                            if ((cx != j || cy != i) && fields[cy][cx] == -1) {
                                fields[i][j] += 1;
                            }
                        }
                    }
                    cells[i][j].setText("" + fields[i][j]);
                }
            }
        }
    }

    public void open(int y, int x) {
        int color = Color.BLACK;
        String text = "" + fields[y][x];
        switch(fields[y][x]) {
            case 0:
                text = "";
                break;
            case 1:
                color = Color.BLUE;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.RED;
                break;
            case 4:
                color = Color.rgb(40, 10, 200);
                break;
            case 5:
                color = Color.rgb(200, 0, 0);
                break;
        }
        cells[y][x].setTextColor(color);
        cells[y][x].setBackgroundColor(Color.BLACK);
        cells[y][x].setText(text);

        if (fields[y][x] == -1) {
            cells[y][x].setBackgroundColor(Color.RED);
            cells[y][x].setText("M");
            cells[y][x].setTextColor(Color.WHITE);
        }
    }

    public void setFlag(int y, int x) {
        cells[y][x].setBackgroundColor(Color.BLUE);
    }
}