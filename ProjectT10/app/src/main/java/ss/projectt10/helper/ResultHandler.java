package ss.projectt10.helper;

/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.telephony.PhoneNumberUtils;
import com.google.zxing.Result;

import com.google.zxing.client.android.Intents;

import com.google.zxing.client.android.R;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.ArrayList;

import static com.google.zxing.client.result.ResultParser.parseResult;

/**
 * A base class for the Android-specific barcode handlers. These allow the app to polymorphically
 * suggest the appropriate actions for each data type.
 *
 * This class also contains a bunch of utility methods to take common actions like opening a URL.
 * They could easily be moved into a helper object, but it can't be static because the Activity
 * instance is needed to launch an intent.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public abstract class ResultHandler {

    private static final String TAG = ResultHandler.class.getSimpleName();

    private static final String[] EMAIL_TYPE_STRINGS = {"home", "work", "mobile"};
    private static final String[] PHONE_TYPE_STRINGS = {"home", "work", "mobile", "fax", "pager", "main"};
    private static final String[] ADDRESS_TYPE_STRINGS = {"home", "work"};
    private static final int[] EMAIL_TYPE_VALUES = {
            ContactsContract.CommonDataKinds.Email.TYPE_HOME,
            ContactsContract.CommonDataKinds.Email.TYPE_WORK,
            ContactsContract.CommonDataKinds.Email.TYPE_MOBILE,
    };
    private static final int[] PHONE_TYPE_VALUES = {
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME,
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK,
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK,
            ContactsContract.CommonDataKinds.Phone.TYPE_PAGER,
            ContactsContract.CommonDataKinds.Phone.TYPE_MAIN,
    };
    private static final int[] ADDRESS_TYPE_VALUES = {
            ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME,
            ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK,
    };
    private static final int NO_TYPE = -1;

    public static final int MAX_BUTTON_COUNT = 4;

    private final ParsedResult result;
    private final Activity activity;
    private final Result rawResult;


    ResultHandler(Activity activity, ParsedResult result) {
        this(activity, result, null);
    }

    ResultHandler(Activity activity, ParsedResult result, Result rawResult) {
        this.result = result;
        this.activity = activity;
        this.rawResult = rawResult;

    }

    public final ParsedResult getResult() {
        return result;
    }



    final Activity getActivity() {
        return activity;
    }

    /**
     * Indicates how many buttons the derived class wants shown.
     *
     * @return The integer button count.
     */
    public abstract int getButtonCount();

    /**
     * The text of the nth action button.
     *
     * @param index From 0 to getButtonCount() - 1
     * @return The button text as a resource ID
     */
    public abstract int getButtonText(int index);

    public Integer getDefaultButtonID() {
        return null;
    }

    /**
     * Execute the action which corresponds to the nth button.
     *
     * @param index The button that was clicked.
     */
    public abstract void handleButtonPress(int index);

    /**
     * Some barcode contents are considered secure, and should not be saved to history, copied to
     * the clipboard, or otherwise persisted.
     *
     * @return If true, do not create any permanent record of these contents.
     */
    public boolean areContentsSecure() {
        return false;
    }

    /**
     * Create a possibly styled string for the contents of the current barcode.
     *
     * @return The text to be displayed.
     */
    public CharSequence getDisplayContents() {
        String contents = result.getDisplayResult();
        return contents.replace("\r", "");
    }

    /**
     * A string describing the kind of barcode that was found, e.g. "Found contact info".
     *
     * @return The resource ID of the string.
     */
    public abstract int getDisplayTitle();

    /**
     * A convenience method to get the parsed type. Should not be overridden.
     *
     * @return The parsed type, e.g. URI or ISBN
     */
    public final ParsedResultType getType() {
        return result.getType();
    }


    private static int toEmailContractType(String typeString) {
        return doToContractType(typeString, EMAIL_TYPE_STRINGS, EMAIL_TYPE_VALUES);
    }


    private static int doToContractType(String typeString, String[] types, int[] values) {
        if (typeString == null) {
            return NO_TYPE;
        }
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            if (typeString.startsWith(type) || typeString.startsWith(type.toUpperCase(Locale.ENGLISH))) {
                return values[i];
            }
        }
        return NO_TYPE;
    }

    final void shareByEmail(String contents) {
        sendEmail(null, null, null, null, contents);
    }

    final void sendEmail(String[] to,
                         String[] cc,
                         String[] bcc,
                         String subject,
                         String body) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        if (to != null && to.length != 0) {
            intent.putExtra(Intent.EXTRA_EMAIL, to);
        }
        if (cc != null && cc.length != 0) {
            intent.putExtra(Intent.EXTRA_CC, cc);
        }
        if (bcc != null && bcc.length != 0) {
            intent.putExtra(Intent.EXTRA_BCC, bcc);
        }
        putExtra(intent, Intent.EXTRA_SUBJECT, subject);
        putExtra(intent, Intent.EXTRA_TEXT, body);
        intent.setType("text/plain");
        launchIntent(intent);
    }

    final void shareBySMS(String contents) {
        sendSMSFromUri("smsto:", contents);
    }

    final void sendSMS(String phoneNumber, String body) {
        sendSMSFromUri("smsto:" + phoneNumber, body);
    }

    private void sendSMSFromUri(String uri, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
        putExtra(intent, "sms_body", body);
        // Exit the app once the SMS is sent
        intent.putExtra("compose_mode", true);
        launchIntent(intent);
    }



    final void dialPhone(String phoneNumber) {
        launchIntent(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    final void dialPhoneFromUri(String uri) {
        launchIntent(new Intent(Intent.ACTION_DIAL, Uri.parse(uri)));
    }

    final void openMap(String geoURI) {
        launchIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(geoURI)));
    }

    /**
     * Do a geo search using the address as the query.
     *
     * @param address The address to find
     */
    final void searchMap(String address) {
        launchIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(address))));
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
            launchIntent(intent);
        } catch (ActivityNotFoundException ignored) {
            Log.w(TAG, "Nothing available to handle " + intent);
        }
    }

    final void webSearch(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra("query", query);
        launchIntent(intent);
    }

    /**
     * Like {@link #launchIntent(Intent)} but will tell you if it is not handle-able
     * via {@link ActivityNotFoundException}.
     *
     * @throws ActivityNotFoundException if Intent can't be handled
     */
    final void rawLaunchIntent(Intent intent) {
        if (intent != null) {
         //   intent.addFlags(Intents.FLAG_NEW_DOC);
            activity.startActivity(intent);
        }
    }

    /**
     * Like {@link #rawLaunchIntent(Intent)} but will show a user dialog if nothing is available to handle.
     */
    final void launchIntent(Intent intent) {
        try {
            rawLaunchIntent(intent);
        } catch (ActivityNotFoundException ignored) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//            builder.setTitle(R.string.app_name);
//            builder.setMessage(R.string.msg_intent_failed);
//            builder.setPositiveButton(R.string.button_ok, null);
            builder.show();
        }
    }

    private static void putExtra(Intent intent, String key, String value) {
        if (value != null && !value.isEmpty()) {
            intent.putExtra(key, value);
        }
    }

//    private String parseCustomSearchURL() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//        String customProductSearch = prefs.getString(PreferencesActivity.KEY_CUSTOM_PRODUCT_SEARCH,
//                null);
//        if (customProductSearch != null && customProductSearch.trim().isEmpty()) {
//            return null;
//        }
//        return customProductSearch;
//    }

//    final String fillInCustomSearchURL(String text) {
////        if (customProductSearch == null) {
////            return text; // ?
////        }
//        try {
//            text = URLEncoder.encode(text, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            // can't happen; UTF-8 is always supported. Continue, I guess, without encoding
//        }
//    //    String url = customProductSearch;
//        if (rawResult != null) {
//            // Replace %f but only if it doesn't seem to be a hex escape sequence. This remains
//            // problematic but avoids the more surprising problem of breaking escapes
//            url = url.replaceFirst("%f(?![0-9a-f])", rawResult.getBarcodeFormat().toString());
//            if (url.contains("%t")) {
//                ParsedResult parsedResultAgain = ResultParser.parseResult(rawResult);
//                url = url.replace("%t", parsedResultAgain.getType().toString());
//            }
//        }
//        // Replace %s last as it might contain itself %f or %t
//        return url.replace("%s", text);
//    }

    static String formatPhone(String phoneData) {
        // Just collect the call to a deprecated method in one place
        return PhoneNumberUtils.formatNumber(phoneData);
    }


//    public static ResultHandler makeResultHandler(CaptureActivity activity, Result rawResult) {
//        ParsedResult result = parseResult(rawResult);
//        switch (result.getType()) {
////            case ADDRESSBOOK:
////                return new AddressBookResultHandler(activity, result);
////            case EMAIL_ADDRESS:
////                return new EmailAddressResultHandler(activity, result);
////            case PRODUCT:
////                return new ProductResultHandler(activity, result, rawResult);
////            case URI:
////                return new URIResultHandler(activity, result);
//            case WIFI:
//                return new WifiResultHandler(activity, result);
//            case GEO:
//                return new GeoResultHandler(activity, result);
//            case TEL:
//                return new TelResultHandler(activity, result);
//            case SMS:
//                return new SMSResultHandler(activity, result);
//            case CALENDAR:
//                return new CalendarResultHandler(activity, result);
//            case ISBN:
//                return new ISBNResultHandler(activity, result, rawResult);
//            default:
//                return new TextResultHandler(activity, result, rawResult);
//        }
//    }

    private static ParsedResult parseResult(Result rawResult) {
        return ResultParser.parseResult(rawResult);
    }
}