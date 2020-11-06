import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import junit.framework.TestCase;

public class AcceptOrderBeanTest extends TestCase {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost/test";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	public AcceptOrderBeanTest(String name) {
		super(name);
	}

	static {

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private File file;

	private Connection getConnection() throws Exception {
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		return connection;
	}

	/*
	 * �ƥ��Ȥν�����Ԥ���
	 * Accept_Order�ơ��֥�Υǡ����ΥХå����åפ��롣
	 * ���θ塢���ƤΥǡ������������ƥ��ȥǡ������������롣
	 */
	@SuppressWarnings("deprecation")
	protected void setUp() {
		IDatabaseConnection connection = null;
		try {
			super.setUp();
			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//�����ΥХå����åפ����
			QueryDataSet partialDataSet = new QueryDataSet(connection);
			partialDataSet.addTable("ACCEPT_ORDER");
			file = File.createTempFile("accept", ".xml");
			FlatXmlDataSet.write(partialDataSet,
					new FileOutputStream(file));

			//�ƥ��ȥǡ�������������
			IDataSet dataSet = new FlatXmlDataSet(new File("accept_order_test_data.xml"));
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	/*
	 * �ƥ��Ȥθ夫���Ť���Ԥ���
	 * Accept_Order�ơ��֥�ΥХå����åץǡ�����ꥹ�ȥ����롣
	 */
	protected void tearDown() throws Exception {

		IDatabaseConnection connection = null;
		try {
			super.tearDown();
			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			IDataSet dataSet = new FlatXmlDataSet(file);
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}

	}

	public void testLoadByO_num() throws Exception {
		//loadByO_num�᥽�åɤ�¹�
		AcceptOrderBean order = new AcceptOrderBean();
		order.loadByO_num("5001");

		//�¹Է�̤��ǧ

		//��������AcceptOrderBean������ϡ�ʸ����פ���Ӥ��롣

		assertEquals("accept_date=2004-01-01", "accept_date=" + order.getAccept_date());
		assertEquals("c_num=1000", "c_num=" + order.getC_num());
		assertEquals("dc_rate=5", "dc_rate=" + order.getDc_rate());
		assertEquals("employee=101", "employee=" + order.getEmployee());
		assertEquals("o_num=5001", "o_num=" + order.getO_num());
		assertEquals("option_price=30", "option_price=" + order.getOption_price());
		assertEquals("p_num=501", "p_num=" + order.getP_num());

	}

	public void testStore() throws Exception {

		//store�᥽�åɤμ¹�
		AcceptOrderBean order = new AcceptOrderBean();
		order.setO_num("5004");
		order.setC_num("1004");
		order.setP_num("501");
		order.setDc_rate(20);
		order.setOption_price(400);
		order.setEmployee("101");
		order.setAccept_date(Date.valueOf("2004-01-03"));

		order.store();

		//�¹Է�̤򸡾ڤ���
		IDatabaseConnection connection = null;
		try {
			// accept_order�ơ��֥�ξ��֤��ǧ
			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("accept_order");

			// ���Ԥ����ǡ��������
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("accept_order_test_data2.xml"));
			ITable expectedTable = expectedDataSet.getTable("accept_order");

			// ��Ӥ���
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	public void testDelete() throws Exception {

		//delete�᥽�åɤ�¹�
		AcceptOrderBean order = new AcceptOrderBean();
		order.setO_num("5003");
		order.delete();

		//�¹Է�̤��ǧ
		IDatabaseConnection connection = null;
		try {
			// accept_order�ơ��֥�ξ��֤��ǧ
			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("accept_order");

			// ����٤��Ѥ��ǧ
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("accept_order_test_data3.xml"));
			ITable expectedTable = expectedDataSet.getTable("accept_order");

			//��Ӥ���
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}
	}

}
