package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.port.Integrations;
import com.undeadlydev.UTA.application.port.MessageProvider;
import com.undeadlydev.UTA.application.port.SettingsProvider;

/** Backs the {@code /utitleauth reload [config|lang]} command. */
public class ReloadUseCase {

    private final SettingsProvider settings;
    private final MessageProvider messages;
    private final Integrations integrations;

    @Inject
    public ReloadUseCase(SettingsProvider settings, MessageProvider messages, Integrations integrations) {
        this.settings = settings;
        this.messages = messages;
        this.integrations = integrations;
    }

    public void reloadAll() {
        settings.reload();
        messages.reload();
        integrations.reload();
    }

    public void reloadConfig() {
        settings.reload();
        integrations.reload();
    }

    public void reloadLang() {
        messages.reload();
    }
}
