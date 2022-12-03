package com.example.memorina;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Card {
    Paint p = new Paint();
    int color, backColor = Color.parseColor("#D2FDFF");;
    Bitmap image;
    boolean flag = false;

    public Card(boolean b) {
        flag = true;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    boolean isOpen = false; // цвет карты
    float x, y, width, height;

    public Card(float x, float y, float width, float height, Bitmap img, int color) {
        this.color = color;
        this.image = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @SuppressLint("ResourceAsColor")
    public void draw(Canvas c) {
        RectF r = new RectF(x, y, x + width, y + height);
        if (isOpen) {
            //p.setColor(color);
            c.drawBitmap(image, null, r, p);
        } else {
            p.setColor(backColor);
            c.drawRect(r, p);
        }
    }

    public boolean flip(float touch_x, float touch_y) {
        if (touch_x >= x && touch_x <= x + width && touch_y >= y && touch_y <= y + height) {
            isOpen = ! isOpen;
            return true;
        } else {
            return false;
        }
    }
}

public class TilesView extends View {
    // пауза для запоминания карт в секундах
    final int PAUSE_LENGTH = 2;
    boolean isOnPauseNow = false;

    // открытая карта
    int openedCardId = -1;
    int currentCardId = -1;

    // список карт на игровом поле
    ArrayList<Card> cards = new ArrayList<Card>();

    int[] colors = new int[]{R.drawable.banana, R.drawable.milk,R.drawable.egg,R.drawable.tomato,R.drawable.cucumber,R.drawable.butter,R.drawable.bread,R.drawable.tangerine};
    List<Integer> card_colors = new ArrayList<>();

    int width, height; // ширина и высота канвы
    float col = 5.5F, row = 8;

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // размеры поля
        Display display = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = context.getDisplay();
        }
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        //float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / row;
        float dpWidth  = outMetrics.widthPixels / col;

        // генерация 2*n карт
        for (int i=0;i<colors.length;i++) {
            card_colors.add(colors[i]);
            card_colors.add(colors[i]);
        }
        // перемешивание карт
        Collections.shuffle(card_colors);

        // размещение карт по игровому полю
        float x = 0, y = 0;
        int k = 3;
        for (int i=0;i<card_colors.size();i++) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), card_colors.get(i));
            cards.add(new Card(x, y, dpWidth, dpHeight, bm, card_colors.get(i)));
            x += dpWidth + dpWidth/col;
            if (i > 0 && i % k == 0) {
                System.out.println(i);
                y += dpHeight + dpHeight/row;
                x = 0;
                k += 4;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        // отрисовка плиток
        for (Card c: cards) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // получить координаты касания
        int x = (int) event.getX();
        int y = (int) event.getY();
        // определить тип события
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {
            // палец коснулся экрана
            for (int i=0;i<cards.size();i++) {
                // все карты закрыты
                if (openedCardId == -1) {
                    if (cards.get(i).flip(x, y)) {
                        Log.d("mytag", "card flipped: " + openedCardId);
                        openedCardId = i;
                        invalidate();
                        return true;
                    }
                } else  {
                    // перевернуть карту с задержкой
                    if (cards.get(i).flip(x, y)) {
                        invalidate();
                        PauseTask task = new PauseTask();
                        task.execute(PAUSE_LENGTH);
                        isOnPauseNow = true;
                        currentCardId = i;
                        return true;
                    }
                }

            }
        }

        // перерисовываем экран
        return true;
    }

    // запуск новой игры
    public void newGame() {

        Toast toast = Toast.makeText(getContext(),"Победа!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 700); // передаём число секунд ожидания
            } catch (InterruptedException e) {}
            Log.d("mytag", "Pause finished");
            return null;
        }

        // после паузы, перевернуть все карты обратно
        @Override
        protected void onPostExecute(Void aVoid) {

            invalidate();
            if (cards.get(currentCardId).color == cards.get(openedCardId).color) {
                cards.remove(openedCardId);
                if (currentCardId > openedCardId) {
                    currentCardId--;
                }
                cards.remove(currentCardId);
            } else {
                for (Card c: cards) {
                    if (c.isOpen) {
                        c.isOpen = false;
                    }
                }
            }
            openedCardId = -1;
            isOnPauseNow = false;
            // проверить, остались ли ещё карты
            // иначе сообщить об окончании игры
            if (!cards.isEmpty()) {
                newGame();
            }
        }
    }
}

