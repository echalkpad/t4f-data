package io.datalayer.sql.dao.dao2.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbutils.QueryRunner;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

class transaction implements Runnable {

    @Override
    public void run() {
        while (true) {
            Connection conn = null;
            try {
                conn = NoteDao.run.getDataSource().getConnection();
                conn.setAutoCommit(false);
                try {
                    TimeUnit.SECONDS.sleep(100);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
                NoteDao.run.update(conn, "update note set  description='123' where noteid = ?", 1);
                NoteDao.run.update(conn, "update note set  description='0' where noteid = ?", 1);
                try {
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {

            }
        }
    }

}

public class NoteDao {
    public static QueryRunner run = new QueryRunner(new __DataSource());

    static class __DataSource extends MysqlDataSource {
        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public __DataSource() {
            this.databaseName = "xhq";
            this.encoding = "utf-8";
            this.hostName = "192.168.8.220";
            this.user = "root";
            this.password = "123456";
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null; // To change body of implemented methods use File |
                         // Settings | File Templates.
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false; // To change body of implemented methods use File |
                          // Settings | File Templates.
        }
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        new Thread(new transaction()).start();
    }
}
