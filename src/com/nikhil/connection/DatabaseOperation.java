package com.nikhil.connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class DatabaseOperation implements IDatabaseOperation {

	public boolean insert(String tableName, HashMap<String, String> insMap) {
		boolean result = false;
		int rowcount;
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = Pool.getConn();
			String insQry = "INSERT INTO " + tableName + " (";
			String valQry = "values (";
			String qQry = "values (";
			String comma = ",";
			for (Map.Entry<String, String> entry : insMap.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());

				insQry = insQry + entry.getKey() + comma;
				qQry += "?" + comma;
			}
			insQry = insQry.substring(0, insQry.length() - 1);
			qQry = qQry.substring(0, qQry.length() - 1);
			insQry = insQry + ")";
			qQry = qQry + ");";
			String variableQuery = insQry + " " + qQry;
			stmt = conn.prepareStatement(variableQuery);
			int i = 1;
			for (Map.Entry<String, String> entry : insMap.entrySet()) {

				valQry = valQry + entry.getValue() + comma;
				stmt.setString(i, entry.getValue());
				i++;
			}

			valQry = valQry.substring(0, valQry.length() - 1);

			valQry = valQry + ");";

			String sanitizedQuery = insQry + " " + valQry;

			System.out.println("Sanitized qry -- " + sanitizedQuery);
			System.out.println("Q qry -- " + variableQuery);
			System.out.println("Final stmt exec -- " + stmt);
			// //////////////////////////////////////////////////////////

			rowcount = stmt.executeUpdate();
			if (rowcount > 0) {
				result = true;
				System.out.println("Insert Successful");
				return result;
			} else {
				result = false;
				return result;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("SQL error");
			e.printStackTrace();
		} finally {
			try {
				Pool.returnConn(conn);
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("SQL error");
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<Map<String, Object>> selectAll(String table) {
		// ///////////////////////////////////////////////////////////////
		// select stmt

		Connection conn = null;
		Map<String, Object> row = null;
		List<Map<String, Object>> resultList = null;
		PreparedStatement s1 = null;
		try {
			String selectAll = "SELECT * FROM " + table + " ORDER BY 1 DESC";
			conn = Pool.getConn();
			s1 = conn.prepareStatement(selectAll);

			System.out.println("Query --" + s1);
			ResultSet result = s1.executeQuery();
			resultList = new ArrayList<Map<String, Object>>();

			ResultSetMetaData metaData = result.getMetaData();
			Integer columnCount = metaData.getColumnCount();

			while (result.next()) {
				row = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					row.put(metaData.getColumnName(i), result.getObject(i));
				}
				resultList.add(row);
			}
			// System.out.println("ROW - " + row);
			// System.out.println("Ele - " + row.get("username"));
			// System.out.println("Entire list - " + resultList);
			return resultList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Pool.returnConn(conn);
				s1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultList;

	}

	public List<Map<String, Object>> selectWhere(String table, String keyCol,
			String keyVal) {
		// ///////////////////////////////////////////////////////////////
		// select stmt
		Connection conn = null;
		Map<String, Object> row = null;
		List<Map<String, Object>> resultList = null;
		ResultSet result = null;
		PreparedStatement s1 = null;

		try {

			conn = Pool.getConn();
			String selectWhere = "SELECT * FROM " + table + " WHERE " + keyCol
					+ " = ? ORDER BY 1 DESC";
			s1 = conn.prepareStatement(selectWhere);

			s1.setString(1, keyVal);

			System.out.println("Query --" + s1);
			result = s1.executeQuery();
			resultList = new ArrayList<Map<String, Object>>();
			System.out.println("result --" + result);

			ResultSetMetaData metaData = result.getMetaData();
			Integer columnCount = metaData.getColumnCount();
			// System.out.println("meta --" +
			// metaData);System.out.println("col count --"+ columnCount);

			while (result.next()) {
				row = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					row.put(metaData.getColumnName(i), result.getObject(i));
					System.out.println(metaData.getColumnName(i));
					System.out.println(result.getObject(i));
				}
				resultList.add(row);
				// System.out.println("ROW - " + row);
				// System.out.println("Ele - " + row.get("username"));
				// System.out.println("Entire list - " + resultList);
			}

			return resultList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Pool.returnConn(conn);
				s1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultList;

	}

	public List<Map<String, Object>> selectList(String table, String selectCol) {
		// ///////////////////////////////////////////////////////////////
		// select stmt

		Connection conn = null;
		PreparedStatement s1 = null;
		ResultSet result = null;
		List<Map<String, Object>> resultList = null;
		Map<String, Object> row = null;
		try {
			conn = Pool.getConn();
			String selectList = "SELECT DISTINCT " + selectCol + " FROM "
					+ table + " ORDER BY 1 DESC";
			s1 = conn.prepareStatement(selectList);
			System.out.println("Query --" + s1);
			result = s1.executeQuery();
			resultList = new ArrayList<Map<String, Object>>();

			ResultSetMetaData metaData = result.getMetaData();
			Integer columnCount = metaData.getColumnCount();

			while (result.next()) {
				row = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					row.put(metaData.getColumnName(i), result.getObject(i));
				}
				resultList.add(row);
			}
			// System.out.println("ROW - " + row);
			// System.out.println("Ele - " + row.get("username"));
			// System.out.println("Entire list - " + resultList);
			return resultList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Pool.returnConn(conn);
				s1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultList;

	}

	public boolean update(String table, String idCol, String idVal,
			HashMap<String, String> updMap) {
		String sql1 = "UPDATE " + table + " SET ";
		String comma = ",";
		PreparedStatement s1 = null;
		Connection conn = null;
		try {
			conn = Pool.getConn();
			for (Map.Entry<String, String> entry : updMap.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());

				sql1 += entry.getKey() + "= ?" + comma;

			}
			sql1 = sql1.substring(0, sql1.length() - 1);
			sql1 += " WHERE " + idCol + " = ?";
			System.out.println("Upd q query --" + sql1);

			s1 = conn.prepareStatement(sql1);
			int i = 1;

			for (Map.Entry<String, String> entry : updMap.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());

				s1.setString(i, entry.getValue());
				i++;

			}
			s1.setString(i, idVal);
			System.out.println("Prepared stmt -- " + s1);
			int updated = s1.executeUpdate();
			if (updated > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				Pool.returnConn(conn);
				s1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean delete(String table, String keyCol, String keyVal) {
		Connection conn = null;
		PreparedStatement s1 = null;
		try {
			conn = Pool.getConn();
			String sql = "DELETE FROM " + table + " WHERE " + keyCol + " = ?";
			s1 = conn.prepareStatement(sql);
			s1.setString(1, keyVal);
			System.out.println(s1);
			int res = s1.executeUpdate();
			if (res > 0) {
				System.out.println(res + "rows");
				return true;
			}
		} catch (MySQLIntegrityConstraintViolationException m) {

			m.printStackTrace();
			try {
				throw new Exception("My SQL integrity constraint violation");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Pool.returnConn(conn);
				s1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
