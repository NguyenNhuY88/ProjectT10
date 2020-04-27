package ss.projectt10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;
import ss.projectt10.model.MyCard;

public class DetailCardActivity extends AppCompatActivity {
    private ImageView codeImage;
    private TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);

        codeImage = findViewById(R.id.ac_detail_card_codeImage);
        code = findViewById(R.id.ac_detail_card_code);

        Intent intent = getIntent();
        MyCard card = (MyCard) intent.getSerializableExtra("data");
        if (card != null) {
            String key = card.getCardCode();
            String codeType = card.getCodeType();

            code.setText(key);

            try {
                Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                Writer coWriter;
                coWriter = new MultiFormatWriter();
                BarcodeFormat format = BarcodeFormat.valueOf(codeType);
                BitMatrix byteMatrix = coWriter.encode(key, format, 400, 200, hintMap);
                int width = byteMatrix.getWidth();
                int height = byteMatrix.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                codeImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.getMessage();
            }
        }

    }
}
