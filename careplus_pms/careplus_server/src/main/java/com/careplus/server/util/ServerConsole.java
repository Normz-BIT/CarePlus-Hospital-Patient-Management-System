package com.careplus.server.util;

/*
 * Somewhere to send progress messages. The server window implements it.
 *
 * This exists so Server and DatabaseResetService can report what they're doing
 * without importing Swing. They only know about this one method rather than
 * about ServerView, which keeps the networking and database code free of any UI
 * and means we could add a console-only server later without touching them.
 */
public interface ServerConsole {

	void showln(String message);
}
