package net.foldingcoin.fldcbot.util.fldc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FLDCUser {

    @Expose()
    @SerializedName("id")
    private long id;

    @Expose()
    @SerializedName("name")
    private String name;

    @Expose()
    @SerializedName("token")
    private String token;

    @Expose()
    @SerializedName("address")
    private String address;

    @Expose()
    @SerializedName("newcredit")
    private long newCredit;

    public FLDCUser () {

    }

    public FLDCUser (long id, String name, String token, String address, long newCredit) {

        this.id = id;
        this.name = name;
        this.token = token;
        this.address = address;
        this.newCredit = newCredit;
    }

    public long getId () {

        return this.id;
    }

    public String getName () {

        return this.name;
    }

    public String getToken () {

        return this.token;
    }

    public String getAddress () {

        return this.address;
    }

    public long getNewCredit () {

        return this.newCredit;
    }

    public void setId (long id) {

        this.id = id;
    }

    public void setName (String name) {

        this.name = name;
    }

    public void setToken (String token) {

        this.token = token;
    }

    public void setAddress (String address) {

        this.address = address;
    }

    public void setNewCredit (long newcredit) {

        this.newCredit = newcredit;
    }

    @Override
    public String toString () {

        final StringBuilder sb = new StringBuilder("FLDCUser{");
        sb.append("id=").append(this.id);
        sb.append(", name='").append(this.name).append('\'');
        sb.append(", token='").append(this.token).append('\'');
        sb.append(", address='").append(this.address).append('\'');
        sb.append(", newCredit=").append(this.newCredit);
        sb.append('}');
        return sb.toString();
    }
}
