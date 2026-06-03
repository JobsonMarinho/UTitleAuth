package com.undeadlydev.UTA.application.port;

import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.model.SavedExperience;

import java.util.Optional;
import java.util.UUID;

/**
 * Per-player runtime state while a player is pending registration/login: the current pending
 * {@link AuthStage}, the handles of the repeating notification tasks, and the player's saved XP.
 * Everything is keyed by {@link UUID} (the old name-vs-UUID inconsistency is gone).
 */
public interface SessionRepository {

    void setPendingStage(UUID playerId, AuthStage stage);

    Optional<AuthStage> pendingStage(UUID playerId);

    void clearPending(UUID playerId);

    /** Stores a task handle for a channel, cancelling any previous handle for that channel. */
    void storeTask(UUID playerId, String channel, TaskHandle handle);

    boolean hasTask(UUID playerId, String channel);

    void cancelTask(UUID playerId, String channel);

    void saveExperience(UUID playerId, SavedExperience experience);

    Optional<SavedExperience> savedExperience(UUID playerId);

    void clearSavedExperience(UUID playerId);

    /** Cancels every task for the player and drops all stored state. */
    void clear(UUID playerId);
}
