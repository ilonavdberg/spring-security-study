package dev.ilona.springsecurity.infrastructure.email;

public final class EmailTemplate {

    private static final String INVITE_SUBJECT = "You're invited to register";

    public static EmailCommand invite(String sender, String recipient, String token) {
        return new EmailCommand(
                sender,
                recipient,
                INVITE_SUBJECT,
                buildInviteEmailBody(token)
        );
    }

    private static String buildInviteEmailBody(String token) {
        return """
                Hello,

                You have been invited to register an account.
                Registration token: %s

                """.formatted(token);
    }
}
