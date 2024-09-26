package util;

import db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String SQL, Object... val) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(SQL);
        for (int i = 0; i < val.length; i++) {
            pstm.setObject(i + 1, val[i]);
        }
        if (SQL.startsWith("SELECT") || SQL.startsWith("select")) {
            if(val==null) {
                return (T) pstm.executeQuery();
            }else{

                return (T) pstm.executeQuery();
            }
        } else {
            for (int i = 0; i < val.length; i++) {
                pstm.setObject(i + 1, val[i]);
            }
            return (T) (Boolean) (pstm.executeUpdate() > 0);
        }
    }
}
