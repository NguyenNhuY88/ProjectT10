package ss.projectt10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;
import ss.projectt10.model.Card;
import ss.projectt10.view.EditCardActivity;

import static ss.projectt10.helper.Util.DATA_CARD;
import static ss.projectt10.helper.Util.DATA_CARD_UPDATE;
import static ss.projectt10.helper.Util.DATA_CATEGORY;

public class DetailCardActivity extends AppCompatActivity {
    private ImageView codeImage;
    private TextView code, mCardName, mCardNote;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);

        codeImage = findViewById(R.id.ac_detail_card_codeImage);
        code = findViewById(R.id.ac_detail_card_code);
        mCardName = (TextView) findViewById(R.id.tv_card_name_detail);
        mCardNote = (TextView) findViewById(R.id.tv_card_note_detail);

        Intent intent = getIntent();
       card = (Card) intent.getSerializableExtra(DATA_CARD);
        if (card != null) {
            String key = card.getCardCode();
            String codeType = card.getCodeType();

            code.setText(key);
            mCardName.setText(card.getCardName());
            mCardNote.setText(card.getNote());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_detail_card, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(this, EditCardActivity.class);

                intent.putExtra(DATA_CARD_UPDATE, card);
                startActivity(intent);

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
