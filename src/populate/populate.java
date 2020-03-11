package populate;

import java.sql.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class populate {

	public static class Attribute {
		public String name;
		public String value;
	}

	public static class Business {
		public String businessId;
		public List<String> category;
		public List<String> subCategory;
		public String city;
		public String state;
		public Integer reviewCount;
		public String name;
		public Float stars;
		public List<Attribute> attributes;
	}

	public static class Review {
		public String reviewId;
		public String businessId;
		public String userId;
		public Integer votesFunny;
		public Integer votesCool;
		public Integer votesUseful;
		public String reviewDate;
		public String reviewText;
		public Float stars;
	}

	public static class User {
		public String yelpingSince;
		public Integer votesFunny;
		public Integer votesCool;
		public Integer votesUseful;
		public Integer reviewCount;
		public String name;
		public String userId;
		public List<String> friends;
		public Float averageStars;
	}

	public static List<Business> generateBusinessData(JSONArray jsonArray) {
		List<Business> businessList = new ArrayList<Business>();
		Iterator itr = jsonArray.iterator();
		try {
			while (itr.hasNext()) {
				JSONObject obj = (JSONObject) itr.next();
				Business bsns = new Business();
				bsns.businessId = (String) obj.get("business_id");

				List<String> catList = new ArrayList<String>();
				List<String> subcatList = new ArrayList<String>();
				List<String> allCat = getAllCategories();
				JSONArray catObj = (JSONArray) obj.get("categories");
				for (int i = 0; i < catObj.size(); i++) {
					String cat = catObj.get(i).toString();
					if (allCat.contains(cat))
						catList.add(cat);
					else
						subcatList.add(cat);
				}
				bsns.category = catList;
				bsns.subCategory = subcatList;
				bsns.city = (String) obj.get("city");
				bsns.reviewCount = (int) (long) obj.get("review_count");
				bsns.name = (String) obj.get("name");
				bsns.state = (String) obj.get("state");
				bsns.stars = Float.parseFloat(obj.get("stars").toString());

				List<Attribute> attributeList = new ArrayList<Attribute>();
				if (obj.get("attributes") != null) {
					JSONObject attrObj = (JSONObject) new JSONParser().parse(obj.get("attributes").toString());
					for (Object key : attrObj.keySet()) {
						String k = (String) key;
						Object keyvalue = attrObj.get(k);
						if (keyvalue instanceof JSONObject) {
							JSONObject temp = (JSONObject) attrObj.get(key);
							for (Object key2 : temp.keySet()) {
								String k2 = (String) key2;
								Object keyvalue2 = temp.get(k2);
								String val = keyvalue2.toString();
								if (keyvalue2 instanceof Boolean || keyvalue2 instanceof Integer) {
									k2 += "_" + val;
								}
								Attribute attr = new Attribute();
								attr.name = k2;
								attr.value = val;
								attributeList.add(attr);
							}
						} else {
							String val = attrObj.get(k).toString();
							if (keyvalue instanceof Boolean || keyvalue instanceof Integer) {
								k += "_" + val;
							}
							Attribute attr = new Attribute();
							attr.name = k;
							attr.value = val;
							attributeList.add(attr);
						}

					}
				}
				bsns.attributes = attributeList;
				businessList.add(bsns);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessList;
	}

	public static void insertBusinessData(List<Business> businesses) {
		Connection conn = getDBConnection();
		try {
			PreparedStatement ps1 = conn.prepareStatement("INSERT INTO business"
					+ "(bsns_id, name, city, state, review_count, stars) " + "VALUES (?, ?, ?, ?, ?, ?)");
			PreparedStatement ps2 = conn.prepareStatement("INSERT  INTO business_cat(bsns_id, category) VALUES(?, ?)");
			PreparedStatement ps3 = conn
					.prepareStatement("INSERT INTO business_subcat(bsns_id, subcategory)  VALUES(?,?)");
			PreparedStatement ps4 = conn
					.prepareStatement("INSERT INTO business_attr(bsns_id, attr_name, attr_value)  VALUES(?,?,?)");

			for (Business bsns : businesses) {
				ps1.setString(1, bsns.businessId);
				ps1.setString(2, bsns.name);
				ps1.setString(3, bsns.city);
				ps1.setString(4, bsns.state);
				ps1.setInt(5, bsns.reviewCount);
				ps1.setFloat(6, bsns.stars);
				ps1.addBatch();

				List<String> categories = bsns.category;
				for (String cat : categories) {
					ps2.setString(1, bsns.businessId);
					ps2.setString(2, cat);
					ps2.addBatch();
				}
				List<String> subcategories = bsns.subCategory;
				for (String subcat : subcategories) {
					ps3.setString(1, bsns.businessId);
					ps3.setString(2, subcat);
					ps3.addBatch();
				}
				List<Attribute> attrs = bsns.attributes;
				for (Attribute attr : attrs) {
					ps4.setString(1, bsns.businessId);
					ps4.setString(2, attr.name);
					ps4.setString(3, attr.value);
					ps4.addBatch();
				}
			}
			ps1.executeBatch();
			ps2.executeBatch();
			ps3.executeBatch();
			ps4.executeBatch();
			ps1.close();
			ps2.close();
			ps3.close();
			ps4.close();
			conn.close();
			System.out.println("SUCCESS: Business Data inserted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Review> generateReviewData(JSONArray jsonArray) {
		List<Review> reviewList = new ArrayList<Review>();
		Iterator itr = jsonArray.iterator();
		try {
			while (itr.hasNext()) {
				JSONObject obj = (JSONObject) itr.next();
				Review review = new Review();
				review.reviewDate = (String) obj.get("date");
				JSONObject votesObj = (JSONObject) new JSONParser().parse(obj.get("votes").toString());
				review.votesFunny = Integer.parseInt(votesObj.get("funny").toString());
				review.votesCool = Integer.parseInt(votesObj.get("cool").toString());
				review.votesUseful = Integer.parseInt(votesObj.get("useful").toString());
				review.stars = Float.parseFloat(obj.get("stars").toString());
				review.reviewText = (String) obj.get("text");
				review.userId = (String) obj.get("user_id");
				review.reviewId = (String) obj.get("review_id");
				review.businessId = (String) obj.get("business_id");
				reviewList.add(review);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reviewList;
	}

	public static void insertReviewData(List<Review> reviews) {
		Connection conn = getDBConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO reviews "
					+ "(stars , user_id,  votes_funny, votes_useful, votes_cool, review_dt, review_txt, bsns_id, review_id) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			for (Review review : reviews) {
				ps.setFloat(1, review.stars);
				ps.setString(2, review.userId);
				ps.setInt(3, review.votesFunny);
				ps.setInt(4, review.votesUseful);
				ps.setInt(5, review.votesCool);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date tempDate = format.parse(review.reviewDate);
				java.sql.Date reviewDt = new java.sql.Date(tempDate.getTime());
				ps.setDate(6, reviewDt);
				String str = review.reviewText.substring(0, Math.min(review.reviewText.length(), 2000));
				ps.setString(7, str);
				String businessStr = review.businessId.substring(0, Math.min(review.businessId.length(), 2000));
				ps.setString(8, businessStr);
				ps.setString(9, review.reviewId);
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
			conn.close();
			System.out.println("SUCCESS: Review Data inserted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<User> generateUserData(JSONArray jsonArray) {
		List<User> userList = new ArrayList<User>();
		Iterator itr = jsonArray.iterator();
		try {
			while (itr.hasNext()) {
				JSONObject obj = (JSONObject) itr.next();
				User user = new User();
				user.yelpingSince = (String) obj.get("yelping_since");
				JSONObject votesObj = (JSONObject) new JSONParser().parse(obj.get("votes").toString());
				user.votesFunny = Integer.parseInt(votesObj.get("funny").toString());
				user.votesCool = Integer.parseInt(votesObj.get("cool").toString());
				user.votesUseful = Integer.parseInt(votesObj.get("useful").toString());
				user.reviewCount = (int) (long) obj.get("review_count");
				user.name = (String) obj.get("name");
				user.userId = (String) obj.get("user_id");
				user.friends = new ArrayList<String>();
				JSONArray friendArr = (JSONArray) obj.get("friends");
				Iterator itr1 = friendArr.iterator();
				while (itr1.hasNext()) {
					user.friends.add((String) itr1.next());
				}
				user.averageStars = Float.parseFloat(obj.get("average_stars").toString());
				userList.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	public static void insertUserData(List<User> users) {
		Connection conn = getDBConnection();
		try {
			PreparedStatement ps1 = conn.prepareStatement("INSERT INTO yelp_user "
					+ "(user_id , name , yelping_since,  votes_funny, votes_useful, votes_cool, review_count, average_stars) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			for (User user : users) {
				ps1.setString(1, user.userId);
				ps1.setString(2, user.name);
				DateFormat format = new SimpleDateFormat("yyyy-MM");
				java.util.Date tempDate = format.parse(user.yelpingSince);
				java.sql.Date yelpingSinceDate = new java.sql.Date(tempDate.getTime());
				ps1.setDate(3, yelpingSinceDate);
				ps1.setInt(4, user.votesFunny);
				ps1.setInt(5, user.votesUseful);
				ps1.setInt(6, user.votesCool);
				ps1.setInt(7, user.reviewCount);
				ps1.setFloat(8, user.averageStars);
				ps1.addBatch();
			}
			ps1.executeBatch();
			ps1.close();
			conn.close();
			System.out.println("SUCCESS: User Data inserted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insertFriendData(List<User> users) {
		Connection conn = getDBConnection();
		try {
			PreparedStatement ps1 = conn
					.prepareStatement("INSERT INTO friend_list (user_id , friend_id ) VALUES (?, ?)");
			for (User user : users) {
				for (String str : user.friends) {
					ps1.setString(1, user.userId);
					ps1.setString(2, str);
					ps1.addBatch();
				}
			}
			ps1.executeBatch();
			ps1.close();
			conn.close();
			System.out.println("SUCCESS: Friend Data inserted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getDBConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception Ex) {
			System.err.println("Unable to load driver.");
			Ex.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "oracle");
		} catch (SQLException E) {
			System.out.println("SQLException: " + E.getMessage());
			System.out.println("SQLState: " + E.getSQLState());
			System.out.println("VendorError: " + E.getErrorCode());
		}
		return conn;
	}

	public static void deleteUserData() {
		String sql1 = "DELETE FROM yelp_user";
		String sql2 = "DELETE FROM friend_list";
		Connection conn = getDBConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			System.out.println("SUCCESS: User Data Deleted");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteBusinessData() {
		String sql1 = "DELETE FROM business";
		String sql2 = "DELETE FROM business_cat";
		String sql3 = "DELETE FROM business_subcat";
		String sql4 = "DELETE FROM business_attr";
		Connection conn = getDBConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			stmt.executeUpdate(sql4);
			System.out.println("SUCCESS: Business Data Deleted");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteReviewData() {
		String sql = "DELETE FROM reviews";
		Connection conn = getDBConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("SUCCESS: Review Data Deleted");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> getAllCategories() {
		List<String> categories = new ArrayList<String>();
		categories.add("Active Life");
		categories.add("Arts & Entertainment");
		categories.add("Automotive");
		categories.add("Car Rental");
		categories.add("Cafes");
		categories.add("Beauty & Spas");
		categories.add("Convenience Stores");
		categories.add("Dentists");
		categories.add("Doctors");
		categories.add("Drugstores");
		categories.add("Department Stores");
		categories.add("Education");
		categories.add("Event Planning & Services");
		categories.add("Flowers & Gifts");
		categories.add("Food");
		categories.add("Health & Medical");
		categories.add("Home Services");
		categories.add("Home & Garden");
		categories.add("Hospitals");
		categories.add("Hotels & Travel");
		categories.add("Hardware Stores");
		categories.add("Grocery");
		categories.add("Medical Centers");
		categories.add("Nurseries & Gardening");
		categories.add("Nightlife");
		categories.add("Restaurants");
		categories.add("Shopping");
		categories.add("Transportation");

		return categories;
	}

	public static void executeBatch(String fileName, JSONArray jsonArray) {
		if (fileName.equals("yelp_business.json")) {
			List<Business> businesses = generateBusinessData(jsonArray);
			insertBusinessData(businesses);
		} else if (fileName.equals("yelp_review.json")) {
			List<Review> reviews = generateReviewData(jsonArray);
			insertReviewData(reviews);
		} else if (fileName.equals("yelp_user.json")) {
			List<User> users = generateUserData(jsonArray);
			insertUserData(users);
			insertFriendData(users);
		}
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				int count = 0;
				int batchSize = 10000;
				JSONParser parser = new JSONParser();
				for (int i = 0; i < args.length; i++) {
					BufferedReader reader = new BufferedReader(new FileReader(args[i]));
					String line = reader.readLine();
					JSONArray jsonArray = new JSONArray();
					if (args[i].equals("yelp_business.json")) {
						deleteBusinessData();
					} else if (args[i].equals("yelp_review.json")) {						
						deleteReviewData();
					} else if (args[i].equals("yelp_user.json")) {
						deleteUserData();
					}
					while (line != null) {
						jsonArray.add((JSONObject) parser.parse(line));
						line = reader.readLine();
						if(args[i].equals("yelp_review.json") && ++count%batchSize==0){
							executeBatch(args[i], jsonArray);
						}
					}
					executeBatch(args[i], jsonArray);
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			System.out.println("ERROR: no command line arguments");
	}
}
