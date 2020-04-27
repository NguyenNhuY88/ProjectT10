package ss.projectt10.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyCard implements Serializable {
    private String cardCode;
    private String cardName;
    private String cardFrontImage;
    private String cardBackImage;
    private String codeType;
    private boolean isFavorite;
    public MyCard() {
    }

    public MyCard(String cardCode, String cardName, String cardFrontImage, String cardBackImage, String codeType, boolean isFavorite) {
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.cardFrontImage = cardFrontImage;
        this.cardBackImage = cardBackImage;
        this.codeType = codeType;
        this.isFavorite = isFavorite;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardFrontImage() {
        return cardFrontImage;
    }

    public void setCardFrontImage(String cardFrontImage) {
        this.cardFrontImage = cardFrontImage;
    }

    public String getCardBackImage() {
        return cardBackImage;
    }

    public void setCardBackImage(String cardBackImage) {
        this.cardBackImage = cardBackImage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cardCode", cardCode);
        result.put("cardName", cardName);
        result.put("cardFrontImage", cardFrontImage);
        result.put("cardBackImage", cardBackImage);
        result.put("codeType", codeType);
        result.put("isFavorite", isFavorite);

        return result;
    }
}
