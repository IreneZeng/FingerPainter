package example.com.fingerpainter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class ChooseColorActivity extends AppCompatActivity implements View.OnClickListener{
    private static String BLACK = "#000000";
    private static String BLUE = "#0000ff";
    private static String GREEN = "#00ff00";
    private static String YELLOW = "#ffffbb33";
    private static String RED = "#ff0000";
    private static String PURPLE = "#ffaa66cc";
    private static int r = 0;
    private static int g = 0;
    private static int b = 0;
    private static Button createColorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);

        int currentColor = getIntent().getExtras().getInt("currentColor");

        createColorButton = (Button) findViewById(R.id.user_define_color_button);
        createColorButton.setOnClickListener(this);

        // Create listener for buttons
        Button blueButton = (Button) findViewById(R.id.blueButton);
        blueButton.setOnClickListener(this);

        Button greenButton = (Button) findViewById(R.id.greenButton);
        greenButton.setOnClickListener(this);

        Button yellowButton = (Button) findViewById(R.id.yellowButton);
        yellowButton.setOnClickListener(this);

        Button redButton = (Button) findViewById(R.id.redButton);
        redButton.setOnClickListener(this);

        Button blackButton = (Button) findViewById(R.id.blackButton);
        blackButton.setOnClickListener(this);

        Button purpleButton = (Button) findViewById(R.id.purpleButton);
        purpleButton.setOnClickListener(this);

        // Set button background color
        createColorButton.getBackground().setColorFilter(currentColor, PorterDuff.Mode.SRC);
        blueButton.getBackground().setColorFilter(Color.parseColor(BLUE), PorterDuff.Mode.SRC);
        greenButton.getBackground().setColorFilter(Color.parseColor(GREEN), PorterDuff.Mode.SRC);
        yellowButton.getBackground().setColorFilter(Color.parseColor(YELLOW), PorterDuff.Mode.SRC);
        redButton.getBackground().setColorFilter(Color.parseColor(RED), PorterDuff.Mode.SRC);
        blackButton.getBackground().setColorFilter(Color.parseColor(BLACK), PorterDuff.Mode.SRC);
        purpleButton.getBackground().setColorFilter(Color.parseColor(PURPLE), PorterDuff.Mode.SRC);

        SeekBar redColorSeekBar = (SeekBar) findViewById(R.id.red_color_seekbar);
        SeekBar blueColorSeekBar = (SeekBar) findViewById(R.id.blue_color_seekbar);
        SeekBar greenColorSeekBar = (SeekBar) findViewById(R.id.green_color_seekbar);

        redColorSeekBar.setProgress(Color.red(currentColor));
        blueColorSeekBar.setProgress(Color.blue(currentColor));
        greenColorSeekBar.setProgress(Color.green(currentColor));

        TextView redValueTextView = (TextView) findViewById(R.id.red_value_textview);
        TextView blueValueTextView = (TextView) findViewById(R.id.blue_value_textview);
        TextView greenValueTextView = (TextView) findViewById(R.id.green_value_textview);

        redValueTextView.setText(String.valueOf(Color.red(currentColor)));
        blueValueTextView.setText(String.valueOf(Color.blue(currentColor)));
        greenValueTextView.setText(String.valueOf(Color.green(currentColor)));

        // Set seekbar listener
        seekBarListener(redColorSeekBar, redValueTextView, "RED");
        seekBarListener(blueColorSeekBar, blueValueTextView, "BLUE");
        seekBarListener(greenColorSeekBar, greenValueTextView, "GREEN");

        //Set gradient seekbar
        setGradientColor(redColorSeekBar, "RED");
        setGradientColor(blueColorSeekBar, "BLUE");
        setGradientColor(greenColorSeekBar, "GREEN");
    }

    @Override
    public void onClick(View view) {
        int chosenColor;
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.blackButton:
                chosenColor = Color.parseColor(BLACK);
                break;
            case R.id.blueButton:
                chosenColor = Color.parseColor(BLUE);
                break;
            case R.id.greenButton:
                chosenColor = Color.parseColor(GREEN);
                break;
            case R.id.yellowButton:
                chosenColor = Color.parseColor(YELLOW);
                break;
            case R.id.redButton:
                chosenColor =  Color.parseColor(RED);
                break;
            case R.id.purpleButton:
                chosenColor = Color.parseColor(PURPLE);
                break;
            case R.id.user_define_color_button:
                chosenColor = android.graphics.Color.rgb(r, g, b);
                break;
            default:
                chosenColor = Color.parseColor(BLACK);
                break;
        }
        intent.putExtra("newColor", chosenColor);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void seekBarListener (SeekBar seekBar, final TextView textView, String color) {
        final String inputColor = color;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (inputColor.equals("RED")) {
                    r = progress;
                    createColorButton.getBackground().setColorFilter(android.graphics.Color.rgb(progress, g, b), PorterDuff.Mode.SRC);
                } else if (inputColor.equals("BLUE")) {
                    b = progress;
                    createColorButton.getBackground().setColorFilter(android.graphics.Color.rgb(r, g, progress), PorterDuff.Mode.SRC);
                } else if (inputColor.equals("GREEN")) {
                    g = progress;
                    createColorButton.getBackground().setColorFilter(android.graphics.Color.rgb(r, progress, b), PorterDuff.Mode.SRC);
                }
                textView.setText(String.valueOf(progress));
            }
        });
    }

    public void setGradientColor(SeekBar seekBar, String color) {
        int[] colors = new int[] {};
        int startColor = Color.parseColor(BLACK);
        int endColor;

        if (color.equals("RED")) {
            endColor = Color.parseColor(RED);
            colors = new int[]{startColor, endColor};

        } else if (color.equals("BLUE")) {
            endColor = Color.parseColor(BLUE);
            colors = new int[] {startColor, endColor};

        } else if (color.equals("GREEN")) {
            endColor = Color.parseColor(GREEN);
            colors = new int[] {startColor, endColor};
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors);

        seekBar.setBackground(gradientDrawable);
    }
}
