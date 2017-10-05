
package servlets;

import business.Book;
import business.ConnectionPool;
import business.Store;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class StoreSelectionServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String URL = "/StoreSelection.jsp";
        String storeid;
        String sql, msg="";
        ArrayList<Book> books = new ArrayList<>();
        String action = "";
        
        
        //register jdbc driver
        try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch (Exception e){
                msg = "JDBC driver registration failure: " + e.getMessage();
            }
             
        //connect to db to get store info
        try{
            //get selected store from form
            storeid = request.getParameter("storeid").trim();
            action = request.getParameter("actiontype");
            
            //connection pool to datasource
            ConnectionPool pool = ConnectionPool.getInstance(); 
            Connection conn = pool.getConnection();
            
            
            //******************* BOOKS *************************************
            
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
                
                //success. We can move to the next page   
                //URL="/Inventory.jsp";
            }else{
                msg += "No Books read from table<br>";
            }
            
            //***************** STORE ********************************
            
            Statement s2 = conn.createStatement();
            
            sql = "SELECT * FROM stores WHERE storeID = '" + storeid + "'";
            
            ResultSet r2 = s2.executeQuery(sql);
            
            //build store obj
            if(r2.next()){
                Store store = new Store();
                store.setStoreaddr(r2.getString("storeAddr"));
                store.setStoreid(Integer.parseInt(storeid));
                store.setStorename(r2.getString("storeName"));
               
                request.getSession().setAttribute("s", store);
                URL="/Inventory.jsp";
                
            }else{
                msg += "Error reading store records";
            }
             
            
            //cant get this to work...supposed to be part of AJAX on StoreSelection.jsp
//            if(msg.isEmpty() && action.equalsIgnoreCase("inventory")){
//                URL = "/Inventory.jsp";          
//            }
              
            conn.close();
            
        }catch(SQLException e){
            msg += "SQL Exception:" + e.getMessage() + "<br>";
        }catch(Exception e){
            msg += "Error: " + e.getMessage() + "<br>";
        }
//        //for quick testing
//        request.getSession().setAttribute("sid", storeid);    
//        URL = "/Inventory.jsp";

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
