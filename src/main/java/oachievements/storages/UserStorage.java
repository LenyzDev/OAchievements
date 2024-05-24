package oachievements.storages;

import oachievements.database.QuestDAO;
import oachievements.objects.User;

import java.util.HashMap;
import java.util.UUID;

public class UserStorage {

    private HashMap<UUID, User> users;
    private QuestDAO questDAO;

    public UserStorage(QuestDAO questDAO) {
        this.users = new HashMap<>();
        this.questDAO = questDAO;
    }

    public void addUser(UUID uuid, User user) {
        this.users.put(uuid, user);
    }

    public User getUser(UUID uuid) {
        if (!this.users.containsKey(uuid)) {
            User user = this.questDAO.loadUser(uuid);
            this.users.put(uuid, user);
        }
        return this.users.get(uuid);
    }

    public void removeUser(UUID uuid) {
        this.users.remove(uuid);
    }

    public boolean containsUser(UUID uuid) {
        return this.users.containsKey(uuid);
    }

    public void deleteQuest(String id) {
        questDAO.deleteQuest(id);
    }

    public HashMap<UUID, User> getUsers() {
        return this.users;
    }
}
