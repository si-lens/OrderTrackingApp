package orderManager.dal.Properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private Properties prop = new Properties();

    public String read() {
        String dep = null;
        try (FileInputStream input = new FileInputStream("src\\orderManager\\dal\\Properties\\config.properties")) {
            prop.load(input);
            dep = prop.getProperty("chosenDepartment");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dep;
    }

    public void write(String department)
    {
        try (FileOutputStream output = new FileOutputStream("src\\orderManager\\dal\\Properties\\config.properties")) {
            prop.setProperty("chosenDepartment", department);
            prop.store(output, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean hasDepartment()
    {
        Boolean b = null;
        try (FileInputStream input = new FileInputStream("src\\orderManager\\dal\\Properties\\config.properties")) {
            prop.load(input);
            String content = prop.getProperty("chosenDepartment");
            String fixValue = "empty";
            b = content.matches(fixValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

}
