package parser.idParser.controller;

public class UserData {
    private String cnp;
    private String lastName;
    private String firstName;
    private String address;
    private float cnpConf;
    private float lastNameConf;
    private float firstNameConf;
    private float addressConf;
    private DocumentData documentData;


    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnpConf(float cnpConf) {
        this.cnpConf = cnpConf;
    }

    public float getCnpConf() {
        return cnpConf;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstNameConf(float firstNameConf) {
        this.firstNameConf = firstNameConf;
    }

    public float getFirstNameConf() {
        return firstNameConf;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastNameConf(float lastNameConf) {
        this.lastNameConf = lastNameConf;
    }

    public float getLastNameConf() {
        return lastNameConf;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddressConf(float addressConf) {
        this.addressConf = addressConf;
    }

    public float getAddressConf() {
        return addressConf;
    }

    //Todo:: this might be removed after debuging.
    public DocumentData getDocumentData() {
        return documentData;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }
}
