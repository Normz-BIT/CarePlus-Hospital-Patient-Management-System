package com.careplus.server.dao;

import java.util.List;

import com.careplus.common.model.ChatMessages;

public interface ChatDAO extends GenericDAO<ChatMessages> {
	List<ChatMessages> poll(int userId);

}
