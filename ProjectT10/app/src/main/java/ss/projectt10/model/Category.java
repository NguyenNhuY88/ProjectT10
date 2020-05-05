package ss.projectt10.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String nameCard;
    private String imageIcon;
    private String cardType;
    private Object location;

//    public Object getLocation() {
//        return location;
//    }
//
//    public void setLocation(Object location) {
//        this.location = location;
//    }
    //     List<String> location;
//    public List<String> getLocation() {
//        return location;
//    }
//
//    public void setLocation(List<String> location) {
//        this.location = location;
//    }



    public Category() {
    }

    public Category(String nameCard, String imageIcon, String cardType) {
        this.nameCard = nameCard;
        this.imageIcon = imageIcon;
        this.cardType = cardType;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
