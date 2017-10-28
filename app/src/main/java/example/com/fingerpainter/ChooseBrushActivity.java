package example.com.fingerpainter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.media.Image;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.concurrent.LinkedBlockingDeque;

public class ChooseBrushActivity extends AppCompatActivity implements View.OnClickListener{
    Intent intent = new Intent();
    private static int MAX_PROGRESS = 100;
    private static int width;
    private static int height;
    private static int currentBrushWidth;
    private static int newBrushWidth;
    private static int currentColor;
    private static Bitmap bitmap;
    private static Canvas canvas;
    private static Paint.Cap ROUND_BRUSH = Paint.Cap.ROUND;
    private static Paint.Cap SQUARE_BRUSH = Paint.Cap.SQUARE;
    private static Paint.Cap currentBrushStyle;
    private static ImageButton roundBrushButton;
    private static ImageButton squareBrushButton;
    private static ImageView strokeImageView;
    private static boolean rSelected = false;
    private static boolean sSelected = false;
    private static SeekBar brushWidthSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_brush);

        currentColor = getIntent().getExtras().getInt("currentColor");
        currentBrushWidth = getIntent().getExtras().getInt("currentBrushWidth");
        currentBrushStyle = ((Paint.Cap) getIntent().getExtras().getSerializable("currentBrushStyle"));

        strokeImageView = (ImageView) findViewById(R.id.strokeImageView);

        roundBrushButton = (ImageButton) findViewById(R.id.roundBrushButton);
        roundBrushButton.setOnClickListener(this);

        squareBrushButton = (ImageButton) findViewById(R.id.squareBrushButton);
        squareBrushButton.setOnClickListener(this);

        ImageButton finishSettingButton = (ImageButton) findViewById(R.id.finish_setting_button);
        finishSettingButton.setOnClickListener(this);

        // Set default brush style
        if (currentBrushStyle == Paint.Cap.ROUND) {
            roundBrushButton.setImageResource(R.drawable.round_brush_button_active);
            rSelected = true;
        } else {
            squareBrushButton.setImageResource(R.drawable.square_brush_button_active);
            sSelected = true;
        }

        drawInitialStroke();

        // For seekbar
        brushWidthSeekBar = (SeekBar) findViewById(R.id.brushWidthSeekBar);
        brushWidthSeekBar.setProgress(currentBrushWidth);
        brushWidthSeekBar.setMax(MAX_PROGRESS);

        seekbarListener();
    }

    public void drawInitialStroke () {
        strokeImageView.post(new Runnable() {
            @Override
            public void run() {
                // Draw stroke on the ImageView to indicate the width of the brush
                LinearLayout baseLayout = (LinearLayout) findViewById(R.id.baseLayout);
                width = baseLayout.getWidth();
                height = baseLayout.getHeight();
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                drawStroke(canvas, bitmap, currentBrushWidth, strokeImageView);
            }
        });
    }

    public void seekbarListener () {
        brushWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas.setBitmap(bitmap);
                drawStroke(canvas, bitmap, progress, strokeImageView);

                // Update the width of the stroke based on the progress
                newBrushWidth = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                intent.putExtra("newBrushWidth", newBrushWidth);
            }
        });
    }

    // draw the stroke to indicate the brush width
    public void drawStroke(Canvas imageCanvas, Bitmap bitmap, int brushWidth, ImageView imageView) {
        Paint paint = new Paint();
        int start_x = 0;
        int end_x = bitmap.getWidth();
        int start_y = bitmap.getHeight()/2;
        int end_y = bitmap.getHeight()/2;

        if (rSelected) {
            paint.setStrokeCap(Paint.Cap.ROUND);
            start_x = brushWidth;
            end_x = end_x - brushWidth;
        } else {
            paint.setStrokeCap(Paint.Cap.SQUARE);
        }
        paint.setColor(currentColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(brushWidth);
        paint.setAntiAlias(true);
        paint.setDither(true);

        imageCanvas.drawLine(
                start_x,
                start_y,
                end_x,
                end_y,
                paint
        );

        imageView.setImageBitmap(bitmap);
        imageView.invalidate();
    }

    @Override
    public void onClick(View view) {
        Paint.Cap brush;

        // Update the brush width
        int brushWidth = currentBrushWidth;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        if(newBrushWidth != 0) {
            brushWidth = newBrushWidth;
        }

        switch (view.getId()) {

            case R.id.roundBrushButton:
                brush = ROUND_BRUSH;
                intent.putExtra("newBrushStyle", brush);

                if ((rSelected == false && sSelected == false) || (rSelected == false && sSelected == true)) {
                    roundBrushButton.setImageResource(R.drawable.round_brush_button_active);
                    squareBrushButton.setImageResource(R.drawable.square_brush_button_inactive);
                    rSelected = true;
                    sSelected = false;
                }

                // Update the stroke
                drawStroke(canvas, bitmap, brushWidth, strokeImageView);

                break;

            case R.id.squareBrushButton:
                brush = SQUARE_BRUSH;
                intent.putExtra("newBrushStyle", brush);

                if ((sSelected == false && rSelected == false) || (sSelected == false && rSelected == true)) {
                    squareBrushButton.setImageResource(R.drawable.square_brush_button_active);
                    roundBrushButton.setImageResource(R.drawable.round_brush_button_inactive);
                    sSelected = true;
                    rSelected = false;
                }

                // Update the stroke
                drawStroke(canvas, bitmap, brushWidth, strokeImageView);

                break;

            case R.id.finish_setting_button:
                if (intent.getExtras() == null) {
                    // Set previous setting for brush if user didn't choose
                    intent.putExtra("chosenBrush", currentBrushStyle);
                    intent.putExtra("newBrushWidth", currentBrushWidth);
                }
                setResult(RESULT_OK, intent);
                finish();

            default:
                break;
        }
    }
}
