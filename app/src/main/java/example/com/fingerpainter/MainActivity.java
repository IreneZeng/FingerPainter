package example.com.fingerpainter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static FingerPainterView fingerPainterView;
    private static String eraser = "#ffffffff";
    private static int CHOOSE_COLOR = 0;
    private static int CHOOSE_BURSH = 1;
    private static int colorBeforeEraser;
    private static boolean clicked = true;
    private static ImageButton eraserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fingerPainterView = new FingerPainterView(this);

        colorBeforeEraser = fingerPainterView.getColour();

        fingerPainterView.load(getIntent().getData());

        LinearLayout baseLayout = (LinearLayout) findViewById(R.id.baseLayout);
        fingerPainterView.setId(R.id.FingerPainterViewId);
        baseLayout.addView(fingerPainterView);

        ImageButton chooseColorButton = (ImageButton) findViewById(R.id.chooseColorButton);
        chooseColorButton.setOnClickListener(this);

        ImageButton chooseBrushButton = (ImageButton) findViewById(R.id.chooseBrushButton);
        chooseBrushButton.setOnClickListener(this);

        eraserButton = (ImageButton) findViewById(R.id.eraserButton);
        eraserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.chooseColorButton:
                intent = new Intent(this, ChooseColorActivity.class);
                startActivityForResult(intent, CHOOSE_COLOR);
                break;

            case R.id.chooseBrushButton:
                intent = new Intent(this, ChooseBrushActivity.class);

                int currentBrushWidth = fingerPainterView.getBrushWidth();
                Paint.Cap currentBrushStyle = fingerPainterView.getBrush();

                // Pass the current brush width and style to the ChooseBrushActivity
                Bundle bundle = new Bundle();
                bundle.putInt("currentBrushWidth", currentBrushWidth);
                bundle.putSerializable("currentBrushStyle", currentBrushStyle);
                intent.putExtras(bundle);

                startActivityForResult(intent, CHOOSE_BURSH);
                break;

            case R.id.eraserButton:

                if (clicked) {
                    eraserButton.setImageResource(R.drawable.eraser_button_active);
                    fingerPainterView.setColour(Color.parseColor(eraser));
                    clicked = false;

                } else {
                    eraserButton.setImageResource(R.drawable.eraser_button_inactive);
                    fingerPainterView.setColour(colorBeforeEraser);
                    clicked = true;
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_COLOR) {
            if (resultCode == RESULT_OK) {
                String chosenColor = data.getExtras().getString("newColor");
                fingerPainterView.setColour(Color.parseColor(chosenColor));
                colorBeforeEraser = Color.parseColor(chosenColor);
            }
        }
        if (requestCode == CHOOSE_BURSH) {

            // Back to the main activity only when the user chose the brush style
            if (resultCode == RESULT_OK) {
                fingerPainterView.setColour(colorBeforeEraser);  // To discard eraser function

                Paint.Cap brushStyle = ((Paint.Cap) data.getSerializableExtra("newBrushStyle"));

                // deal the "go back" button
                if(brushStyle != null) {
                    fingerPainterView.setBrush(brushStyle);
                }

                if (data.getExtras().size() == 2 || (data.getExtras().size() == 1 && brushStyle == null)) {
                    // check whether the user changed the brush width
                    int brushWidth = data.getExtras().getInt("newBrushWidth");
                    fingerPainterView.setBrushWidth(brushWidth);
                }

            }
        }
    }
}
