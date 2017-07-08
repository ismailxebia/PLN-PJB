package xebia.ismail.plnpjb.sms;

/**
 * Created by acer on 6/13/2017.
 */

public class SmsData {

    //Number From witch the sms was send
    private String number;
    // SMS Text body
    private String body;

    public String getNumber(){
        return number;
    }

    public void setNumber (String number) {
        this.number = number;
    }

    public String getBody(){
        return body;
    }

    public void setBody (String body){
        this.body = body;
    }
}
