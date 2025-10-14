package astra.testutil;

import astra.ui.Ui;
import java.util.ArrayList;
import java.util.List;

public class TestUi extends Ui {
    public final List<String> messages = new ArrayList<>();
    public final List<String> errors = new ArrayList<>();
    @Override public void showMessage(String msg) { messages.add(msg); }
    @Override public void showError(String msg) { errors.add(msg); }
    @Override public void showLogo() {}
    @Override public void showBotIntro() {}
    @Override public void showPrompt() {}
    @Override public void showDash() {}
    @Override public void showDone() {}
    @Override public void showEnd() {}
    @Override public void showHelp() { messages.add("HELP"); }
}