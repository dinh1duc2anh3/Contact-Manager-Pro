package koolsoft.server;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import koolsoft.shared.ContactInfo;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyOfyHelper implements ServletContextListener {
	@Override
    public void contextInitialized(ServletContextEvent event) {
    	
		Datastore datastore = DatastoreOptions.newBuilder()
                .setProjectId("demo-local") // Dùng project ID của bạn
                .setHost("http://localhost:8081") // Kết nối đến Datastore Emulator
                .build()
                .getService();

    	ObjectifyFactory factory = new ObjectifyFactory(datastore);
    	ObjectifyService.init(factory);
    	
        ObjectifyService.register(ContactInfo.class);
        System.out.println("Datastore Emulator is running.");
    }

	@Override
    public void contextDestroyed(ServletContextEvent event) {}
}