package orderManager.dal;

import java.sql.SQLException;
import java.util.List;

public interface IDAODetails {

    public List getDetails() throws SQLException;

    public boolean hasNewData();

    public void setHasNewData(boolean hasNewData);
}
