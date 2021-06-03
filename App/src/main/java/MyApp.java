import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.view.Views.*;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.event.AppMentionEvent;
import com.slack.api.model.event.StarAddedEvent;

import static com.slack.api.model.block.element.BlockElements.*;
public class MyApp {
    public static void main(String[] args) throws Exception {
        var app = new App();

        // All the room in the world for your code
        app.command("/hello", (req, ctx) -> {
            return ctx.ack(":wave: Hello!");
        });
        AddAppHome(app);
        AddMentions(app);

        var server = new SlackAppServer(app);
        server.start();
    }

    private static void AddMentions(App app) {
        app.event(AppMentionEvent.class, (payload, ctx) -> {

            ctx.say("hello");

            return ctx.ack();
        });
    }

    private static void AddAppHome(App app) {
        app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
            var appHomeView = view(view -> view
                    .type("home")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText(mt -> mt.text("*Welcome to your _App's Home_* :tada:")))),
                            divider(),
                            section(section -> section.text(markdownText(mt -> mt.text("This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on <https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>.")))),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.text(plainText(pt -> pt.text("Click me!"))).value("button1").actionId("button_1"))
                                    ))
                            )
                    ))
            );

            var res = ctx.client().viewsPublish(r -> r
                    .userId(payload.getEvent().getUser())
                    .view(appHomeView)
            );

            return ctx.ack();
        });
    }
}