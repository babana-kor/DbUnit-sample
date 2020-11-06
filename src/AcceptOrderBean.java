import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AcceptOrderBean {

	private String o_num;
	private String c_num;
	private String p_num;
	private int dc_rate;
	private int option_price;
	private String employee;
	private Date accept_date;

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost/test";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	public String getO_num() {
		return o_num;
	}

	public String getC_num() {
		return c_num;
	}

	public String getP_num() {
		return p_num;
	}

	public int getDc_rate() {
		return dc_rate;
	}

	public int getOption_price() {
		return option_price;
	}

	public String getEmployee() {
		return employee;
	}

	public Date getAccept_date() {
		return accept_date;
	}

	public void setO_num(String o_num) {
		this.o_num = o_num;
	}

	public void setC_num(String c_num) {
		this.c_num = c_num;
	}

	public void setP_num(String p_num) {
		this.p_num = p_num;
	}

	public void setDc_rate(int dc_rate) {
		this.dc_rate = dc_rate;
	}

	public void setOption_price(int option_price) {
		this.option_price = option_price;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public void setAccept_date(Date date) {
		this.accept_date = date;
	}

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		return connection;
	}

	public void loadByO_num(String o_num) throws Exception {

		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "select * from Accept_Order where o_num=?";

			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, o_num);
			ResultSet result = stat.executeQuery();

			if (result.next()) {
				this.o_num = o_num;
				this.c_num = result.getString("c_num");
				this.p_num = result.getString("p_num");
				this.dc_rate = result.getInt("dc_rate");
				this.option_price = result.getInt("option_price");
				this.employee = result.getString("employee");
				this.accept_date = result.getDate("accept_date");
			}

		} finally {
			if (connection != null)
				connection.close();
		}

	}

	public static AcceptOrderBean[] findByEmployee(String employee)
			throws Exception {

		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "select * from Accept_Order where employee=?";

			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, employee);
			ResultSet result = stat.executeQuery();
			ArrayList list = new ArrayList();

			while (result.next()) {
				AcceptOrderBean accept = new AcceptOrderBean();
				accept.o_num = result.getString("o_num");
				accept.c_num = result.getString("c_num");
				accept.p_num = result.getString("p_num");
				accept.dc_rate = result.getInt("dc_rate");
				accept.option_price = result.getInt("option_price");
				accept.employee = result.getString("employee");
				accept.accept_date = result.getDate("accept_date");
				list.add(accept);
			}

			return (AcceptOrderBean[]) list.toArray(new AcceptOrderBean[list.size()]);

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}

	}

	public void store() throws Exception {

		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "insert into Accept_Order values(?,?,?,?,?,?,?)";

			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, this.o_num);
			stat.setString(2, this.c_num);
			stat.setString(3, this.p_num);
			stat.setInt(4, this.dc_rate);
			stat.setInt(5, this.option_price);
			stat.setString(6, this.employee);
			stat.setDate(7, this.accept_date);
			stat.executeUpdate();

			connection.commit();

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}

	}

	public void delete() throws Exception {

		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "delete from Accept_Order where o_num=?";

			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, this.o_num);
			stat.executeUpdate();

			connection.commit();

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}

	}

}
