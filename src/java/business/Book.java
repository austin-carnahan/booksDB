
package business;

import java.text.NumberFormat;


public class Book {
    private int onhand;
    private int price;
    private String title;
    private String bookid;
    private String author;
    
    NumberFormat curr = NumberFormat.getCurrencyInstance();
    
    public Book(){
        onhand=0;
        price = 0;
        title="";
        bookid="";
        author="";
    }

    public int getOnhand() {
        return onhand;
    }

    public void setOnhand(int onhand) {
        this.onhand = onhand;
    }

    public int getPrice() {
        return price;
    }
    
    public String getFormattedPrice(){
        return curr.format(this.price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    
}
