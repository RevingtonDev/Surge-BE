package dev.revington.status;

import net.minidev.json.JSONObject;

public class StatusHandler {

    //
    private static JSONObject createStatus(int code, String description) {
        JSONObject result = new JSONObject();
        result.put("status", code);
        result.put("query", description);
        return result;
    }

    // Query success response
    public static JSONObject S200 = createStatus(200, "Query successful.");

    // Authentication failed response
    public static JSONObject E401 = createStatus(401, "Authentication failed.");

    // Internal server error response
    public static JSONObject E500 = createStatus(500, "Internal server error.");

    // Internal encryption error response
    public static JSONObject E1001 = createStatus(1001, "Encryption interrupted.");

    // Empty or invalid access token response
    public static JSONObject E1002 = createStatus(1002, "Unrecognized token.");

    // Role doesn't have permission to visit provided route response
    public static JSONObject E1003 = createStatus(1003, "Trespassing.");
}
