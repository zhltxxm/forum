package dao;

import model.Section;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SectionDao {
    boolean createApplication(Connection conn, int userId, String name, String description) throws SQLException;
    List<Section> getPendingSections(Connection conn) throws SQLException;
    boolean approveSection(Connection conn, int applicationId, boolean approved) throws SQLException;
    boolean isModerator(Connection conn, int sectionId, int userId) throws SQLException;
    Section getById(Connection conn, int id) throws SQLException;
    List<Section> getUserSections(Connection conn, int userId) throws SQLException;
}