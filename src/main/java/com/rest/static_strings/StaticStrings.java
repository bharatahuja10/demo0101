package com.rest.static_strings;

public class StaticStrings {

	public static class Errors {
		public static final String errorEmpDetails = "All Required Employee Details Not Provided";
		public static final String errorNotFound = "Requested Resource Not Found On Server: ";

		public static String errorNotFound(int id) {
			return "Requested Resource Not Found On Server with ID: " + id;
		}
	}

}
