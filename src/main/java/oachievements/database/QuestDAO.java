package oachievements.database;

import oachievements.objects.PlayerQuest;
import oachievements.objects.User;
import oachievements.storages.QuestStorage;
import oachievements.storages.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class QuestDAO {

    private final String TABLE = "oachievements_quest";
    private QuestStorage questStorage;
    private Logger logger;

    public QuestDAO(QuestStorage questStorage, Logger logger) {
        this.questStorage = questStorage;
        this.logger = logger;
        createTable();
    }

    public void createTable() {
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                    "id VARCHAR(100) NOT NULL PRIMARY KEY," +
                    "owner VARCHAR(100) NOT NULL," +
                    "goal DOUBLE NOT NULL," +
                    "progress DOUBLE NOT NULL" +
                    ");";
            try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                statement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public User loadUser(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM `" + TABLE + "` WHERE owner = ?";
            try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                ResultSet resultSet = statement.executeQuery();
                HashMap<String, PlayerQuest> quests = new HashMap<>();
                while(resultSet.next()) {
                    String id = resultSet.getString(1);
                    String questId = id.split("\\.")[0];
                    if(!questStorage.containsQuest(questId)) continue;
                    quests.put(questId, new PlayerQuest(
                            id,
                            questId,
                            resultSet.getDouble(3),
                            resultSet.getDouble(4)
                    ));
                }
                return new User(quests);
            } catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        }).join();
    }

    public void unloadUser(UUID uuid, User user) {
        CompletableFuture.runAsync(() -> {
            user.getQuests().forEach((id, quest) -> {
                String sql = "REPLACE INTO " + TABLE + " VALUES(?,?,?,?)";
                try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                    statement.setString(1, quest.getId());
                    statement.setString(2, uuid.toString());
                    statement.setDouble(3, quest.getGoal());
                    statement.setDouble(4, quest.getProgress());
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        });
    }

    public void unloadAll(UserStorage userStorage) {
        userStorage.getUsers().forEach((owner, user) -> {
            user.getQuests().forEach((id, quest) -> {
                String sql = "REPLACE INTO " + TABLE + " VALUES(?,?,?,?)";
                try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                    statement.setString(1, quest.getId());
                    statement.setString(2, owner.toString());
                    statement.setDouble(3, quest.getGoal());
                    statement.setDouble(4, quest.getProgress());
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        });
    }

    public void deleteQuest(String id) {
        CompletableFuture.runAsync(() -> {
            String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
            try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                statement.setString(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
