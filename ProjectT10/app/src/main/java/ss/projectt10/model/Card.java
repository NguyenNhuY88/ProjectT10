package ss.projectt10.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Card implements Serializable {
    private String ID;
    private String cardCode;
    private String cardName;
    private String cardFrontImage;
    private String cardBackImage;
    private String codeType;


    private String category;
    private String cardAvatar;
    private String note;

    private boolean isFavorite;
    private long useTime;
    private String useTimeID;
    public Card() {
    }

    public Card(String id, String cardCode, String cardName, String cardFrontImage, String cardBackImage, String codeType, String category, String cardAvatar, String note, boolean isFavorite) {
        this.ID = id;
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.cardFrontImage = cardFrontImage;
        this.cardBackImage = cardBackImage;
        this.codeType = codeType;
        this.category = category;
        this.cardAvatar = cardAvatar;
        this.note = note;
        this.isFavorite = isFavorite;
    }

    public String getUseTimeID() {
        return useTimeID;
    }

    public void setUseTimeID(String useTimeID) {
        this.useTimeID = useTimeID;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCardAvatar() {
        return cardAvatar;
    }

    public void setCardAvatar(String cardAvatar) {
        this.cardAvatar = cardAvatar;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", ID);
        result.put("cardCode", cardCode);
        result.put("cardName", cardName);
        result.put("cardFrontImage", cardFrontImage);
        result.put("cardBackImage", cardBackImage);
        result.put("codeType", codeType);
        result.put("isFavorite", isFavorite);
        result.put("category",category);
        result.put("cardAvatar",cardAvatar);
        result.put("note",note);
        return result;
    }
}
