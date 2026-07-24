package com.careplus.server.util;

/*
 * Somewhere to send progress messages. The server window implements it.
 *
 * This exists so Server and DatabaseResetService can report what they're doing.
 * They only know about this one method rather than
 * about ServerView, which keeps the networking and database code free of any UI.
 */
public interface ServerConsole {

	void showln(String message);
}
