
package business;


public class Store {
    private int storeid, storeemp;
    private String storename, storeaddr;
    
    public Store(){
        storeid = 0;
        storeemp = 0;
        storename = "";
        storeaddr = "";
    }

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public int getStoreemp() {
        return storeemp;
    }

    public void setStoreemp(int storeemp) {
        this.storeemp = storeemp;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getStoreaddr() {
        return storeaddr;
    }

    public void setStoreaddr(String storeaddr) {
        this.storeaddr = storeaddr;
    }
}
