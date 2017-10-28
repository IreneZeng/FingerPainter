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
    private static int CHOOSE_COLOR = 0;
    private static int CHOOSE_BRUSH = 1;
    private static int colorBeforeEraser;
    private static int widthBeforeEraser;
    private static int eraserSize = 70;
    private static boolean selected = false;
    private static String eraserColor = "#ffffffff";
    private static ImageButton eraserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fingerPainterView = new FingerPainterView(this);

        colorBeforeEraser = fingerPainterView.getColour();
        widthBeforeEraser = fingerPainterView.getBrushWidth();

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
        Bundle bundle;

        switch (view.getId()) {
            case R.id.chooseColorButton:
                intent = new Intent(this, ChooseColorActivity.class);
                bundle = new Bundle();

                //
                if (selected) {
                    bundle.putInt("currentColor", colorBeforeEraser);
                    bundle.putInt("currentWidth", widthBeforeEraser);
                } else {
                    bundle.putInt("currentColor", fingerPainterView.getColour());
                }

                intent.putExtras(bundle);
                startActivityForResult(intent, CHOOSE_COLOR);
                break;

            case R.id.chooseBrushButton:
                intent = new Intent(this, ChooseBrushActivity.class);

                // Pass the current brush color, brush width and style to the ChooseBrushActivity
                bundle = new Bundle();
                if (selected) {
                    bundle.putInt("currentColor", colorBeforeEraser);
                    bundle.putInt("currentBrushWidth", widthBeforeEraser);
                } else {
                    bundle.putInt("currentColor", fingerPainterView.getColour());
                    bundle.putInt("currentBrushWidth", fingerPainterView.getBrushWidth());
                }
                bundle.putSerializable("currentBrushStyle", fingerPainterView.getBrush());
                intent.putExtras(bundle);

                startActivityForResult(intent, CHOOSE_BRUSH);
                break;

            case R.id.eraserButton:
                if (!selected) {
                    eraserButton.setImageResource(R.drawable.eraser_button_active);
                    fingerPainterView.setColour(Color.parseColor(eraserColor));
                    fingerPainterView.setBrushWidth(eraserSize);
                    selected = true;

                } else {
                    eraserButton.setImageResource(R.drawable.eraser_button_inactive);
                    fingerPainterView.setColour(colorBeforeEraser);
                    fingerPainterView.setBrushWidth(widthBeforeEraser);
                    selected = false;
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
                if (selected) {
                    // Disable the eraser settings
                    eraserButton.setImageResource(R.drawable.eraser_button_inactive);
                    fingerPainterView.setBrushWidth(widthBeforeEraser);
                    selected = false;
                }

                fingerPainterView.setColour(data.getExtras().getInt("newColor"));
                colorBeforeEraser = data.getExtras().getInt("newColor");
            }
        }
        if (requestCode == CHOOSE_BRUSH) {
            if (resultCode == RESULT_OK) {
                if (selected) {
                    // Disable the eraser functionality
                    fingerPainterView.setColour(colorBeforeEraser);
                    eraserButton.setImageResource(R.drawable.eraser_button_inactive);
                    selected = false;
                }

                Paint.Cap newBrushStyle = ((Paint.Cap) data.getSerializableExtra("newBrushStyle"));
                int newBrushWidth = data.getExtras().getInt("newBrushWidth");

                if(newBrushStyle != null) {
                    fingerPainterView.setBrush(newBrushStyle);
                }

                if (newBrushWidth != 0) {
                    // check whether the user changed the brush width
                    fingerPainterView.setBrushWidth(newBrushWidth);
                    widthBeforeEraser = newBrushWidth;
                } else {
                    fingerPainterView.setBrushWidth(widthBeforeEraser);
                }
            }
        }
    }
}
