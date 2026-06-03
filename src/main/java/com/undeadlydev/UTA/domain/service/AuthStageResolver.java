package com.undeadlydev.UTA.domain.service;

import com.undeadlydev.UTA.domain.model.AuthStage;

import java.util.Optional;

/**
 * Pure business rules that map an authentication situation to the {@link AuthStage} to display.
 * No framework dependencies — fully unit-testable.
 */
public class AuthStageResolver {

    /**
     * Stage to show right after a player joins (once AuthMe has had a chance to auto-login).
     *
     * @return empty if the player is already authenticated and needs no notification.
     */
    public Optional<AuthStage> resolveOnJoin(boolean registered, boolean authenticated) {
        if (authenticated) {
            return Optional.empty();
        }
        if (!registered) {
            return Optional.of(AuthStage.NO_REGISTER);
        }
        return Optional.of(AuthStage.NO_LOGIN);
    }

    /** Stage after a successful AuthMe login: premium (FastLogin) players get the auto-login stage. */
    public AuthStage resolveOnLogin(boolean premium) {
        return premium ? AuthStage.AUTOLOGIN : AuthStage.LOGIN;
    }

    /**
     * Stage after registration. When AuthMe forces a login right after registering, the player is
     * still pending login; otherwise the registration is complete.
     */
    public AuthStage resolveOnRegister(boolean forceLoginAfterRegister) {
        return forceLoginAfterRegister ? AuthStage.NO_LOGIN : AuthStage.REGISTER;
    }
}
