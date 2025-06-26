public class UserSession {
    private static int sessionId;
    private static String sessionEmail;
    private static String sessionName;

    public static void startSession(int id, String userEmail, String userName) {
        sessionId = id;
        sessionEmail = userEmail;
        sessionName = userName;
    }

    public static int getClientId() {
        return sessionId;
    }

    public static String getEmail() {
        return sessionEmail;
    }

    public static String getName() {
        return sessionName;
    }

    public static void clearSession() {
        sessionId = 0;
        sessionEmail = null;
        sessionName = null;
    }
}
