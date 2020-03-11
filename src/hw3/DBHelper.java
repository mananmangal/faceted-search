package hw3;

import hw3.Models.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DBHelper {

	public Connection connection;

	public DBHelper() {

	}

	public void DBConnect() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		} catch (Exception e) {
			System.err.println("Unable to load driver.");
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "oracle");
		} catch (Exception e) {
			System.err.println("Unable to connect");
			e.printStackTrace();
		}
	}

	public void DBClose() {
		try {
			connection.close();
		} catch (Exception e) {
			System.err.println("Unable to close db");
			e.printStackTrace();
		}
	}

	public ReturnAttribute getAttributes(ArrayList<String> selectedSubCategories, ArrayList<String> selectedCategories,
			String condition) {
		ReturnAttribute ra = new ReturnAttribute();
		ArrayList<Attribute> attrs = new ArrayList<Attribute>();
		try {
			String query = "";
			if (condition == "AND") {
				String subq = "(SELECT bsns_id FROM business_cat WHERE category = '" + selectedCategories.get(0) + "')";
				if (selectedCategories.size() > 1) {
					for (int i = 1; i < selectedCategories.size(); i++) {
						subq += " INTERSECT (SELECT bsns_id FROM business_cat WHERE category = '"
								+ selectedCategories.get(i) + "' )";
					}
				}
				query = "SELECT DISTINCT ba.attr_name FROM business_attr ba JOIN business_subcat bs on bs.bsns_id = ba.bsns_id JOIN business_cat BC on ba.bsns_id = BC.bsns_id WHERE ba.bsns_id IN ( "
						+ subq + " )";

			} else {

				query = "SELECT DISTINCT ba.attr_name FROM business_attr ba JOIN business_subcat bs on bs.bsns_id = ba.bsns_id JOIN business_cat BC on ba.bsns_id = BC.bsns_id WHERE bc.category = ";
				query += "'" + selectedCategories.get(0) + "' ";

				if (selectedCategories.size() > 1) {
					for (int i = 1; i < selectedCategories.size(); i++) {
						query += condition + " bc.category = '" + selectedCategories.get(i) + "' ";
					}
				}

			}

			query += " AND bs.subcategory = '" + selectedSubCategories.get(0) + "' ";
			if (selectedSubCategories.size() > 1) {
				for (int i = 1; i < selectedSubCategories.size(); i++) {
					query += condition + " bs.subcategory = '" + selectedSubCategories.get(i) + "' ";
				}
			}
			ra.query = query;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Attribute attribute = new Attribute();
				attribute.name = rs.getString(1);
				attrs.add(attribute);
			}
		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		ra.attributes = attrs;
		return ra;
	}

	public ReturnString getSubCategories(ArrayList<String> selectedCategories, String condition) {
		ReturnString rStr = new ReturnString();
		ArrayList<String> subs = new ArrayList<String>();
		try {
			String query;
			if (condition == "AND") {
				String subq = "(SELECT bsns_id FROM BUSINESS_CAT WHERE category = '" + selectedCategories.get(0) + "')";
				if (selectedCategories.size() > 1) {
					for (int i = 1; i < selectedCategories.size(); i++) {
						subq += " INTERSECT (SELECT bsns_id FROM BUSINESS_CAT WHERE  category = '"
								+ selectedCategories.get(i) + "' )";
					}
				}
				query = "SELECT DISTINCT bs.subcategory FROM BUSINESS_SUBCAT bs JOIN BUSINESS_CAT bc on bs.bsns_id = bc.bsns_id WHERE bs.bsns_id IN ( "
						+ subq + " )";

			} else {

				query = "SELECT DISTINCT bs.subcategory FROM BUSINESS_SUBCAT bs JOIN BUSINESS_CAT bc on bs.bsns_id = bc.bsns_id WHERE bc.CATEGORY = ";
				query += "'" + selectedCategories.get(0) + "' ";
				if (selectedCategories.size() > 1) {
					for (int i = 1; i < selectedCategories.size(); i++) {
						query += condition + " bc.category = '" + selectedCategories.get(i) + "' ";
					}
				}
			}
			rStr.query = query;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String sub = new String();
				sub = rs.getString(1);
				subs.add(sub);
			}
		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rStr.str = subs;
		return rStr;
	}

	public ReturnString getAllCategories() {
		ReturnString rStr = new ReturnString();
		ArrayList<String> cats = new ArrayList<String>();
		try {
			String query = "SELECT DISTINCT category FROM BUSINESS_CAT ORDER BY category";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String cat = new String();
				cat = rs.getString(1);
				cats.add(cat);
			}
			rStr.query = query;
		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rStr.str = cats;
		return rStr;
	}

	public ReturnBusiness queryBusinessByCategory(ArrayList<String> selectedCategories, Date reviewFrom, Date reviewTo,
			String reviewStars, String starsCondition, String reviewVotes, String votesCondition, String condition) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		ReturnBusiness rb = new ReturnBusiness();
		String query;
		if (condition == "AND") {
			String subq = "SELECT bsns_id FROM BUSINESS_CAT WHERE category = '" + selectedCategories.get(0) + "'";
			if (selectedCategories.size() > 1) {
				for (int i = 1; i < selectedCategories.size(); i++) {
					subq += " INTERSECT SELECT bsns_id FROM BUSINESS_CAT WHERE category = '" + selectedCategories.get(i)
							+ "' ";
				}
			}
			query = "SELECT DISTINCT b.bsns_id, b.name, b.city, b.state, b.stars FROM BUSINESS b JOIN BUSINESS_CAT bc on bc.bsns_id = b.bsns_id LEFT JOIN reviews r ON r.bsns_id = b.bsns_id  WHERE b.bsns_id IN ( "
					+ subq + " )";

		} else {
			query = "SELECT DISTINCT b.bsns_id, b.name, b.city, b.state, b.stars FROM BUSINESS b JOIN BUSINESS_CAT bc on bc.bsns_id = b.bsns_id LEFT JOIN reviews r ON r.bsns_id = b.bsns_id  WHERE (bc.category = ";
			query += "'" + selectedCategories.get(0) + "' ";
			if (selectedCategories.size() > 1) {
				for (int i = 1; i < selectedCategories.size(); i++) {
					query += condition + " bc.category = '" + selectedCategories.get(i) + "' ";
				}
			}
			query += ")";
		}
		if (reviewStars.length() > 0 && reviewStars != null && !reviewStars.isEmpty()) {
			query += " AND b.stars" + starsCondition + reviewStars;
		}

		if (reviewFrom != null && !reviewFrom.toString().isEmpty()) {
			query += " AND r.review_dt >= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewFrom)
					+ "','DD/MM/YY')";
		}

		if (reviewTo != null && !reviewTo.toString().isEmpty()) {
			query += " AND r.review_dt <= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewTo)
					+ "','DD/MM/YY')";
		}

		if (reviewVotes.length() > 0 && reviewVotes != null && !reviewVotes.isEmpty()) {
			query += " GROUP BY b.bsns_id, b.name, b.city, b.state, b.stars HAVING sum(r.votes_useful+r.votes_cool+r.votes_funny)"
					+ votesCondition + reviewVotes;
		}
		rb.query = query;
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Business b = new Business();
				b.businessId = rs.getString(1);
				b.name = rs.getString(2);
				b.city = rs.getString(3);
				b.state = rs.getString(4);
				b.stars = rs.getFloat(5);
				businesses.add(b);
			}
		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rb.business = businesses;
		return rb;
	}

	public ReturnBusiness queryBusinessByCategorySubCategory(ArrayList<String> selectedCategories,
			ArrayList<String> selectedSubCategories, Date reviewFrom, Date reviewTo, String reviewStars,
			String starsCondition, String reviewVotes, String votesCondition, String condition) {
		ReturnBusiness rb = new ReturnBusiness();
		ArrayList<Business> businesses = new ArrayList<Business>();
		String query = "";
		if (condition == "AND") {
			String subq = "(SELECT bsns_id FROM BUSINESS_CAT WHERE category = '" + selectedCategories.get(0) + "')";
			if (selectedCategories.size() > 1) {
				for (int i = 1; i < selectedCategories.size(); i++) {
					subq += " INTERSECT (SELECT bsns_id FROM BUSINESS_CAT WHERE category = '"
							+ selectedCategories.get(i) + "' )";
				}
			}
			query = "SELECT DISTINCT b.bsns_id, b.name, b.city, b.state, b.stars FROM BUSINESS b JOIN BUSINESS_CAT bc on bc.bsns_id = b.bsns_id JOIN BUSINESS_SUBCAT bs ON bs.bsns_id = b.bsns_id LEFT JOIN reviews r ON r.bsns_id = b.bsns_id  WHERE  b.bsns_id IN ( "
					+ subq + " )";

		} else {
			query = "SELECT DISTINCT b.bsns_id, b.name, b.city, b.state, b.stars FROM BUSINESS b JOIN BUSINESS_CAT bc on bc.bsns_id = b.bsns_id JOIN BUSINESS_SUBCAT bs ON bs.bsns_id = b.bsns_id LEFT JOIN reviews r ON r.bsns_id = b.bsns_id  WHERE (bc.category = ";
			query += "'" + selectedCategories.get(0) + "' ";
			if (selectedCategories.size() > 1) {
				for (int i = 1; i < selectedCategories.size(); i++) {
					query += condition + " bc.category = '" + selectedCategories.get(i) + "' ";
				}
			}
			query += ")";

		}
		query += " AND (bs.subcategory = '" + selectedSubCategories.get(0) + "' ";
		if (selectedSubCategories.size() > 1) {
			for (int i = 1; i < selectedSubCategories.size(); i++) {
				query += condition + " bs.subcategory = '" + selectedSubCategories.get(i) + "' ";
			}
		}
		query += ")";

		if (reviewStars.length() > 0 && reviewStars != null && !reviewStars.isEmpty()) {
			query += " AND b.stars" + starsCondition + reviewStars;
		}

		if (reviewFrom != null && !reviewFrom.toString().isEmpty()) {
			query += " AND r.review_dt >= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewFrom)
					+ "','DD/MM/YY')";
		}

		if (reviewTo != null && !reviewTo.toString().isEmpty()) {
			query += " AND r.review_dt <= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewTo)
					+ "','DD/MM/YY')";
		}

		if (reviewVotes.length() > 0 && reviewVotes != null && !reviewVotes.isEmpty()) {
			query += " GROUP BY b.bsns_id, b.name, b.city, b.state, b.stars HAVING sum(r.votes_useful+r.votes_cool+r.votes_funny)"
					+ votesCondition + reviewVotes;
		}
		rb.query = query;
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Business b = new Business();
				b.businessId = rs.getString(1);
				b.name = rs.getString(2);
				b.city = rs.getString(3);
				b.state = rs.getString(4);
				b.stars = rs.getFloat(5);
				businesses.add(b);
			}
		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rb.business = businesses;
		return rb;
	}

	public ReturnBusiness queryBusiness(ArrayList<String> selectedAttributes, ArrayList<String> selectedCategories,
			ArrayList<String> selectedSubCategories, Date reviewFrom, Date reviewTo, String reviewStars,
			String starsCondition, String reviewVotes, String votesCondition, String condition) {
		ReturnBusiness rb = new ReturnBusiness();
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			String query = "SELECT DISTINCT b.bsns_id, b.name, b.city, b.state, b.stars  FROM business b JOIN BUSINESS_ATTR ba on b.bsns_id = ba.bsns_id JOIN BUSINESS_CAT bc on bc.bsns_id = b.bsns_id JOIN BUSINESS_SUBCAT bs ON bs.bsns_id = b.bsns_id LEFT JOIN reviews r ON r.bsns_id = b.bsns_id WHERE (ba.attr_name = ";
			query += "'" + selectedAttributes.get(0) + "' ";
			if (selectedAttributes.size() > 1) {
				for (int i = 1; i < selectedAttributes.size(); i++) {
					query += condition + "  ba.attr_name = '" + selectedAttributes.get(i) + "' ";
				}
			}
			query += ")";
			query += " AND (bc.category = '" + selectedCategories.get(0) + "' ";
			if (selectedCategories.size() > 1) {
				for (int i = 1; i < selectedCategories.size(); i++) {
					query += condition + " bc.category = '" + selectedCategories.get(i) + "' ";
				}
			}
			query += ")";
			query += " AND (bs.subcategory = '" + selectedSubCategories.get(0) + "' ";
			if (selectedSubCategories.size() > 1) {
				for (int i = 1; i < selectedSubCategories.size(); i++) {
					query += condition + " bs.subcategory = '" + selectedSubCategories.get(i) + "' ";
				}
			}
			query += ")";

			if (reviewStars.length() > 0 && reviewStars != null && !reviewStars.isEmpty()) {
				query += " AND b.stars" + starsCondition + reviewStars;
			}

			if (reviewFrom != null && !reviewFrom.toString().isEmpty()) {
				query += " AND r.review_dt >= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewFrom)
						+ "','DD/MM/YY')";
			}

			if (reviewTo != null && !reviewTo.toString().isEmpty()) {
				query += " AND r.review_dt <= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewTo)
						+ "','DD/MM/YY')";
			}

			if (reviewVotes.length() > 0 && reviewVotes != null && !reviewVotes.isEmpty()) {
				query += " GROUP BY b.bsns_id, b.name, b.city, b.state, b.stars HAVING sum(r.votes_useful+r.votes_cool+r.votes_funny)"
						+ votesCondition + reviewVotes;
			}
			rb.query = query;

			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Business b = new Business();
				b.businessId = rs.getString(1);
				b.name = rs.getString(2);
				b.city = rs.getString(3);
				b.state = rs.getString(4);
				b.stars = rs.getFloat(5);
				businesses.add(b);
			}
		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rb.business = businesses;
		return rb;
	}

	public ReturnBusiness advancedQueryBusiness(ArrayList<String> selectedCategories,
			ArrayList<String> selectedSubCategories, ArrayList<String> selectedAttributes, Date reviewFrom,
			Date reviewTo, String reviewStars, String starsCondition, String reviewVotes, String votesCondition,
			String condition) {
		ReturnBusiness rb = new ReturnBusiness();
		ArrayList<Business> businesses = new ArrayList<Business>();
		String query = "SELECT DISTINCT b.bsns_id, b.name, b.city, b.state, b.stars FROM business b LEFT JOIN BUSINESS_ATTR ba ON b.bsns_id = ba.bsns_id LEFT JOIN BUSINESS_CAT bc on bc.bsns_id = b.bsns_id LEFT JOIN BUSINESS_SUBCAT bs ON bs.bsns_id = b.bsns_id LEFT JOIN reviews r ON r.bsns_id = b.bsns_id WHERE (bc.category =  ";
		query += " '" + selectedCategories.get(0) + "' ";
		if (selectedCategories.size() > 1) {
			for (int i = 1; i < selectedCategories.size(); i++) {
				query += condition + " bc.category = '" + selectedCategories.get(i) + "'";
			}
		}
		query += ")";
		query += " AND (ba.attr_name =  '" + selectedAttributes.get(0) + "' ";
		if (selectedAttributes.size() > 1) {
			for (int i = 1; i < selectedAttributes.size(); i++) {
				query += condition + "  ba.attr_name = '" + selectedAttributes.get(i) + "'";
			}
		}
		query += ")";

		query += " AND (bs.subcategory = '" + selectedSubCategories.get(0) + "' ";
		if (selectedSubCategories.size() > 1) {
			for (int i = 1; i < selectedSubCategories.size(); i++) {
				query += condition + " bs.subcategory = '" + selectedSubCategories.get(i) + "'";
			}
		}
		query += ")";

		if (reviewStars.length() > 0 && reviewStars != null && !reviewStars.isEmpty()) {
			query += " AND b.stars" + starsCondition + reviewStars;
		}

		if (reviewFrom != null && !reviewFrom.toString().isEmpty()) {
			query += " AND r.review_dt >= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewFrom)
					+ "','DD/MM/YY')";
		}

		if (reviewTo != null && !reviewTo.toString().isEmpty()) {
			query += " AND r.review_dt <= TO_DATE('" + new SimpleDateFormat("dd-MM-yy").format(reviewTo)
					+ "','DD/MM/YY')";
		}

		if (reviewVotes.length() > 0 && reviewVotes != null && !reviewVotes.isEmpty()) {
			query += " GROUP BY b.bsns_id, b.name, b.city, b.state, b.stars HAVING sum(r.votes_useful+r.votes_cool+r.votes_funny)"
					+ votesCondition + reviewVotes;
		}
		rb.query = query;

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Business b = new Business();
				b.businessId = rs.getString(1);
				b.name = rs.getString(2);
				b.city = rs.getString(3);
				b.state = rs.getString(4);
				b.stars = rs.getFloat(5);
				businesses.add(b);
			}
		} catch (

		SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rb.business = businesses;
		return rb;
	}

	public ReturnUser queryUser(Date memberSince, String reviewCount, String reviewCondition, String numberFriends,
			String friendsCondition, String averageStars, String starsCondition, String numberVotes,
			String votesCondition, String searchCondition) {
		ReturnUser ru = new ReturnUser();
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT u.user_id, u.name, u.yelping_since, u.average_stars FROM yelp_user u WHERE u.yelping_since >= TO_DATE(";
		query += " '" + new SimpleDateFormat("dd-MM-yy").format(memberSince) + "','DD/MM/YY')";

		if (reviewCount.length() > 0 && reviewCount != null && !reviewCount.isEmpty()) {
			query += " " + searchCondition + " u.review_count" + reviewCondition + reviewCount;
		}
		if (averageStars.length() > 0 && averageStars != null && !averageStars.isEmpty()) {
			query += " " + searchCondition + " u.average_stars" + starsCondition + averageStars;
		}
		if (numberVotes.length() > 0 && numberVotes != null && !numberVotes.isEmpty()) {
			query += " " + searchCondition
					+ " u.user_id IN (SELECT yu.user_id FROM yelp_user yu GROUP BY yu.user_id HAVING SUM(yu.votes_funny+yu.votes_cool+yu.votes_useful)"
					+ votesCondition + numberVotes + ")";
		}
		if (numberFriends.length() > 0 && numberFriends != null && !numberFriends.isEmpty()) {
			query += " " + searchCondition
					+ " u.user_id IN (SELECT f.user_id FROM friend_list f GROUP BY f.user_id HAVING COUNT(*)"
					+ friendsCondition + numberFriends + ")";
		}
		ru.query = query;
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				User u = new User();
				u.userId = rs.getString(1);
				u.name = rs.getString(2);
				u.yelpingSince = rs.getString(3);
				u.averageStars = rs.getFloat(4);
				users.add(u);
			}

		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		ru.users = users;
		return ru;
	}

	public ReturnReview getReviews(String businessId, ArrayList<String> filters) {
		ReturnReview rr = new ReturnReview();
		ArrayList<Review> reviews = new ArrayList<Review>();
		String query = "SELECT r.review_dt, r.stars, r.review_txt, u.name, (r.votes_useful+r.votes_funny+r.votes_cool) FROM reviews r JOIN yelp_user u ON u.user_id = r.user_id WHERE r.bsns_id = '"
				+ businessId + "'";
		try {

			String reviewFrom = filters.get(0);
			if (filters.get(0).length() > 0 && filters.get(0) != null && !filters.get(0).isEmpty()) {
				query += " AND r.review_dt>='" + reviewFrom + "'";
			}

			String reviewTo = filters.get(1);
			if (filters.get(1).length() > 0 && filters.get(1) != null && !filters.get(1).isEmpty()) {
				query += " AND r.review_dt<='" + reviewTo + "'";
			}

			String reviewStars = filters.get(2);
			if (reviewStars.length() > 0 && reviewStars != null && !reviewStars.isEmpty()) {
				query += " AND r.stars" + filters.get(3) + reviewStars;
			}

			String reviewVotes = filters.get(4);
			if (reviewVotes.length() > 0 && reviewVotes != null && !reviewVotes.isEmpty()) {
				query += " AND (r.votes_useful+r.votes_funny+r.votes_cool)" + filters.get(5) + reviewVotes;
			}

			rr.query = query;

			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Review r = new Review();
				r.reviewDate = rs.getDate(1).toString();
				r.stars = rs.getFloat(2);
				r.reviewText = rs.getString(3);
				r.userId = rs.getString(4);
				r.votesUseful = rs.getInt(5);
				reviews.add(r);
			}

		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rr.reviews = reviews;
		return rr;
	}

	public ReturnReview getUserReviews(String userId) {
		ReturnReview rr = new ReturnReview();
		ArrayList<Review> reviews = new ArrayList<Review>();
		try {
			String query = "SELECT r.review_dt, r.stars, r.review_txt, r.votes_useful FROM reviews r WHERE r.user_id = '"
					+ userId + "'";
			rr.query = query;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Review r = new Review();
				r.reviewDate = rs.getDate(1).toString();
				r.stars = rs.getFloat(2);
				r.reviewText = rs.getString(3);
				r.votesUseful = rs.getInt(4);
				reviews.add(r);
			}

		} catch (SQLException se) {
			System.err.println("Query error: SQL Exception");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Query error ");
			e.printStackTrace();
		}
		rr.reviews = reviews;
		return rr;
	}

}
