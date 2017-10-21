package com.mikelduke.java.wifidisplay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.qmetric.spark.authentication.AuthenticationDetails;
import com.qmetric.spark.authentication.BasicAuthenticationFilter;

import spark.ModelAndView;
import spark.Spark;
import spark.TemplateEngine;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class WifiDisplay {
	private static final String CLAZZ = WifiDisplay.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	public static final int ROWS = 4;
	public static final int ROW_LENGTH = 20;
	
	//We use rows-1 because the first row will be the time
	private static volatile String[] message = new String[ROWS - 1];
	
	private static final TemplateEngine thymeleaf = new ThymeleafTemplateEngine();
	
	static {
		for (int i = 0; i < message.length; i++) {
			message[i] = "";
		}
	}
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) {
		if (args.length > 0) {
			int port = Integer.parseInt(args[0]);
			Spark.port(port);
		}
		
		String user = System.getenv("wifi-user");
		String pass = System.getenv("wifi-pass");
		
		if (user == null) {
			user = System.getProperty("wifi-user", "admin");
		}
		if (pass == null) {
			pass = System.getProperty("wifi-pass", "admin");
		}
		
		Spark.before(new BasicAuthenticationFilter(
				"/*", new AuthenticationDetails(user, pass)));
		
		Spark.get("/status", (req, res) -> {
			res.type("text/plain");
			return "up";
		});

		Spark.get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("time", LocalDateTime.now().format(formatter));
			model.put("message", message);
			model.put("rows", ROWS - 1);
			model.put("rowLength", ROW_LENGTH);
			return thymeleaf.render(
					new ModelAndView(model, "editMessage")
			);
		});

		Spark.get("/message", (req, res) -> {
			res.type("text/plain");
			return getFormattedMessage();
		});
		
		Spark.post("/message", (req, res) -> {
			if (req.queryParams().isEmpty()) {
				res.status(400);
				return "Invalid message";
			} else {
				LOGGER.logp(Level.INFO, CLAZZ, "message", "Message Updated");
				String[] newMessage = new String[message.length];
				
				for (int i = 0; i < newMessage.length; i++) {
					newMessage[i] = req.queryParamOrDefault(i + "", "");
				}
				
				message = newMessage;
				res.redirect("/");
				return "Message updated";
			}
		});
		
		Spark.awaitInitialization();
		LOGGER.logp(Level.INFO, CLAZZ, "main", "Server started on port " + Spark.port());
	}
	
	private static String getFormattedMessage() {
		StringBuilder formattedMessage = new StringBuilder();

		String formatDateTime = LocalDateTime.now().format(formatter);
		formattedMessage.append(rPadOrTrim(formatDateTime, ROW_LENGTH));
		
		for (String line : message) {
			formattedMessage.append(rPadOrTrim(line, ROW_LENGTH));
		}
		
		return formattedMessage.toString();
	}
	
	private static String rPadOrTrim(String str, int maxLength) {
		while (str.length() < maxLength) {
			str += " ";
		}
		
		return str.substring(0, maxLength);
	}
}
