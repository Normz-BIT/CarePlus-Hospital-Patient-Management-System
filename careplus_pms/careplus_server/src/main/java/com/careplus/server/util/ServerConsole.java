package com.careplus.server.util;

/*
 * A destination for progress messages, implemented by the server window.
 *
 * Exists so that Server and DatabaseResetService can report progress without
 * importing Swing. They depend on this one method interface instead of on
 * ServerView, which keeps the networking and persistence code headless and
 * testable, and leaves the door open for a console only server build.
 *
 */
public interface ServerConsole {

	void println(String message);
}
