package ss.projectt10.ui.scanQRCode;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.Writer;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import ss.projectt10.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link scanQRCode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class scanQRCode extends Fragment {
    private View scanQRCodeView;
    private Button mScanButton, mSearchWebButton, mConnectWifiButton;
    private TextView mQRCodeContent, mFomart, mType, mMetadata;
    private ImageView mQRCodeImage;
    public scanQRCode() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static scanQRCode newInstance(String param1, String param2) {
        scanQRCode fragment = new scanQRCode();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        scanQRCodeView =  inflater.inflate(R.layout.fragment_scan_qr_code, container, false);
        return scanQRCodeView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mScanButton = (Button) scanQRCodeView.findViewById(R.id.btn_scan_qr);
        mSearchWebButton = (Button) scanQRCodeView.findViewById(R.id.btn_search_web);
        mConnectWifiButton = (Button) scanQRCodeView.findViewById(R.id.btn_connect_wifi);
        mQRCodeContent = (TextView) scanQRCodeView.findViewById(R.id.tv_qr_code_content);
        mFomart = (TextView) scanQRCodeView.findViewById(R.id.tv_format);
        mType = (TextView) scanQRCodeView.findViewById(R.id.tv_type);
        mMetadata = (TextView) scanQRCodeView.findViewById(R.id.tv_metadata);
        mQRCodeImage = (ImageView) scanQRCodeView.findViewById(R.id.iv_qr_code);

        mSearchWebButton.setVisibility(View.GONE);
        mConnectWifiButton.setVisibility(View.GONE);

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQRCode();
            }
        });
        mSearchWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(mQRCodeContent.getText().toString());
            }
        });


    }

    private void scanQRCode() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String formatName =  result.getFormatName()  ;
        String content = result.getContents();

        mFomart.setText(formatName);

        mQRCodeContent.setText(content);

        mSearchWebButton.setVisibility(View.VISIBLE);

        loadImageBarcode(formatName, content, mQRCodeImage);



    }
    private static ParsedResult parseResult(Result rawResult) {
        return ResultParser.parseResult(rawResult);
    }


    public void search(String text) {
        if (text.startsWith("http")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(text)));
        }
        else {
            //https://www.google.com/#q=
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/#q=" + text)));
        }
    }

    final void openURL(String url) {
        // Strangely, some Android browsers don't seem to register to handle HTTP:// or HTTPS://.
        // Lower-case these as it should always be OK to lower-case these schemes.
        if (url.startsWith("HTTP://")) {
            url = "http" + url.substring(4);
        } else if (url.startsWith("HTTPS://")) {
            url = "https" + url.substring(5);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
            Log.w("OpenURL", "Nothing available to handle " + intent);
            Toast.makeText(getContext(),"Lỗi", Toast.LENGTH_LONG).show();
        }
    }

    public void loadImageBarcode(String codeType, String content, ImageView mBarcodeImage) {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer coWriter;
            coWriter = new MultiFormatWriter();
            BarcodeFormat format = BarcodeFormat.valueOf(codeType);
            BitMatrix byteMatrix = coWriter.encode(content, format, 400, 200, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            mBarcodeImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
    }


}
