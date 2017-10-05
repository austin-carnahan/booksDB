
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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author raccoonmoonswoon
 */
public class InventoryServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String URL = "/Inventory.jsp";
        String bookid, storeid, msg = "", sql;
        
        //register jdbc driver
        try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch (Exception e){
                msg = "JDBC driver registration failure: " + e.getMessage();
            }
             
        //connect to db to get store info
        try{
            //get selected store from form
            bookid = request.getParameter("bookid");
            storeid = request.getParameter("storeid");
            //connection pool to datasource
            ConnectionPool pool = ConnectionPool.getInstance(); 
            Connection conn = pool.getConnection();
            
            Statement s = conn.createStatement();
            
            sql = "SELECT i.OnHand, l.title, l.author FROM " +
                    "booklist l, bookinv i WHERE i.bookID = '" + bookid + "'" +
                    " AND i.storeID = '" + storeid + "'";
            
            ResultSet r = s.executeQuery(sql);
            
            //build book obj
            if(r.next()){
                Book book = new Book();
                book.setBookid(bookid);
                book.setOnhand(r.getInt("OnHand"));
                book.setTitle(r.getString("title"));
                book.setAuthor(r.getString("author"));
               
                request.getSession().setAttribute("book", book);
                URL="/Update.jsp";
                
            }else{
                msg += "Error reading store records";
            }
            
            conn.close();
            
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
