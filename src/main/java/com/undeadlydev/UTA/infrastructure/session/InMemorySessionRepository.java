package com.undeadlydev.UTA.infrastructure.session;

import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.SessionRepository;
import com.undeadlydev.UTA.application.port.TaskHandle;
import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.model.SavedExperience;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Thread-safe in-memory {@link SessionRepository}; all state is keyed by player {@link UUID}. */
@Singleton
public class InMemorySessionRepository implements SessionRepository {

    private static final class Session {
        private volatile AuthStage pendingStage;
        private volatile SavedExperience savedExperience;
        private final Map<String, TaskHandle> tasks = new ConcurrentHashMap<>();
    }

    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    private Session session(UUID playerId) {
        return sessions.computeIfAbsent(playerId, k -> new Session());
    }

    @Override
    public void setPendingStage(UUID playerId, AuthStage stage) {
        session(playerId).pendingStage = stage;
    }

    @Override
    public Optional<AuthStage> pendingStage(UUID playerId) {
        Session session = sessions.get(playerId);
        return session == null ? Optional.empty() : Optional.ofNullable(session.pendingStage);
    }

    @Override
    public void clearPending(UUID playerId) {
        Session session = sessions.get(playerId);
        if (session != null) {
            session.pendingStage = null;
        }
    }

    @Override
    public void storeTask(UUID playerId, String channel, TaskHandle handle) {
        TaskHandle previous = session(playerId).tasks.put(channel, handle);
        if (previous != null) {
            previous.cancel();
        }
    }

    @Override
    public boolean hasTask(UUID playerId, String channel) {
        Session session = sessions.get(playerId);
        return session != null && session.tasks.containsKey(channel);
    }

    @Override
    public void cancelTask(UUID playerId, String channel) {
        Session session = sessions.get(playerId);
        if (session == null) {
            return;
        }
        TaskHandle handle = session.tasks.remove(channel);
        if (handle != null) {
            handle.cancel();
        }
    }

    @Override
    public void saveExperience(UUID playerId, SavedExperience experience) {
        session(playerId).savedExperience = experience;
    }

    @Override
    public Optional<SavedExperience> savedExperience(UUID playerId) {
        Session session = sessions.get(playerId);
        return session == null ? Optional.empty() : Optional.ofNullable(session.savedExperience);
    }

    @Override
    public void clearSavedExperience(UUID playerId) {
        Session session = sessions.get(playerId);
        if (session != null) {
            session.savedExperience = null;
        }
    }

    @Override
    public void clear(UUID playerId) {
        Session session = sessions.remove(playerId);
        if (session != null) {
            session.tasks.values().forEach(TaskHandle::cancel);
            session.tasks.clear();
        }
    }
}
