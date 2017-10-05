
package servlets;

import business.Book;
import business.ConnectionPool;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UpdateServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String URL = "/Update.jsp";
        String action, msg = "", sql;
        String qty, storeid, bookid;
        int testqty;
        ArrayList<Book> books = new ArrayList<>();
        
        //connect to db to get store info
        try{
            //get selected store from form
            action = request.getParameter("actiontype");
            qty = request.getParameter("qty");
            storeid = request.getParameter("storeid");
            bookid = request.getParameter("bookid");
            
            //make sure we get a good value
            try{
                testqty = Integer.parseInt(qty);
            }catch(Exception e){
                msg += "Please enter an integer: " + e.getMessage();
            }
            
            if(action.equalsIgnoreCase("update")){
                
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                }catch (Exception e){
                    msg = "JDBC driver registration failure: " + e.getMessage() + "<br>";
                }
                
                //connection pool to datasource
                ConnectionPool pool = ConnectionPool.getInstance(); 
                Connection conn = pool.getConnection();
                
                sql = "UPDATE bookinv SET " +
                        "OnHand = ? "+
                        "WHERE bookID = '" + bookid + "' AND " +
                        "storeid = '" + storeid + "'";
                
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, qty);
                
                int rc = ps.executeUpdate();
                if(rc == 0){
                    msg += "Update Failed. No Changes<br>";
                }else if (rc == 1){
                    msg += "Inventory Updated Successfuly<br>";
                 //************** rebuild session objects *******************  
                    
                    //rebuild book obj
                    Statement s2 = conn.createStatement();
            
                    sql = "SELECT i.OnHand, l.title, l.author FROM " +
                    "booklist l, bookinv i WHERE i.bookID = '" + bookid + "'" +
                    " AND i.storeID = '" + storeid + "'";
            
                    ResultSet r2 = s2.executeQuery(sql);
                    if(r2.next()){
                        Book book = new Book();
                        book.setBookid(bookid);
                        book.setOnhand(r2.getInt("OnHand"));
                        book.setTitle(r2.getString("title"));
                        book.setAuthor(r2.getString("author"));

                        request.getSession().setAttribute("book", book);
                       
                    
                    //rebuild books array/
                    Statement s1 = conn.createStatement();
             
                    sql = "SELECT i.bookID, i.onHand, l.title, l.author, l.price "+
                            "FROM booklist l, bookinv i " +
                            "WHERE i.bookID = l.bookID " +
                            "AND storeID='"+ storeid + "'"+
                            " ORDER BY bookID";

                    ResultSet r1 = s1.executeQuery(sql);

                    //build book objs and add them to books arraylist
                    while(r1.next()){
                        Book bk = new Book();
                        bk.setBookid(r1.getString("bookID"));
                        bk.setTitle(r1.getString("title"));
                        bk.setAuthor(r1.getString("author"));
                        bk.setOnhand(r1.getInt("onHand"));
                        bk.setPrice(r1.getInt("price"));
                        books.add(bk);
                    }

                    //make sure we have results before posting to session
                    if(books.size() > 0){
                        request.getSession().setAttribute("books", books);
                        URL="/Update.jsp";

                    }else{
                        msg += "Error rebuilding books array<br>";
                    }      
                        
                  //***********************************************************      
                
            }else{
                msg += "Error reading store records";
            }
                }else{
                    msg += "Fatal Error " + rc + "records were changed<br>";
                }
                
                conn.close();
                
            }else if(action.equalsIgnoreCase("cancel")){
                URL = "/Inventory.jsp";
            }else{
                msg+= "Error: No actiontype received from form";
            }
            
        }catch(SQLException e){
            msg += "SQL Exception:" + e.getMessage() + "<br>";
        }catch(Exception e){
            msg += "Error: " + e.getMessage() + "<br>";
        }        
        
        request.setAttribute("msg", msg);
        
        RequestDispatcher disp = getServletContext().getRequestDispatcher(URL);
        disp.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
