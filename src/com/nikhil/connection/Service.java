package com.nikhil.connection;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.nikhil.bean.*;

@SuppressWarnings("unused")
@WebService
public class Service {

	public SignInBean signIn(String user, String password) {

		String table = null, keyCol = null, keyVal = null, cur_date = null;
		String errMsg = null;
		DatabaseOperation dbo = null;
		HashMap<String, String> map = null;
		Format formatter = null;
		Boolean time_upd = false;
		List<Map<String, Object>> result = null;
		SignInBean sb = null;
		try {
			table = "user";
			keyCol = "user_email";
			keyVal = user;
			dbo = new DatabaseOperation();
			sb = new SignInBean();
			if (user.trim().equals("") || password.trim().equals("")) {
				errMsg = "User/password field cannot be empty";
				sb.setResult("false");
				sb.setError(errMsg);
				sb.setPrivilege("0");
				return sb;
			}

			result = dbo.selectWhere(table, keyCol, keyVal);

			if (result.isEmpty()) {
				System.out.println("Login failed");
				errMsg = "Incorrect email or password";
				sb.setResult("false");
				sb.setError(errMsg);
				sb.setPrivilege("0");
				return sb;
			}

			if (result.get(0).get("user_password").equals(password)) {

				System.out.println("Logged in as - " + user);

				formatter = new SimpleDateFormat("dd-MMM-yyyy");
				cur_date = formatter.format(new Date());
				map = new HashMap<String, String>();
				map.put("last_login", cur_date);
				time_upd = dbo.update(table, keyCol, keyVal, map);
				sb.setResult("true");
				sb.setUser_id((String) result.get(0).get("user_email"));
				sb.setLast_login((String) result.get(0).get("last_login"));
				System.out.println("service privilege"
						+ result.get(0).get("user_privilege"));
				String priv = (String) result.get(0).get("user_privilege");
				sb.setPrivilege(priv);
				return sb;
			} else {
				System.out.println("Login failed -- " + user);
				sb.setResult("false");
				sb.setError("Incorrect email or password");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;

	}

	public CategoryBean[] getCategory() {
		CategoryBean cat = null;
		DatabaseOperation dbo = null;
		String table = null;
		ArrayList<CategoryBean> clist = null;
		try {
			clist = new ArrayList<CategoryBean>();
			dbo = new DatabaseOperation();
			table = "category";
			List<Map<String, Object>> result = dbo.selectAll(table);
			for (int i = 0; i < result.size(); i++) {
				cat = new CategoryBean();
				cat.setId(Integer.toString((int) result.get(i).get(
						"category_id")));
				cat.setName((String) result.get(i).get("category_name"));
				clist.add(cat);
			}
			CategoryBean[] carray = new CategoryBean[clist.size()];
			clist.toArray(carray);
			System.out.println("list array --" + carray + "\n" + "list size --"
					+ clist.size());
			return carray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ElementBean[] getElement() {
		ElementBean ele = null;
		DatabaseOperation dbo = null;
		String table = null;
		ArrayList<ElementBean> elist = null;
		try {
			elist = new ArrayList<ElementBean>();
			dbo = new DatabaseOperation();
			table = "element";
			List<Map<String, Object>> result = dbo.selectAll(table);
			for (int i = 0; i < result.size(); i++) {
				ele = new ElementBean();
				ele.setId(Integer.toString((int) result.get(i)
						.get("element_id")));
				ele.setName((String) result.get(i).get("element_name"));
				elist.add(ele);
			}
			ElementBean[] earray = new ElementBean[elist.size()];
			elist.toArray(earray);
			return earray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public ReviewBean[] getReview() {
		ReviewBean review = null;
		DatabaseOperation dbo = null;
		String table = null;
		ArrayList<ReviewBean> rlist = null;
		try {
			rlist = new ArrayList<ReviewBean>();
			dbo = new DatabaseOperation();
			table = "review";
			List<Map<String, Object>> result = dbo.selectAll(table);
			for (int i = 0; i < result.size(); i++) {
				review = new ReviewBean();
				review.setId(Integer.toString((int) result.get(i).get(
						"review_id")));
				review.setContent((String) result.get(i).get("review_content"));
				review.setRating(Integer.toString((int) result.get(i).get(
						"review_rating")));
				review.setUser((String) result.get(i).get("review_user"));
				review.setElement((String) result.get(i).get("review_ele"));
				review.setDate((String) result.get(i).get("review_date"));
				rlist.add(review);
			}
			ReviewBean[] rarray = new ReviewBean[rlist.size()];
			rlist.toArray(rarray);
			return rarray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ReviewBean[] getUserReview(String keyCol, String keyVal) {
		ReviewBean review = null;
		DatabaseOperation dbo = null;
		String table = null;
		ArrayList<ReviewBean> rlist = null;
		try {
			rlist = new ArrayList<ReviewBean>();
			dbo = new DatabaseOperation();
			table = "review";
			List<Map<String, Object>> result = dbo.selectWhere(table, keyCol,
					keyVal);
			for (int i = 0; i < result.size(); i++) {
				review = new ReviewBean();
				review.setId(Integer.toString((int) result.get(i).get(
						"review_id")));
				review.setContent((String) result.get(i).get("review_content"));
				review.setRating(Integer.toString((int) result.get(i).get(
						"review_rating")));
				review.setUser((String) result.get(i).get("review_user"));
				review.setElement((String) result.get(i).get("review_ele"));
				review.setDate((String) result.get(i).get("review_date"));
				rlist.add(review);
			}

			ReviewBean[] rarray = new ReviewBean[rlist.size()];
			rlist.toArray(rarray);
			return rarray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CategoryBean[] getCategoryDetails(String keyCol, String keyVal) {
		CategoryBean cat = null;
		DatabaseOperation dbo = null;
		String table = null;
		ArrayList<CategoryBean> clist = null;
		try {
			clist = new ArrayList<CategoryBean>();
			dbo = new DatabaseOperation();
			table = "category";

			List<Map<String, Object>> result = dbo.selectWhere(table, keyCol,
					keyVal);
			System.out.println("RESULT size --" + result.size());

			for (int i = 0; i < result.size(); i++) {
				cat = new CategoryBean();
				cat.setId(Integer.toString((int) result.get(i).get(
						"category_id")));
				cat.setName((String) result.get(i).get("category_name"));
				cat.setDescription((String) result.get(i).get("category_desc"));
				clist.add(cat);
			}

			CategoryBean[] carray = new CategoryBean[clist.size()];
			clist.toArray(carray);
			return carray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ElementBean[] getElementDetails(String keyCol, String keyVal) {
		ElementBean ele = null;
		DatabaseOperation dbo = null;
		String table = null;
		ArrayList<ElementBean> elist = null;
		try {
			elist = new ArrayList<ElementBean>();
			dbo = new DatabaseOperation();
			table = "element";

			List<Map<String, Object>> result = dbo.selectWhere(table, keyCol,
					keyVal);
			for (int i = 0; i < result.size(); i++) {
				ele = new ElementBean();
				ele.setId(Integer.toString((int) result.get(i)
						.get("element_id")));
				ele.setName((String) result.get(i).get("element_name"));
				ele.setDescription((String) result.get(i).get("element_desc"));
				ele.setContact((String) result.get(i).get("element_contact"));
				ele.setCategory((String) result.get(i).get("element_category"));
				ele.setAddress((String) result.get(i).get("element_address"));
				ele.setCity((String) result.get(i).get("element_city"));
				elist.add(ele);
			}

			ElementBean[] earray = new ElementBean[elist.size()];
			elist.toArray(earray);
			return earray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public UserBean[] getUserDetails(String keyVal) {
		UserBean usr = null;
		DatabaseOperation dbo = null;
		String table = null, keyCol = null;
		ArrayList<UserBean> ulist = null;
		try {
			ulist = new ArrayList<UserBean>();
			dbo = new DatabaseOperation();
			table = "user";
			keyCol = "user_email";
			List<Map<String, Object>> result = dbo.selectWhere(table, keyCol,
					keyVal);
			for (int i = 0; i < result.size(); i++) {
				usr = new UserBean();

				usr.setId(Integer.toString((int) result.get(i).get("user_id")));
				usr.setEmail((String) result.get(i).get("user_email"));
				usr.setFirst_name((String) result.get(i).get("user_first_name"));
				usr.setLast_name((String) result.get(i).get("user_last_name"));
				usr.setCity((String) result.get(i).get("user_city"));

				ulist.add(usr);
			}
			UserBean[] uarray = new UserBean[ulist.size()];
			ulist.toArray(uarray);
			return uarray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String deleteCategory(String keyVal) {

		DatabaseOperation dbo = null;
		String table = null, keyCol = null;
		String errMsg = null;
		Boolean result = false;
		try {
			dbo = new DatabaseOperation();
			table = "category";
			keyCol = "category_id";

			result = dbo.delete(table, keyCol, keyVal);
			if (result) {
				return "true";
			} else {
				errMsg = "Could not delete item";
			}
		} catch (Exception e) {
			// System.out.println(e);
			errMsg = e.getMessage();
			// e.printStackTrace();
		}
		return errMsg;

	}

	public String deleteElement(String keyVal) {

		DatabaseOperation dbo = null;
		String table = null, keyCol = null;
		String errMsg = null;
		Boolean result = false;
		try {
			dbo = new DatabaseOperation();
			table = "element";
			keyCol = "element_id";

			result = dbo.delete(table, keyCol, keyVal);

			if (result) {
				return "true";
			} else {
				errMsg = "Could not delete item";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errMsg;

	}

	public String newCategory(CategoryBean cat) {
		DatabaseOperation dbo = null;
		String table = null;
		Boolean result = false;
		String errMsg = null;
		HashMap<String, String> map = null;
		try {

			dbo = new DatabaseOperation();
			table = "category";

			map = new HashMap<String, String>();
			map.put("category_name", cat.getName());
			map.put("category_desc", cat.getDescription());

			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " --- " + entry.getValue());
				if (entry.getValue().trim().equals("")
						|| entry.getKey().trim().equals("")) {
					errMsg = "Mandatory field - " + entry.getKey()
							+ " - cannot be null";
					return errMsg;
				}
			}
			result = dbo.insert(table, map);
			if (result) {
				return "true";
			} else {
				errMsg = "Insert failed";
			}
		} catch (Exception e) {
			errMsg = "unsuccessful insert";
			return errMsg;
		}
		return errMsg;
	}

	public String newElement(ElementBean ele) {
		DatabaseOperation dbo = null;
		String table = null;
		String errMsg = null;
		Boolean result = false;
		HashMap<String, String> map = null;
		try {

			dbo = new DatabaseOperation();
			table = "element";

			map = new HashMap<String, String>();

			map.put("element_name", ele.getName());
			map.put("element_desc", ele.getDescription());
			map.put("element_address", ele.getAddress());
			map.put("element_contact", ele.getContact());
			map.put("element_category", ele.getCategory());
			map.put("element_city", ele.getCity());

			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " --- " + entry.getValue());
				if (entry.getValue().trim().equals("")) {
					errMsg = "Mandatory field - " + entry.getKey()
							+ " - cannot be null";
					return errMsg;
				}
			}

			result = dbo.insert(table, map);
			if (result) {
				return "true";
			} else {
				errMsg = "Insert failed";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return errMsg;

	}

	public String newReview(ReviewBean rev) {
		DatabaseOperation dbo = null;
		String table = null;
		String errMsg = null;
		Boolean result = false;
		HashMap<String, String> map = null;
		try {
			dbo = new DatabaseOperation();
			table = "review";

			map = new HashMap<String, String>();
			map.put("review_content", rev.getContent());
			map.put("review_rating", rev.getRating());
			map.put("review_user", rev.getUser());
			map.put("review_ele", rev.getElement());
			map.put("review_date", rev.getDate());

			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " --- " + entry.getValue());
				if (entry.getValue().trim().equals("")
						|| entry.getKey().trim().equals("")) {
					errMsg = "Mandatory field - " + entry.getKey()
							+ " - cannot be null";
					return errMsg;
				}
			}
			if (Integer.parseInt(map.get("review_rating")) > 5
					|| Integer.parseInt(map.get("review_rating")) <= 0) {
				errMsg = "Rating cannot be greater than 5 or less than/equal to 0";
				return errMsg;
			}
			result = dbo.insert(table, map);
			if (result) {
				return "true";
			} else {
				errMsg = "Insert failed";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errMsg;

	}

	public String newUser(UserBean usr) {
		DatabaseOperation dbo = null;
		String table = null;
		String errMsg = null;
		Boolean result = false;

		HashMap<String, String> map = null;
		try {
			dbo = new DatabaseOperation();
			table = "user";

			map = new HashMap<String, String>();
			map.put("user_email", usr.getEmail());
			map.put("user_password", usr.getPassword());
			map.put("user_city", usr.getCity());
			map.put("user_first_name", usr.getFirst_name());
			map.put("user_last_name", usr.getLast_name());

			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " --- " + entry.getValue());
				if (entry.getValue().trim().equals("")
						|| entry.getKey().trim().equals("")) {
					errMsg = "Mandatory field - " + entry.getKey()
							+ " - cannot be null";
					return errMsg;
				}
			}

			result = dbo.insert(table, map);
			if (result) {
				return "true";
			} else {
				errMsg = "Failed: User already exists. Signup with a different email";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errMsg;

	}

	public String updateCategory(String idVal, CategoryBean cat) {
		DatabaseOperation dbo = null;
		String table = null, idCol = null;
		String errMsg = null;
		Boolean result = false;
		HashMap<String, String> map = null;
		try {

			dbo = new DatabaseOperation();
			table = "category";
			idCol = "category_id";

			map = new HashMap<String, String>();
			map.put("category_name", cat.getName());
			map.put("category_desc", cat.getDescription());

			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " --- " + entry.getValue());
				if (entry.getValue().trim().equals("")
						|| entry.getKey().trim().equals("")
						|| entry.getKey().trim().equals(null)
						|| entry.getValue().trim().equals(null)) {
					errMsg = "Mandatory field - " + entry.getKey()
							+ " - cannot be null";
					return errMsg;
				}
			}

			result = dbo.update(table, idCol, idVal, map);
			if (result) {
				return "true";
			} else {
				errMsg = "Could not update item. Uniqueness/Integrity constraint violation.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errMsg;

	}

	public String updateElement(String idVal, ElementBean ele) {
		DatabaseOperation dbo = null;
		String table = null, idCol = null;
		String errMsg = null;
		Boolean result = false;
		HashMap<String, String> map = null;
		try {

			dbo = new DatabaseOperation();
			table = "element";
			idCol = "element_id";

			map = new HashMap<String, String>();

			map.put("element_name", ele.getName());
			map.put("element_desc", ele.getDescription());
			map.put("element_address", ele.getAddress());
			map.put("element_contact", ele.getContact());
			map.put("element_category", ele.getCategory());
			map.put("element_city", ele.getCity());

			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " --- " + entry.getValue());
				if (entry.getValue().trim().equals("")
						|| entry.getKey().trim().equals("")) {
					errMsg = "Mandatory field - " + entry.getKey()
							+ " - cannot be null";
					return errMsg;
				}
			}

			result = dbo.update(table, idCol, idVal, map);
			if (result) {
				return "true";
			} else {
				errMsg = "Could not update item. Uniqueness/Integrity constraint violation.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errMsg;

	}

}
