
package servlets;

import business.ConnectionPool;
import business.Store;
import business.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogonServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        User u;
        int userid = 0; 
        int passattempt;
        boolean authorized = false;
        String URL = "/Logon.jsp";
        String sql, msg ="";
        ArrayList<Store> stores = new ArrayList<>();
             
        
        //register jdbc driver
        try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch (Exception e){
                msg = "JDBC driver registration failure: " + e.getMessage();
            }
             
        //connect to db to get store info
        try{
            //get login info from form
            userid = Integer.parseInt(request.getParameter("userid").trim());
            passattempt = Integer.parseInt(request.getParameter("password").trim());
            
            //connection pool to datasource
            ConnectionPool pool = ConnectionPool.getInstance(); 
            Connection conn = pool.getConnection();
            
            //****************** USER *********************************************
            //validate login credentials and query db to build a user obj
            Statement s1 = conn.createStatement();
            
            sql = "SELECT * FROM users WHERE userID ='"+ userid + "'";
            
            ResultSet r1 = s1.executeQuery(sql);
            
            if(r1.next()){
                u = new User();
                u.setUserid(userid);
                u.setPwdattempt(passattempt);
                u.setPassword(r1.getInt("userPassword"));
                if(u.isAuthenticated()){
                    msg = "User Logon Authenticated";
                    authorized = true;
                    u.setUsername(r1.getString("userName"));
                    u.setStoreid(r1.getInt("storeID"));
                    u.setAdminlevel(r1.getString("adminLevel"));
                    
                    //move to next screen
                    URL = "/StoreSelection.jsp";
                    
                }else{
                    msg = "Failed to Authenticate";
                }//end authentication check
                //if we found a record, add it to the session
                request.getSession().setAttribute("u", u);
            }else{
                //no record found
                msg = "User not found";
            }//end check for record load
            
            //****************** STORES ************************************
            if(authorized){
                //create a sql statement
                Statement s2 = conn.createStatement();

                sql = "SELECT * FROM stores ORDER BY storeID";

                ResultSet r2 = s2.executeQuery(sql);

                while(r2.next()){
                    Store st = new Store();
                    st.setStoreid(r2.getInt("StoreID"));
                    st.setStorename(r2.getString("StoreName"));
                    st.setStoreaddr(r2.getString("StoreAddr"));
                    st.setStoreemp(r2.getInt("StoreEmp"));
                    stores.add(st);
                }

                if(stores.size() > 0){
                    request.getSession().setAttribute("stores", stores);
                }else{
                    msg += "No Stores read from table<br>";
                }
            }
            
            conn.close();

        }catch(SQLException e){
            msg += "SQL Exception:" + e.getMessage() + "<br>";
        }catch(Exception e){
            msg += "Error: " + e.getMessage() + "<br>";
        }

         //build a cookie to store user id
        String useridS = Integer.toString(userid); 
        Cookie uid = new Cookie("userid", useridS);
        uid.setMaxAge(60*10);
        uid.setPath("/");
        response.addCookie(uid);
        
        request.setAttribute("msg", msg);
        //dispatch request
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
