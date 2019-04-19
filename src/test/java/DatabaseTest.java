import model.Database;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DatabaseTest {

    @Test
    public void connectionTest() throws Exception {
        Exception ex = null;
        Database db = new Database();
        try {
            db.connectToDB("test1.db");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ex = e;
        }
        assertNull(ex);

        try {
            db.connectToDB("test1.db");
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        db.closeConnection();
    }

    @Test
    public void closeConnectionTest() {
        Exception ex = null;
        Database db = new Database();
        try {
            db.closeConnection(); // close any opened connection before testing
            db.connectToDB("Test1.db"); // shouldn't give exception
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            ex = e;
        }
        assertNull(ex);
        db.closeConnection();
    }

    @Test
    public void createTableTest() throws Exception {
        Exception ex = null;
        Database db = new Database();
        db.closeConnection();
        db.connectToDB("Test1.db");
        try {
            db.createRestaurantTables();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
        db.closeConnection();
    }

    @Test
    public void exploreDB() throws Exception {
        Exception ex = null;
        Database db = new Database();
        db.closeConnection();
        db.connectToDB("RESTAURANT.db");
        try {
            ResultSet set = db.executeQuery("SELECT * FROM dish;");

            while (set.next()) {
                for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
                    System.out.println(set.getString(i));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
        db.closeConnection();
    }

    @Test
    public void executeTableTest() throws Exception {
        Exception ex = null;
        Database db = new Database();
        try {
            db.closeConnection();
            db.execute("select * from tables");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        db.connectToDB("Test1.db");
        db.createRestaurantTables();
        try {
            db.execute("insert into tables values(4);");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);

        ex = null;
        try {
            db.execute("select * from tables;");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        try {
            db.execute("bad command");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ex = e;
        }
        assertNotNull(ex);
        db.closeConnection();
    }


    @Test
    public void executeQueryTableTest() throws Exception {
        Exception ex = null;
        Database db = new Database();
        try {
            db.closeConnection();
            db.executeQuery("select * from tables;");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        db.connectToDB("Test1.db");
        db.createRestaurantTables();

        try {
            db.executeQuery("insert into tables values(3);");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        try {
            db.executeQuery("select * from tables;");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ex = e;
        }
        assertNull(ex);

        ex = null;
        try {
            db.executeQuery("bad command");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ex = e;
        }
        assertNotNull(ex);
        db.closeConnection();
    }
}
