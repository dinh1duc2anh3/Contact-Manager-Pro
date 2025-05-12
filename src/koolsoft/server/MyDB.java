package koolsoft.server;

import java.util.List;

public interface MyDB {
	List<ContactInfo> getAll();
	List<ContactInfo> getContactInfoFromName(String name);
}
