package example.com.fingerpainter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseColorActivity extends AppCompatActivity implements View.OnClickListener{
    private static String BLACK = "#ff000000";
    private static String BLUE = "#ff33b5e5";
    private static String GREEN = "#ff99cc00";
    private static String YELLOW = "#ffffbb33";
    private static String RED = "#ffcc0000";
    private static String PURPLE = "#ffaa66cc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);

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
        blueButton.getBackground().setColorFilter(Color.parseColor(BLUE), PorterDuff.Mode.SRC);
        greenButton.getBackground().setColorFilter(Color.parseColor(GREEN), PorterDuff.Mode.SRC);
        yellowButton.getBackground().setColorFilter(Color.parseColor(YELLOW), PorterDuff.Mode.SRC);
        redButton.getBackground().setColorFilter(Color.parseColor(RED), PorterDuff.Mode.SRC);
        blackButton.getBackground().setColorFilter(Color.parseColor(BLACK), PorterDuff.Mode.SRC);
        purpleButton.getBackground().setColorFilter(Color.parseColor(PURPLE), PorterDuff.Mode.SRC);
    }

    @Override
    public void onClick(View view) {
        String chosenColor;
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.blueButton:
                chosenColor = BLUE;
                break;
            case R.id.greenButton:
                chosenColor = GREEN;
                break;
            case R.id.yellowButton:
                chosenColor = YELLOW;
                break;
            case R.id.redButton:
                chosenColor =  RED;
                break;
            case R.id.purpleButton:
                chosenColor = PURPLE;
                break;
            default:
                chosenColor = BLACK;
                break;
        }
        intent.putExtra("newColor", chosenColor);
        setResult(RESULT_OK, intent);
        finish();
    }
}
