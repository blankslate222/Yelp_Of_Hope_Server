package com.nikhil.connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDatabaseOperation {
	
public boolean insert(String tableName, HashMap<String, String> map);
public boolean update(String table, String idCol, String idVal, HashMap<String, String> updMap);
public boolean delete(String table, String keyCol, String keyVal);

public List<Map<String, Object>> selectAll(String table);
public List<Map<String, Object>> selectList(String table, String selectCol);
public List<Map<String, Object>> selectWhere(String table, String keyCol, String keyVal);

}
