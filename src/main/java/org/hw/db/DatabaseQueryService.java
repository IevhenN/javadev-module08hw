package org.hw.db;

import lombok.Data;
import org.hw.reader.ReaderSQLFiles;
import org.hw.report.*;
import org.hw.settings.Settings;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
public class DatabaseQueryService {

    private static final Logger logger = Logger.getLogger(DatabaseQueryService.class.getName());

    public static void main(String[] args) {
        DataBase dataBase = DataBase.getInstance();
        Connection connection = dataBase.getConnection();

        DatabaseQueryService databaseQueryService = new DatabaseQueryService();

        List<LongestProject> longestProjects = databaseQueryService.findLongestProject(connection);
        logger.info(longestProjects.toString());

        List<MaxProjectsClient> maxProjectsClients = databaseQueryService.findMaxProjectsClient(connection);
        logger.info(maxProjectsClients.toString());

        List<MaxSalaryWorker> maxSalaryWorkers = databaseQueryService.findMaxSalaryWorker(connection);
        logger.info(maxSalaryWorkers.toString());

        List<YoungestOldestWorkers> youngestOldestWorkers = databaseQueryService.findYoungestOldestWorkers(connection);
        logger.info(youngestOldestWorkers.toString());

        List<PrintProjectPrices> printProjectPrices = databaseQueryService.findPrintProjectPrices(connection);
        logger.info(printProjectPrices.toString());

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LongestProject> findLongestProject(Connection connection) {
        String script = new ReaderSQLFiles().getScript(Settings.FILE_NAME_FIND_LONGEST_PROJECT);
        List<LongestProject> longestProjects = fillClassData(LongestProject.class, connection, script);
        return longestProjects;
    }

    public List<MaxProjectsClient> findMaxProjectsClient(Connection connection) {
        String script = new ReaderSQLFiles().getScript(Settings.FILE_NAME_FIND_MAX_PROJECTS_CLIENT);
        List<MaxProjectsClient> maxProjectsClients = fillClassData(MaxProjectsClient.class, connection, script);
        return maxProjectsClients;
    }

    public List<MaxSalaryWorker> findMaxSalaryWorker(Connection connection) {
        String script = new ReaderSQLFiles().getScript(Settings.FILE_NAME_FIND_MAX_SALARY_WORKER);
        List<MaxSalaryWorker> maxSalaryWorkers = fillClassData(MaxSalaryWorker.class, connection, script);
        return maxSalaryWorkers;
    }

    public List<YoungestOldestWorkers> findYoungestOldestWorkers(Connection connection) {
        String script = new ReaderSQLFiles().getScript(Settings.FILE_NAME_FIND_YOUNGEST_OLDEST_WORKERS);
        List<YoungestOldestWorkers> youngestOldestWorkers = fillClassData(YoungestOldestWorkers.class, connection, script);
        return youngestOldestWorkers;
    }

    public List<PrintProjectPrices> findPrintProjectPrices(Connection connection) {
        String script = new ReaderSQLFiles().getScript(Settings.FILE_NAME_PRINT_PROJECT_PRICES);
        List<PrintProjectPrices> printProjectPrices = fillClassData(PrintProjectPrices.class, connection, script);
        return printProjectPrices;
    }

    public <T extends Reportable<T>> List<T> fillClassData(Class<T> tClass, Connection connection, String script) {
        List<T> classes = new ArrayList<>();
        try {
            ResultSet resultSet = connection.prepareStatement(script).executeQuery();
            while (resultSet.next()) {
                classes.add(tClass.getDeclaredConstructor().newInstance().setItems(resultSet));
            }
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.fillInStackTrace();
        }
        return classes;
    }
}
