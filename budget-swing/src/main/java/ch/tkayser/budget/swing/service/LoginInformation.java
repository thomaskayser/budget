package ch.tkayser.budget.swing.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jgoodies.binding.beans.Model;

public class LoginInformation extends Model {
    private static final long  serialVersionUID = 1L;

    public static final String PROP_SERVER      = "server";
    public static final String PROP_PORT        = "port";
    public static final String PROP_USERNAME    = "userName";
    public static final String PROP_PASSWORD    = "password";

    private String             server;
    private String             port;
    private String             userName;
    private String             password;

    public LoginInformation() {
    }

    public LoginInformation(String server, String port, String userName, String password) {
        super();
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static LoginInformation initDefaultFromConfig() {

        // leeres info erstellen
        LoginInformation info = new LoginInformation();

        // defaults aus file lesen
        Properties p = new Properties();
        InputStream stream = LoginInformation.class.getClassLoader().getResourceAsStream("login.properties");
        if (stream != null) {
            try {
                p.load(stream);
                info.setServer(p.getProperty("server"));
                info.setPort(p.getProperty("port"));
                info.setUserName(p.getProperty("user"));
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        return info;
    }
}
