package dev.revington.variables;

public class Parameter {

    // Client application API request parameter values
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String ACCOUNT_TYPE = "accountType";

    // Client application cookie names
    public static final String COOKIE_TOKEN = "access_token";

    // API return parameter values
    public static final String TEMPORARY_TYPE = "temp";
    public static final String RESULTS = "results";
    public static final String PAGES = "pages";
    public static final String ELEMENTS = "elements";

    // Roles
    public static final String ADMIN = "admin";
    public static final String STUDENT = "student";
    public static final String UNIFIED = "uni";

    // Mail
    public static String UserNotificationEmail = """
                Surge
                
                Hello,
                    An administrator has been created an account on Surge using this email address. Please
                visit to %s and fill out your details in order to complete your account.
                
                Temporary password : %s
                
                Thank you, Regards
                Surge
            """;

    // Headers
    public static final String REQUEST_ORIGIN = "origin";
}
