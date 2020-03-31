package com.gregoiredf.knowYourGovernment;

import android.util.Pair;

import java.io.Serializable;
import java.util.HashMap;

class Office implements Serializable
{
    private String officeName, officialName, imageURL;
    private String address, party, phone, url, email;
    private HashMap<String, String> channels = new HashMap<>();

    public Office(){}



    public Office(String officeName, String officialName, String imageURL)
    {
        this.officeName = officeName;
        this.officialName = officialName;
        this.imageURL = imageURL;
    }

    public String getOfficeName() {return officeName;}
    public void setOfficeName(String officeName) {this.officeName = officeName;}

    public String getOfficialName() {return officialName;}
    public void setOfficialName(String officialName) {this.officialName = officialName;}

    public String getImageURL() {return imageURL;}
    public void setImageURL(String imageURL) {this.imageURL = imageURL;}


    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setParty(String party) {
        this.party = party;
    }
    public String getParty() {
        return party;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void addChannelPair(String key, String value){channels.put(key, value);}
    public HashMap<String,String> getChannels() {return channels;}
}
